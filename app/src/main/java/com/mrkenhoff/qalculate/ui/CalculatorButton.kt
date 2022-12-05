package com.mrkenhoff.qalculate.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mrkenhoff.qalculate.R
import com.mrkenhoff.qalculate.databinding.CalculatorButtonBinding
import kotlin.math.sqrt

@SuppressLint("ClickableViewAccessibility")
class CalculatorButton : ConstraintLayout {
    var onCenterAction: () -> Unit = {}
    var onLongCenterAction: () -> Unit = {}
    var onTopLeftAction: () -> Unit = {}
    var onTopRightAction: () -> Unit = {}
    var onBottomRightAction: () -> Unit = {}
    var onBottomLeftAction: () -> Unit = {}

    var text : String
        get() = binding.centerButton.text.toString()
        set(value) { binding.centerButton.text = value }

    var topLeftText : String
        get() = binding.topLeftText.text.toString()
        set(value) { binding.topLeftText.text = value }

    var topRightText : String
        get() = binding.topRightText.text.toString()
        set(value) { binding.topRightText.text = value }

    var bottomRightText : String
        get() = binding.bottomRightText.text.toString()
        set(value) { binding.bottomRightText.text = value }

    var bottomLeftText : String
        get() = binding.bottomLeftText.text.toString()
        set(value) { binding.bottomLeftText.text = value }

    private var xStart = 0.0f
    private var yStart = 0.0f

    private var _binding: CalculatorButtonBinding? = null
    private val binding get() = _binding!!

    constructor(context: Context) : super(context) { }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { }

    init {
        _binding = CalculatorButtonBinding.inflate(LayoutInflater.from(context), this, true)

        binding.centerButton.setOnClickListener { onCenterAction() }
        binding.centerButton.setOnLongClickListener {
            onLongCenterAction()
            true
        }

        binding.centerButton.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    onTouchDown(motionEvent.x, motionEvent.y)
                }
                MotionEvent.ACTION_UP -> {
                    onTouchUp(motionEvent.x, motionEvent.y)
                }
            }
            return@OnTouchListener false
        })
    }

    private fun onTouchDown(x: Float, y: Float) {
        xStart = x
        yStart = y
    }

    private fun onTouchUp(x: Float, y: Float) {
        val dist = sqrt(x*x + y * y)
        if (dist > 8) { // TODO: Do not hardcode distance
            val right = (x - xStart) > 0
            val top = (y - yStart) < 0
            if (top && right) {
                onTopRightAction()
            } else if (!top && right) {
                onBottomRightAction()
            } else if (!top && !right) {
                onBottomLeftAction()
            } else if (top && !right) {
                onTopLeftAction()
            }
        }
    }
}