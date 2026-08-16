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
    private var lastX = 0f; private var lastY = 0f
    private lateinit var audio: AudioManager; private lateinit var vib: Vibrator
    private val rainbow = listOf("#FF5252","#FFAB40","#FFEB3B","#66BB6A","#42A5F5","#AB47BC","#FF7043","#26C6DA").map { Color.parseColor(it) }

    override fun onCreate() {
        super.onCreate()
        audio = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onCreateInputView(): View {
        val root = layoutInflater.inflate(R.layout.keyboard_view, null) as LinearLayout
        val keysContainer = root.findViewById<LinearLayout>(R.id.keysContainer)
        val trackpadView = root.findViewById<View>(R.id.trackpadView)
        val trackpadContainer = root.findViewById<View>(R.id.trackpadContainer)

        // PC Keyboard كامل زي ما طلبت
        val rows = listOf(
            listOf("Esc","F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","Del"),
            listOf("~`","1!","2@","3#","4$","5%","6^","7&","8*","9(","0)","-_","=+","Back"),
            listOf("Tab","Q","W","E","R","T","Y","U","I","O","P","[{","]}","\\|"),
            listOf("Caps","A","S","D","F","G","H","J","K","L",";:","'\"","Enter"),
            listOf("Shift","Z","X","C","V","B","N","M",",<",".>","/?","Shift","▲"),
            listOf("Ctrl","Fn","Win","Alt","Space","Alt","Ctrl","◀","▼","▶")
        )

        var colorIdx = 0
        for (rowData in rows) {
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            for (key in rowData) {
                val btn = Button(this).apply {
                    text = key; textSize = 9f
                    setTextColor(rainbow[colorIdx % rainbow.size])
                    setBackgroundResource(R.drawable.key_carbon)
                    setOnClickListener {
                        audio.playSoundEffect(AudioManager.FX_KEY_CLICK, 1f)
                        try { vib.vibrate(15) } catch(_:Exception){}
                        // RGB Flash زي الفيديو
                        val flash = rainbow[Random.nextInt(rainbow.size)]
                        setBackgroundColor(flash)
                        Handler(Looper.getMainLooper()).postDelayed({ setBackgroundResource(R.drawable.key_carbon) }, 150)
                        handleKey(key)
                    }
                }
                colorIdx++
                val w = when(key){ "Space"->3.2f; "Back","Enter","Shift"->1.6f; else->1f }
                row.addView(btn, LinearLayout.LayoutParams(0, 92, w).apply { setMargins(2,2,2,2) })
            }
            keysContainer.addView(row)
        }

        // زرار التراك باد المدمج
        var trackVisible = true
        root.findViewById<View>(R.id.btnToggle).setOnClickListener {
            trackVisible = !trackVisible
            trackpadContainer.visibility = if(trackVisible) View.VISIBLE else View.GONE
        }
        root.findViewById<View>(R.id.btnSettings).setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        trackpadView.setOnTouchListener { _, e ->
            when(e.action){
                MotionEvent.ACTION_DOWN -> { lastX=e.x; lastY=e.y }
                MotionEvent.ACTION_MOVE -> {
                    MouseAccessibilityService.instance?.moveBy(e.x-lastX, e.y-lastY)
                    lastX=e.x; lastY=e.y
                }
            }
            true
        }
        root.findViewById<View>(R.id.btnLeft).setOnClickListener { MouseAccessibilityService.instance?.click() }
        root.findViewById<View>(R.id.btnRight).setOnClickListener { MouseAccessibilityService.instance?.click() }

        return root
    }

    private fun handleKey(k:String){
        val ic = currentInputConnection ?: return
        when(k){
            "Space"->ic.commitText(" ",1)
            "Back"->ic.deleteSurroundingText(1,0)
            "Enter"->ic.commitText("\n",1)
            "Tab"->ic.commitText("\t",1)
            "Esc","F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","Caps","Shift","Ctrl","Fn","Win","Alt","Del","▲","◀","▼","▶"-> {}
            else-> ic.commitText(if(k.length>1) k.last().toString().lowercase() else k.lowercase(),1)
        }
    }
}
