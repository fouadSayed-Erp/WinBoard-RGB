package com.fouad.winboard
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import com.google.android.material.button.MaterialButton
class RGBMechanicalKey @JvmOverloads constructor(c: Context, a: AttributeSet?=null): MaterialButton(c,a){
 init { cornerRadius=18; setBackgroundColor(Color.parseColor("#2a2a2a")); setTextColor(Color.WHITE); textSize=14f }
 override fun onTouchEvent(e: MotionEvent): Boolean {
  if(e.action==MotionEvent.ACTION_DOWN){
   KeyboardSoundManager.instance?.play()
   setBackgroundColor(listOf(Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.GREEN, Color.rgb(59,130,246)).random())
   postDelayed({ setBackgroundColor(Color.parseColor("#2a2a2a")) },120)
   performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
  }
  return super.onTouchEvent(e)
 }
}
