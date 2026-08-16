package com.fouad.winboard
import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.view.WindowManager
import android.widget.ImageView
import android.graphics.Path
import android.accessibilityservice.GestureDescription
class MousePointerService: AccessibilityService(){
 companion object{ var instance: MousePointerService?=null }
 private var cursorView: ImageView?=null
 private var params: WindowManager.LayoutParams?=null
 private var cx=500f; private var cy=500f
 override fun onServiceConnected(){ instance=this; showCursor() }
 fun showCursor(){ if(cursorView!=null) return; val wm=getSystemService(WINDOW_SERVICE) as WindowManager; cursorView=ImageView(this).apply{ setImageResource(android.R.drawable.ic_menu_compass) }; params=WindowManager.LayoutParams(60,60,WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,PixelFormat.TRANSLUCENT); wm.addView(cursorView, params) }
 fun hideCursor(){ cursorView?.let{ (getSystemService(WINDOW_SERVICE) as WindowManager).removeView(it); cursorView=null } }
 fun moveCursor(dx: Float, dy: Float){ cx+=dx; cy+=dy; params?.x=cx.toInt(); params?.y=cy.toInt(); cursorView?.let{ (getSystemService(WINDOW_SERVICE) as WindowManager).updateViewLayout(it, params) } }
 fun clickAtCurrentPos(){ val p=Path().apply{ moveTo(cx,cy) }; val g=GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(p,0,50)).build(); dispatchGesture(g,null,null) }
 fun rightClick(){ val p=Path().apply{ moveTo(cx,cy) }; val g=GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(p,0,200)).build(); dispatchGesture(g,null,null) }
 override fun onAccessibilityEvent(e: android.view.accessibility.AccessibilityEvent?){}
 override fun onInterrupt(){}
}
