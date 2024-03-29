package com.kotori.common.sdk

import com.kotori.common.utils.LogUtil
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.album.AlbumList
import com.ximalaya.ting.android.opensdk.model.album.DiscoveryRecommendAlbumsList
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList
import com.ximalaya.ting.android.opensdk.model.category.Category
import com.ximalaya.ting.android.opensdk.model.category.CategoryList
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.model.track.TrackList
import com.ximalaya.ting.android.opensdk.model.word.HotWord
import com.ximalaya.ting.android.opensdk.model.word.HotWordList
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords
import java.util.concurrent.TimeoutException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


private const val TAG = "SDKTest"

/**
 * 封装SDK的参数设置、复杂的返回值、回调
 * 尽量返回一个非空的类型
 */
object SDKCallbackExt {

    /**
     * 获取所有分类
     */
    suspend fun getAllCategoryList(specificParams: Map<String, String>? = null) : List<Category> {
        return suspendCoroutine { continuation ->
            // 调用sdk
            CommonRequest.getCategories(specificParams, object : IDataCallBack<CategoryList> {
                override fun onSuccess(p0: CategoryList?) {
                    // 就算回调是空的，自己也应该返回一个空list而不是null
                    val result = p0?.categories ?: ArrayList<Category>()
                    continuation.resume(result)
                }

                override fun onError(p0: Int, p1: String?) {
                    continuation.resumeWithException(TimeoutException(p1))
                }

            })
        }
    }

    /**
     * 获取推荐专辑，将全区的编辑推荐整合为一个大列表
     * 请求一次，缓存下来，之后就不需要请求网络了
     * index需要进行限制，不能无限多
     */
    private val allResultList = ArrayList<Album>()
    suspend fun getRecommendAlbumList(startIndex : Int, endIndex : Int) : List<Album> {
        return suspendCoroutine { continuation ->
            // 若列表里有数据直接返回，不然就请求一次
            if (allResultList.isNotEmpty()) {
                // 根据index返回实际的结果
                val result = allResultList.subList(startIndex, endIndex)
                // 这个resume不会截断后面的代码，不能resume多次，需要else
                continuation.resume(result)
            } else {
                // 设置参数，每类推荐10个
                val params = mapOf(
                    DTransferConstants.DISPLAY_COUNT to 10.toString()
                )
                // 调用编辑推荐
                CommonRequest.getDiscoveryRecommendAlbums(params, object : IDataCallBack<DiscoveryRecommendAlbumsList> {
                    override fun onSuccess(p0: DiscoveryRecommendAlbumsList?) {
                        // 此处的返回是每个分类10个的列表，要将各个分类整个为统一的列表
                        // 拿到结果
                        val recommendAlbumsList = p0?.discoveryRecommendAlbumses
                        // 遍历每个分类的列表
                        recommendAlbumsList?.forEach { recommendAlbums ->
                            // 每个分类下实际的list
                            val currentAlbumList = recommendAlbums.albumList
                            // 将该list插入到结果中
                            allResultList += currentAlbumList
                        }
                        // 根据index返回实际的结果
                        val result = allResultList.subList(startIndex, endIndex)
                        continuation.resume(result)
                    }

                    override fun onError(p0: Int, p1: String?) {
                        LogUtil.d(TAG, "getRecommendAlbumList --> error code : $p0")
                        LogUtil.d(TAG, "getRecommendAlbumList --> error msg : $p1")
                        continuation.resumeWithException(TimeoutException(p1))
                    }

                })
            }

        }
    }

    /**
     * 根据分类id，返回该分类的专辑列表，按播放量排序
     */
    suspend fun getAlbumList(id: Int, page: Int) : List<Album> {
        return suspendCoroutine { continuation ->
            val map = mapOf(
                DTransferConstants.CATEGORY_ID to id.toString(),
                DTransferConstants.CALC_DIMENSION to "3",
                DTransferConstants.PAGE to page.toString(),
                DTransferConstants.PAGE_SIZE to 50.toString()
            )
            CommonRequest.getAlbumList(map, object : IDataCallBack<AlbumList> {
                override fun onSuccess(p0: AlbumList?) {
                    val list = p0?.albums ?: emptyList()
                    continuation.resume(list)
                }

                override fun onError(p0: Int, p1: String?) {
                    continuation.resumeWithException(TimeoutException(p1))
                }

            })
        }
    }

