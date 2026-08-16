package com.fouad.winboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class WinBoardService : InputMethodService() {

    private var isTrackpadMode = false
    private var rootView: LinearLayout? = null

    override fun onCreateInputView(): View {
        rootView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        showKeyboard()
        return rootView!!
    }

    private fun showKeyboard() {
        rootView?.removeAllViews()

        val trackpadBtn = Button(this).apply {
            text = if (isTrackpadMode) "⌨️ رجوع للكيبورد" else "🔵 Trackpad"
            setBackgroundColor(0xFF0A84FF.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener {
                isTrackpadMode = !isTrackpadMode
                if (isTrackpadMode) showTrackpad() else showKeyboard()
            }
        }
        rootView?.addView(trackpadBtn)

        if (isTrackpadMode) return

        val rows = listOf(
            "qwertyuiop",
            "asdfghjkl",
            "zxcvbnm"
        )

        for (row in rows) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }
            for (c in row) {
                val b = Button(this).apply {
                    text = c.toString()
                    setOnClickListener {
                        currentInputConnection?.commitText(c.toString(), 1)
                    }
                }
                rowLayout.addView(b, LinearLayout.LayoutParams(0, 150, 1f))
            }
            rootView?.addView(rowLayout)
        }

        val bottomRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val space = Button(this).apply {
            text = "SPACE"
            setOnClickListener { currentInputConnection?.commitText(" ", 1) }
        }
        val del = Button(this).apply {
            text = "⌫"
            setOnClickListener {
                currentInputConnection?.deleteSurroundingText(1, 0)
            }
        }
        bottomRow.addView(space, LinearLayout.LayoutParams(0, 150, 3f))
        bottomRow.addView(del, LinearLayout.LayoutParams(0, 150, 1f))
        rootView?.addView(bottomRow)
    }

    private fun showTrackpad() {
        rootView?.removeAllViews()

        val trackpadBtn = Button(this).apply {
            text = "⌨️ رجوع للكيبورد"
            setBackgroundColor(0xFF0A84FF.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener {
                isTrackpadMode = false
                showKeyboard()
            }
        }
        rootView?.addView(trackpadBtn)

        val trackpadView = View(this).apply {
            setBackgroundColor(0xFF2C2C2E.toInt())
            setOnTouchListener { _, event ->
                // هنا هنحرك الماوس عن طريق MouseAccessibilityService بعدين
                MouseAccessibilityService.instance?.moveCursor(event.x, event.y)
                true
            }
        }
        rootView?.addView(trackpadView, LinearLayout.LayoutParams(-1, 500))
    }
}
