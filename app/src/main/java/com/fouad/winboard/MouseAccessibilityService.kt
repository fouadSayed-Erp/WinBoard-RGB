package com.fouad.winboard

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.ImageView

class MouseAccessibilityService : AccessibilityService() {
    companion object {
        var instance: MouseAccessibilityService? = null
        var sensitivity: Float = 1.6f
    }

    private var cursorView: ImageView? = null
    private var wm: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null
    private var curX = 500f
    private var curY = 500f

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        cursorView = ImageView(this).apply {
            setImageResource(android.R.drawable.presence_online)
        }
        params = WindowManager.LayoutParams(
            40, 40,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = curX.toInt()
            y = curY.toInt()
        }
        try { wm?.addView(cursorView, params) } catch (e: Exception) {}
    }

    fun moveBy(dx: Float, dy: Float) {
        curX += dx * sensitivity * 2
        curY += dy * sensitivity * 2
        params?.x = curX.toInt()
        params?.y = curY.toInt()
        try { wm?.updateViewLayout(cursorView, params) } catch (e: Exception) {}
    }

    fun click() {
        val path = Path().apply { moveTo(curX, curY) }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, 100))
            .build()
        dispatchGesture(gesture, null, null)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
    override fun onDestroy() {
        super.onDestroy()
        try { wm?.removeView(cursorView) } catch (e: Exception) {}
        instance = null
    }
}
