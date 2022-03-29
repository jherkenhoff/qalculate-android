package com.mrkenhoff.qalculate.ui

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import org.greenrobot.eventbus.EventBus

class CalculatorButton : MaterialButton {
    var typedText: String? = null

    constructor(context: Context) : super(context) {
        setup(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(attrs)
    }

    private fun setup(attrs: AttributeSet?) {
        setOnClickListener {
            EventBus.getDefault().post(ButtonEvent(typedText ?: text.toString()))
        }
    }
}