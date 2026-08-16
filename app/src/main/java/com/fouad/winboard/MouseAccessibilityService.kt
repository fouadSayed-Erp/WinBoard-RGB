package com.fouad.winboard

import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView

class MouseAccessibilityService : AccessibilityService() {

    companion object {
        var instance: MouseAccessibilityService? = null
    }

    private var cursorView: ImageView? = null
    private var windowManager: WindowManager? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    fun moveCursor(x: Float, y: Float) {
        // مبدئياً هنظهر مؤشر بسيط - بعدين نضيف تحريك حقيقي
    }
}
