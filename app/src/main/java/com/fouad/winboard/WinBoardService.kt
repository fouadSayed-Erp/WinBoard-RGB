package com.fouad.winboard

import android.content.Context
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlin.random.Random

class WinBoardService : InputMethodService() {
    private var lastX = 0f
    private var lastY = 0f
    private lateinit var audio: AudioManager
    private lateinit var vibrator: Vibrator

    override fun onCreate() {
        super.onCreate()
        audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreateInputView(): View {
        val root = layoutInflater.inflate(R.layout.keyboard_view, null) as LinearLayout
        val keysContainer = root.findViewById<LinearLayout>(R.id.keysContainer)
        val trackpadView = root.findViewById<View>(R.id.trackpadView)

        val rows = listOf(
            listOf("Esc","F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","Del"),
            listOf("~`","1!","2@","3#","4$","5%","6^","7&","8*","9(","0)","-_","=+","Backspace"),
            listOf("Tab","Q","W","E","R","T","Y","U","I","O","P","[{","]}","\\|"),
            listOf("Caps","A","S","D","F","G","H","J","K","L",";:","'\"","Enter"),
            listOf("Shift","Z","X","C","V","B","N","M",",<",".>","/?","Shift","▲"),
            listOf("Ctrl","Fn","Win","Alt","Space","Alt","Ctrl","◀","▼","▶")
        )

        for (rowData in rows) {
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            for (key in rowData) {
                val btn = Button(this).apply {
                    text = key
                    textSize = 10f
                    setTextColor(Color.WHITE)
                    setBackgroundResource(R.drawable.key_dark)
                    setOnClickListener {
                        playMechanical()
                        rgbFlash(this)
                        handleKey(key)
                    }
                }
                val w = if (key=="Space") 3f else if (key=="Backspace"||key=="Enter"||key=="Shift") 1.5f else 1f
                row.addView(btn, LinearLayout.LayoutParams(0, 105, w).apply { setMargins(2,2,2,2) })
            }
            keysContainer.addView(row)
        }

        trackpadView.setOnTouchListener { _, e ->
            when(e.action){
                MotionEvent.ACTION_DOWN -> { lastX=e.x; lastY=e.y }
                MotionEvent.ACTION_MOVE -> {
                    val dx = e.x-lastX; val dy = e.y-lastY
                    MouseAccessibilityService.instance?.moveBy(dx,dy)
                    lastX=e.x; lastY=e.y
                }
            }
            true
        }
        root.findViewById<View>(R.id.btnLeft).setOnClickListener { MouseAccessibilityService.instance?.click() }
        root.findViewById<View>(R.id.btnRight).setOnClickListener { MouseAccessibilityService.instance?.click() }

        return root
    }

    private fun handleKey(k: String){
        val ic = currentInputConnection ?: return
        when(k){
            "Space" -> ic.commitText(" ",1)
            "Backspace" -> ic.deleteSurroundingText(1,0)
            "Enter" -> ic.commitText("\n",1)
            "Tab" -> ic.commitText("\t",1)
            "Esc","F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","Caps","Shift","Ctrl","Fn","Win","Alt","Del","▲","◀","▼","▶" -> {}
            else -> {
                val txt = if (k.length>1) k.takeLast(1) else k
                ic.commitText(txt.lowercase(),1)
            }
        }
    }

    private fun playMechanical(){
        audio.playSoundEffect(AudioManager.FX_KEY_CLICK, 1.0f)
        try { vibrator.vibrate(20) } catch(e:Exception){}
    }

    private fun rgbFlash(btn: Button){
        val hue = Random.nextInt(0,360).toFloat()
        val col = Color.HSVToColor(floatArrayOf(hue,1f,1f))
        btn.setBackgroundColor(col)
        Handler(Looper.getMainLooper()).postDelayed({
            try { btn.setBackgroundResource(R.drawable.key_dark) } catch(e:Exception){}
        }, 180)
    }
}
