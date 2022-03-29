package com.mrkenhoff.qalculate.ui

import android.content.Context
import com.google.android.material.button.MaterialButton
import android.util.AttributeSet
import com.mrkenhoff.qalculate.R
import org.greenrobot.eventbus.EventBus

class CalculatorButton : MaterialButton {
    private var typedText: String? = null

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
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CalculatorButton, 0, 0)
            typedText = a.getString(R.styleable.CalculatorButton_typedText)
            a.recycle()
        }
        setOnClickListener {
            EventBus.getDefault().post(ButtonEvent(typedText ?: text.toString()))
        }
    }
}