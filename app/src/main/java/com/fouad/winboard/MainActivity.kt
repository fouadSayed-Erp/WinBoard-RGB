package com.fouad.winboard

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 100, 50, 100)
        }

        val title = TextView(this).apply {
            text = "WinBoard RGB V3\n\n1- فعل الكيبورد\n2- فعل مؤشر الماوس\n3- افتح واتساب وجرب الكتابة"
            textSize = 18f
        }

        val btnKeyboard = Button(this).apply {
            text = "1- تفعيل الكيبورد"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            }
        }

        val btnMouse = Button(this).apply {
            text = "2- تفعيل مؤشر الماوس"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        val btnChoose = Button(this).apply {
            text = "3- اختيار الكيبورد"
            setOnClickListener {
                (getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager)
                    .showInputMethodPicker()
            }
        }

        layout.addView(title)
        layout.addView(btnKeyboard)
        layout.addView(btnMouse)
        layout.addView(btnChoose)
        setContentView(layout)
    }
}
