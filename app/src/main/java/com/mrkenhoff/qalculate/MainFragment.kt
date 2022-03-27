package com.mrkenhoff.qalculate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mrkenhoff.libqalculate.Calculator
import com.mrkenhoff.qalculate.databinding.FragmentMainBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.numPad.num1.setOnClickListener { type("1") }
        binding.numPad.num2.setOnClickListener { type("2") }
        binding.numPad.num3.setOnClickListener { type("3") }
        binding.numPad.num4.setOnClickListener { type("4") }
        binding.numPad.num5.setOnClickListener { type("5") }
        binding.numPad.num6.setOnClickListener { type("6") }
        binding.numPad.num7.setOnClickListener { type("7") }
        binding.numPad.num8.setOnClickListener { type("8") }
        binding.numPad.num9.setOnClickListener { type("9") }
        binding.numPad.num0.setOnClickListener { type("0") }
        binding.numPad.dot.setOnClickListener { type(".") }
        binding.numPad.e.setOnClickListener { type("E") }

        binding.operatorDivide.setOnClickListener { type("/") }
        binding.operatorMultiply.setOnClickListener { type("*") }
        binding.operatorMinus.setOnClickListener { type("-") }
        binding.operatorPlus.setOnClickListener { type("+") }

        binding.deleteButton.setOnClickListener {
            val end = binding.inputText.selectionEnd
            if (end > 0) {
                binding.inputText.text.replace(end - 1, end, "")
            }
        }
        binding.deleteButton.setOnLongClickListener {
            binding.inputText.setText("")
            true
        }
        binding.inputText.setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_ENTER }
        binding.inputText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                calculate()
            }
        })

    }

    private fun type(text: String) {
        binding.inputText.text.insert(binding.inputText.selectionStart, text)
    }

    private fun calculate() {
        val calc = Calculator()
        binding.resultTextView.text = calc.calculateAndPrint(binding.inputText.text.toString(), 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}