package com.codingbuffalo.aerialdream

import android.service.dreams.DreamService
import android.view.KeyEvent
import android.view.View
import com.billy.android.swipe.SmartSwipe
import com.billy.android.swipe.SmartSwipeWrapper
import com.billy.android.swipe.SwipeConsumer
import com.billy.android.swipe.consumer.StayConsumer
import com.billy.android.swipe.listener.SimpleSwipeListener

class AerialDream : DreamService() {
    private var videoController: VideoController? = null
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isFullscreen = true
        isInteractive = true
        videoController = VideoController(this)
        val view = videoController!!.view
        setContentView(view)
        SmartSwipe.wrap(view)
                .addConsumer(StayConsumer())
                .enableHorizontal()
                .addListener(object : SimpleSwipeListener() {
                    override fun onSwipeOpened(wrapper: SmartSwipeWrapper, consumer: SwipeConsumer, direction: Int) {
                        if (direction == 1 || direction == 2) {
                            videoController!!.skipVideo()
                        }
                    }
                })
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_UP &&
                event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            videoController!!.skipVideo()
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    override fun onDreamingStopped() {
        videoController!!.stop()
        super.onDreamingStopped()
    }
}