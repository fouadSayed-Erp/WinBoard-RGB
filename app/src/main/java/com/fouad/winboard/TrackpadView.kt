package com.fouad.winboard
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
class TrackpadView(c: Context, a: AttributeSet?): View(c,a){
 var onMove: ((Float,Float)->Unit)?=null
 var onTap: (()->Unit)?=null
 var onRightTap: (()->Unit)?=null
 private var lx=0f; private var ly=0f; private var down=0L
 override fun onTouchEvent(e: MotionEvent): Boolean {
  when(e.actionMasked){
   MotionEvent.ACTION_DOWN->{lx=e.x; ly=e.y; down=System.currentTimeMillis()}
   MotionEvent.ACTION_MOVE->{ val dx=(e.x-lx)*1.8f; val dy=(e.y-ly)*1.8f; if(e.pointerCount==1) onMove?.invoke(dx,dy); lx=e.x; ly=e.y }
   MotionEvent.ACTION_UP->{ if(System.currentTimeMillis()-down<200){ if(e.pointerCount==1) onTap?.invoke() else onRightTap?.invoke() } }
  }
  return true
 }
}
