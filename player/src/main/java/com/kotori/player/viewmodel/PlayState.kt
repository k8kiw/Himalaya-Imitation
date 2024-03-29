package com.kotori.player.viewmodel

/**
 * 播放状态密封类，配合 StateFlow 使用
 * 简化SDK里繁杂的播放器界面回调
 */
sealed class PlayState {

    /**
     * 正在缓冲，缓冲进度为百分比
     * 需要注意缓冲与播放是同时进行的
     */
    data class Loading(val progress: Int) : PlayState()

    /**
     * 正在播放，单位是毫秒
     * @param position 当前播放进度
     * @param duration 声音时长
     */
    data class Playing(val position: Int, val duration: Int) : PlayState()

    /**
     * 加载、播放错误
     */
    data class Error(val error: Throwable) : PlayState()

    /**
     * 播放暂停、停止
     */
    object Pause : PlayState()
}
