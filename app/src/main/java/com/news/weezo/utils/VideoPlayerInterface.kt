package com.news.weezo.utils

interface VideoPlayerInterface {

    fun setListener(videoFragmentInterface: VideoFragmentInterface)
    fun enterInFullScreenMode()
    fun exitFullScreenMode()

}