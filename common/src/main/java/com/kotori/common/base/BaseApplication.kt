package com.kotori.common.base

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.kotori.common.DemoHelper
import com.kotori.common.receiver.MyPlayerReceiver
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import com.ximalaya.ting.android.opensdk.auth.constants.XmlyConstants
import com.ximalaya.ting.android.opensdk.constants.ConstantsOpenSdk
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants
import com.ximalaya.ting.android.opensdk.datatrasfer.AccessTokenManager
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest.ITokenStateChange
import com.ximalaya.ting.android.opensdk.datatrasfer.DeviceInfoProviderDefault
import com.ximalaya.ting.android.opensdk.datatrasfer.IDeviceInfoProvider
import com.ximalaya.ting.android.opensdk.httputil.XimalayaException
import com.ximalaya.ting.android.opensdk.player.appnotification.XmNotificationCreater
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerConfig
import com.ximalaya.ting.android.opensdk.util.BaseUtil
import com.ximalaya.ting.android.opensdk.util.Logger
import com.ximalaya.ting.android.opensdk.util.SharedPreferencesUtil
import com.ximalaya.ting.android.player.XMediaPlayerConstants
import com.ximalaya.ting.android.sdkdownloader.XmDownloadManager
import com.ximalaya.ting.android.sdkdownloader.http.RequestParams
import com.ximalaya.ting.android.sdkdownloader.http.app.RequestTracker
import com.ximalaya.ting.android.sdkdownloader.http.request.UriRequest
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import org.xutils.x
import java.io.IOException


open class BaseApplication : MultiDexApplication(), DemoHelper.AppIdsUpdater {

    companion object {
        const val REFRESH_TOKEN_URL = "https://api.ximalaya.com/oauth2/refresh_token?"
        const val KEY_LAST_OAID = "last_oaid"

        // app相关
        private const val APP_KEY = "ab6cdee3a29df9a2eb5a2f1d9ec1fb48"
        private const val APP_SECRET = "3f02472f86cd0ab340c2099937d1390a"
        private const val PACK_ID = "com.kotori.mobilefmplayer"


        /**
         * 当前 DEMO 应用的回调页，第三方应用应该使用自己的回调页。
         */
        val REDIRECT_URL =
            if (DTransferConstants.isRelease)
                "http://api.ximalaya.com/openapi-collector-app/get_access_token"
            else
                "http://api.test.ximalaya.com/openapi-collector-app/get_access_token"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    private lateinit var oaid: String

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        // 各种初始化
        x.Ext.init(this)
        QMUISwipeBackActivityManager.init(this)

        this.initSDK()

    }

