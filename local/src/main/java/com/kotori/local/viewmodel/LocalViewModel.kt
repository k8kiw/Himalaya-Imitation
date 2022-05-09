package com.kotori.local.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kotori.common.utils.LogUtil
import com.kotori.local.repository.LocalRepository
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LocalViewModel(private val repository: LocalRepository) : ViewModel() {

    private val _albumList = MutableStateFlow(emptyList<Album>())
        .also { albumListStateFlow ->
            viewModelScope.launch {
                repository.getAllSubscribedAlbums().collect {
                    albumListStateFlow.value = it
                }
            }
        }

    val albumList = _albumList.asStateFlow()




    val albumListLiveData = repository.getAllSubscribedAlbums().flatMapLatest { albums ->
        LogUtil.d("LocalFragment", "dataset changed: ${albums.size}")
        flow {
            emit(albums)
        }
    }.asLiveData()

    val liveDataFromRoom = repository.getAlbumsWithLiveData()

    fun refreshData() {
        viewModelScope.launch(Dispatchers.IO) {
            val newData = repository.getAlbumsWithCoroutines()
            _albumList.value = newData
        }
    }
}