    /**
     * 获取一张专辑内的内容
     * @param album 要查询的专辑
     * @param page 当前页数
     */
    suspend fun getTrackByAlbum(album : Album, page : Int) : List<Track> {
        return suspendCoroutine { continuation ->
            // 得到 id
            val albumId = album.id
            // 设置参数
            val map = mapOf(
                DTransferConstants.ALBUM_ID to albumId.toString(),
                DTransferConstants.SORT to "asc",
                DTransferConstants.PAGE to page.toString(),
                DTransferConstants.PAGE_SIZE to 50.toString()
            )
            // 调用接口
            CommonRequest.getTracks(map, object : IDataCallBack<TrackList> {
                override fun onSuccess(p0: TrackList?) {
                    val result = p0?.tracks ?: ArrayList<Track>()
                    // 果然只会返回20个，参数无效
                    LogUtil.d(TAG, "getTrackByAlbum ---> result size : ${result.size}")
                    continuation.resume(result)
                }

                override fun onError(p0: Int, p1: String?) {
                    LogUtil.d(TAG, "getTrackByAlbum --> error code : $p0")
                    LogUtil.d(TAG, "getTrackByAlbum --> error msg : $p1")
                    continuation.resumeWithException(TimeoutException(p1))
                }

            })
        }
    }

    /**
     * 供播放器自动切页的common list，从一开始即可
     */
    suspend fun getCommonTrackListByAlbumId(albumId: Long) : TrackList {
        return suspendCoroutine { continuation ->
            val map = mapOf(
                DTransferConstants.ALBUM_ID to albumId.toString(),
                DTransferConstants.SORT to "asc",
                DTransferConstants.PAGE to 1.toString(),
                DTransferConstants.DISPLAY_COUNT to 50.toString()
            )
            // 调用接口
            CommonRequest.getTracks(map, object : IDataCallBack<TrackList> {
                override fun onSuccess(p0: TrackList?) {
                    p0?.let {
                        continuation.resume(it)
                    }
                }

                override fun onError(p0: Int, p1: String?) {
                    continuation.resumeWithException(TimeoutException(p1))
                }

            })
        }
    }


    /**
     * ========================== 搜索 =======================================
     */

    /**
     * 根据关键字搜索专辑(实际执行的搜索)
     */
    suspend fun getSearchedAlbums(keyword: String, page: Int) : List<Album> {
        return suspendCoroutine { continuation ->
            // 设置参数
            val map = mapOf(
                DTransferConstants.SEARCH_KEY to keyword,
                DTransferConstants.PAGE to page.toString(),
                DTransferConstants.PAGE_SIZE to 50.toString()
            )
            // 搜索
            CommonRequest.getSearchedAlbums(map, object : IDataCallBack<SearchAlbumList> {
                override fun onSuccess(p0: SearchAlbumList?) {
                    val result = p0?.albums ?: ArrayList()
                    LogUtil.d(TAG, "getSearchedAlbums --> result size : ${result.size}")
                    continuation.resume(result)
                }

                override fun onError(p0: Int, p1: String?) {
                    LogUtil.d(TAG, "getSearchedAlbums --> error code : $p0")
                    LogUtil.d(TAG, "getSearchedAlbums --> error msg : $p1")
                    continuation.resumeWithException(TimeoutException(p1))
                }

            })
        }
    }


    /**
     * 输入搜索内容时的实时联想，QueryResult标明了keyword和需要高光的部分
     */
    suspend fun getSuggestWord(keyword: String) : List<ParcelableQueryResult> {
        return suspendCoroutine { continuation ->
            // 设置参数
            val map = mapOf(
                DTransferConstants.SEARCH_KEY to keyword
            )
            // 搜索
            CommonRequest.getSuggestWord(map, object : IDataCallBack<SuggestWords> {
                override fun onSuccess(p0: SuggestWords?) {
                    val result = p0?.keyWordList ?: ArrayList()
                    LogUtil.d(TAG, "getSuggestWord --> result size : ${result.size}")
                    if (result.isNotEmpty()) {
                        // QueryResult转换
                        val myResult = result.map { ParcelableQueryResult(it) }
                        myResult.forEach {
                            LogUtil.d(TAG, "myResult -> ${it.keyword}")
                        }
                        continuation.resume(myResult)
                    } else {
                        val myResult = ParcelableQueryResult()
                        myResult.keyword = ""
                        continuation.resume(listOf(myResult))
                    }
                }

                override fun onError(p0: Int, p1: String?) {
                    LogUtil.d(TAG, "getSuggestWord --> error code : $p0")
                    LogUtil.d(TAG, "getSuggestWord --> error msg : $p1")
                    continuation.resumeWithException(TimeoutException(p1))
                }
            })
        }
    }

    /**
     * 获取热搜词，默认为前十个
     */
    suspend fun getHotWords(topNum: Int = 10) : List<HotWord> {
        return suspendCoroutine { continuation ->
            val map = mapOf(DTransferConstants.TOP to topNum.toString())
            CommonRequest.getHotWords(map, object : IDataCallBack<HotWordList> {
                override fun onSuccess(p0: HotWordList?) {
                    val result = p0?.hotWordList ?: ArrayList()
                    LogUtil.d(TAG, "getHotWords --> result size : ${result.size}")
                    continuation.resume(result)
                }

                override fun onError(p0: Int, p1: String?) {
                    LogUtil.d(TAG, "getHotWords --> error code : $p0")
                    LogUtil.d(TAG, "getHotWords --> error msg : $p1")
                    continuation.resumeWithException(TimeoutException(p1))
                }

            })
        }
    }
}