    private fun initSDK() {
        ConstantsOpenSdk.isDebug = true
        XMediaPlayerConstants.isDebug = true

        XmPlayerConfig.getInstance(this).setDefualtNotificationNickNameAndInfo(
            "开心麻花", "开心开心,无敌开心")

        if (BaseUtil.isMainProcess(this)) {
            oaid = SharedPreferencesUtil.getInstance(this).getString(KEY_LAST_OAID)
            DemoHelper { ids ->
                oaid = ids

                SharedPreferencesUtil.getInstance(this).saveString(KEY_LAST_OAID, ids)

                println("TingApplication.OnOaidAvalid  $ids")

            }.getDeviceIds(this)

            val mp3 = getExternalFilesDir("mp3")!!.absolutePath
            println("地址是  $mp3")

            // 初始化SDK
            CommonRequest.getInstanse().apply {
                if (DTransferConstants.isRelease) {
                    setAppkey(APP_KEY)
                    setPackid(PACK_ID)
                    init(this@BaseApplication, APP_SECRET, true,
                        getDeviceInfoProvider(this@BaseApplication))
                } else {
                    setAppkey(APP_KEY)
                    setPackid(PACK_ID)
                    init(this@BaseApplication, APP_SECRET,
                        getDeviceInfoProvider(this@BaseApplication))
                }
            }

            AccessTokenManager.getInstanse().init(this)
            if (AccessTokenManager.getInstanse().hasLogin()) {
                registerLoginTokenChangeListener(this)
            }

            // 下载sdk
            XmDownloadManager.Builder(this)
                .maxDownloadThread(3) // 最大的下载个数 默认为1 最大为3
                .maxSpaceSize(Long.MAX_VALUE) // 设置下载文件占用磁盘空间最大值，单位字节。不设置没有限制
                .connectionTimeOut(15000) // 下载时连接超时的时间 ,单位毫秒 默认 30000
                .readTimeOut(15000) // 下载时读取的超时时间 ,单位毫秒 默认 30000
                .fifo(false) // 等待队列的是否优先执行先加入的任务. false表示后添加的先执行(不会改变当前正在下载的音频的状态) 默认为true
                .maxRetryCount(3) // 出错时重试的次数 默认2次
                .progressCallBackMaxTimeSpan(1000) //  进度条progress 更新的频率 默认是800
                .requestTracker(requestTracker) // 日志 可以打印下载信息
                .savePath(mp3) // 保存的地址 会检查这个地址是否有效
                .create()
        }

        if (BaseUtil.isPlayerProcess(this)) {
            val instanse = XmNotificationCreater.getInstanse(this)
            instanse.setNextPendingIntent(null as PendingIntent?)
            instanse.setPrePendingIntent(null as PendingIntent?)

            val actionName = "com.app.test.android.Action_Close"
            val intent = Intent(actionName)
            intent.setClass(this, MyPlayerReceiver::class.java)

            val broadcast = PendingIntent.getBroadcast(this, 0, intent, 0)
            instanse.setClosePendingIntent(broadcast)


            val pauseActionName = "com.app.test.android.Action_PAUSE_START"
            val intent1 = Intent(pauseActionName)
            intent1.setClass(this, MyPlayerReceiver::class.java)

            val broadcast1 = PendingIntent.getBroadcast(this, 0, intent1, 0)
            instanse.setStartOrPausePendingIntent(broadcast1)
        }
    }


    override fun OnOaidAvalid(ids: String) {
        oaid = ids

        SharedPreferencesUtil.getInstance(context).saveString(KEY_LAST_OAID, ids)

        println("TingApplication.OnOaidAvalid  $ids")
    }


    private fun getDeviceInfoProvider(context: Context?): IDeviceInfoProvider {
        return object : DeviceInfoProviderDefault(context) {
            override fun oaid(): String {
                // 合作方要尽量优先回传用户真实的oaid，
                // 使用oaid可以关联并打通喜马拉雅主app中记录的用户画像数据，
                // 对后续个性化推荐接口推荐给用户内容的准确性会有极大的提升！
                return oaid
            }
        }
    }

    fun unregisterLoginTokenChangeListener() {
        CommonRequest.getInstanse().iTokenStateChange = null
    }

    private fun registerLoginTokenChangeListener(context: Context) {
        // 使用此回调了就表示贵方接了需要用户登录才能访问的接口,如果没有此类接口可以不用设置此接口,之前的逻辑没有发生改变
        CommonRequest.getInstanse().iTokenStateChange = object : ITokenStateChange {
            // 此接口表示token已经失效 ,
            override fun getTokenByRefreshSync(): Boolean {
                if (!TextUtils.isEmpty(AccessTokenManager.getInstanse().refreshToken)) {
                    try {
                        return refreshSync()
                    } catch (e: XimalayaException) {
                        e.printStackTrace()
                    }
                }
                return false
            }

            override fun getTokenByRefreshAsync(): Boolean {
                if (!TextUtils.isEmpty(AccessTokenManager.getInstanse().refreshToken)) {
                    try {
                        refresh()
                        return true
                    } catch (e: XimalayaException) {
                        e.printStackTrace()
                    }
                }
                return false
            }

            override fun tokenLosted() {
                // 参考demo
                /*val intent = Intent(context, XMAuthDemoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)*/
            }
        }
    }


