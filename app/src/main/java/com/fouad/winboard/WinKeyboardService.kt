package com.fouad.winboard
import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
class WinKeyboardService : InputMethodService() {
 private lateinit var keyboardContainer: LinearLayout
 private lateinit var trackpadContainer: LinearLayout
 override fun onCreate() { super.onCreate(); KeyboardSoundManager.init(applicationContext) }
 override fun onCreateInputView(): View {
  val root = layoutInflater.inflate(R.layout.keyboard_windows, null)
  keyboardContainer = root.findViewById(R.id.keyboard_container)
  trackpadContainer = root.findViewById(R.id.trackpad_container)
  val btnToggle = root.findViewById<MaterialButton>(R.id.btn_toggle_trackpad)
  val btnBack = root.findViewById<View>(R.id.btn_back_to_keyboard)
  val trackpad = root.findViewById<TrackpadView>(R.id.trackpad_view)
  btnToggle.setOnClickListener {
   KeyboardSoundManager.instance?.play()
   keyboardContainer.animate().alpha(0f).setDuration(150).withEndAction {
    keyboardContainer.visibility = View.GONE
    trackpadContainer.visibility = View.VISIBLE
    trackpadContainer.alpha = 0f
    trackpadContainer.animate().alpha(1f).setDuration(150).start()
    MousePointerService.instance?.showCursor()
   }.start()
  }
  btnBack.setOnClickListener {
   trackpadContainer.animate().alpha(0f).setDuration(150).withEndAction {
    trackpadContainer.visibility = View.GONE
    keyboardContainer.visibility = View.VISIBLE
    keyboardContainer.alpha = 0f
    keyboardContainer.animate().alpha(1f).setDuration(150).start()
    MousePointerService.instance?.hideCursor()
   }.start()
  }
  trackpad.onMove = { dx, dy -> MousePointerService.instance?.moveCursor(dx, dy) }
  trackpad.onTap = { MousePointerService.instance?.clickAtCurrentPos() }
  trackpad.onRightTap = { MousePointerService.instance?.rightClick() }
  setupAllKeys(root)
  return root
 }
 private fun setupAllKeys(root: View) {
  val allKeys = mutableListOf<RGBMechanicalKey>()
  findKeysRecursive(root, allKeys)
  for (key in allKeys) {
   key.setOnClickListener {
    val ic = currentInputConnection
    val txt = key.text.toString()
    when (txt) {
     "⌫" -> ic?.deleteSurroundingText(1, 0)
     "Space" -> ic?.commitText(" ", 1)
     "Enter" -> ic?.commitText("\n", 1)
     "Tab" -> ic?.commitText("\t", 1)
     "Trackpad" -> {}
     else -> if (txt.length <= 2) ic?.commitText(txt, 1)
    }
   }
  }
 }
 private fun findKeysRecursive(v: View, list: MutableList<RGBMechanicalKey>) {
  if (v is RGBMechanicalKey) list.add(v)
  else if (v is android.view.ViewGroup) { for (i in 0 until v.childCount) findKeysRecursive(v.getChildAt(i), list) }
 }
}
