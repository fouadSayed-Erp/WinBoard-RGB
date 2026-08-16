package com.fouad.winboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val seek = findViewById<SeekBar>(R.id.seekSensitivity)
        val txt = findViewById<TextView>(R.id.txtSensitivity)
        seek.progress = 60
        seek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(s: SeekBar?, p: Int, f: Boolean) {
                val v = 0.5 + p/100.0*2.0
                txt.text = String.format("%.1fx", v)
                MouseAccessibilityService.sensitivity = v.toFloat()
            }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) {}
        })
    }
    fun openInputSettings(v: android.view.View){ startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) }
    fun openAccessSettings(v: android.view.View){ startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
}