    @Throws(XimalayaException::class)
    fun refresh() {
        val client = OkHttpClient().newBuilder()
            .followRedirects(false)
            .build()
        val builder = FormBody.Builder().apply {
            add(XmlyConstants.AUTH_PARAMS_GRANT_TYPE, "refresh_token")
            add(
                XmlyConstants.AUTH_PARAMS_REFRESH_TOKEN,
                AccessTokenManager.getInstanse().tokenModel.refreshToken
            )
            add(XmlyConstants.AUTH_PARAMS_CLIENT_ID, CommonRequest.getInstanse().appKey)
            add(XmlyConstants.AUTH_PARAMS_DEVICE_ID, CommonRequest.getInstanse().deviceId)
            add(XmlyConstants.AUTH_PARAMS_CLIENT_OS_TYPE, XmlyConstants.ClientOSType.ANDROID)
            add(XmlyConstants.AUTH_PARAMS_PACKAGE_ID, CommonRequest.getInstanse().packId)
            add(XmlyConstants.AUTH_PARAMS_UID, AccessTokenManager.getInstanse().uid)
            add(XmlyConstants.AUTH_PARAMS_REDIRECT_URL, REDIRECT_URL)
        }


        val body = builder.build()
        val request = Request.Builder()
            .url("https://api.ximalaya.com/oauth2/refresh_token?")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Logger.d("refresh", "refreshToken, request failed, error message = " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                //val statusCode = response.code()
                val json = response.body()!!.string()
                println("TingApplication.refreshSync  1  $json")
                var jsonObject: JSONObject? = null
                try {
                    jsonObject = JSONObject(json)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (jsonObject != null) {
                    AccessTokenManager.getInstanse().setAccessTokenAndUid(
                        jsonObject.optString("access_token"),
                        jsonObject.optString("refresh_token"),
                        jsonObject.optLong("expires_in"),
                        jsonObject
                            .optString("uid")
                    )
                }
            }
        })
    }

    @Throws(XimalayaException::class)
    fun refreshSync(): Boolean {
        val client = OkHttpClient().newBuilder()
            .followRedirects(false)
            .build()
        val builder = FormBody.Builder()
        builder.add(XmlyConstants.AUTH_PARAMS_GRANT_TYPE, "refresh_token")
        builder.add(
            XmlyConstants.AUTH_PARAMS_REFRESH_TOKEN,
            AccessTokenManager.getInstanse().tokenModel.refreshToken
        )
        builder.add(XmlyConstants.AUTH_PARAMS_CLIENT_ID, CommonRequest.getInstanse().appKey)
        builder.add(XmlyConstants.AUTH_PARAMS_DEVICE_ID, CommonRequest.getInstanse().deviceId)
        builder.add(XmlyConstants.AUTH_PARAMS_CLIENT_OS_TYPE, XmlyConstants.ClientOSType.ANDROID)
        builder.add(XmlyConstants.AUTH_PARAMS_PACKAGE_ID, CommonRequest.getInstanse().packId)
        builder.add(XmlyConstants.AUTH_PARAMS_UID, AccessTokenManager.getInstanse().uid)
        builder.add(XmlyConstants.AUTH_PARAMS_REDIRECT_URL, REDIRECT_URL)
        val body = builder.build()
        val request: Request = Request.Builder()
            .url(REFRESH_TOKEN_URL)
            .post(body)
            .build()
        try {
            val execute = client.newCall(request).execute()
            if (execute.isSuccessful) {
                try {
                    val string = execute.body()!!.string()
                    val jsonObject = JSONObject(string)
                    println("TingApplication.refreshSync  2  $string")
                    AccessTokenManager.getInstanse().setAccessTokenAndUid(
                        jsonObject.optString("access_token"),
                        jsonObject.optString("refresh_token"),
                        jsonObject.optLong("expires_in"),
                        jsonObject
                            .optString("uid")
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private val requestTracker: RequestTracker = object : RequestTracker {
        override fun onWaiting(params: RequestParams) {
            Logger.log("TingApplication : onWaiting $params")
        }

        override fun onStart(params: RequestParams) {
            Logger.log("TingApplication : onStart $params")
        }

        override fun onRequestCreated(request: UriRequest) {
            Logger.log("TingApplication : onRequestCreated $request")
        }

        override fun onSuccess(request: UriRequest, result: Any) {
            Logger.log("TingApplication : onSuccess $request   result = $result")
        }

        override fun onRemoved(request: UriRequest) {
            Logger.log("TingApplication : onRemoved $request")
        }

        override fun onCancelled(request: UriRequest) {
            Logger.log("TingApplication : onCanclelled $request")
        }

        override fun onError(request: UriRequest, ex: Throwable, isCallbackError: Boolean) {
            Logger.log("TingApplication : onError $request   ex = $ex   isCallbackError = $isCallbackError")
        }

        override fun onFinished(request: UriRequest) {
            Logger.log("TingApplication : onFinished $request")
        }
    }


}