package com.mrkenhoff.qalculate.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.mrkenhoff.qalculate.databinding.FragmentMainBinding
import com.mrkenhoff.qalculate.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        viewModel.resultString.observe(viewLifecycleOwner) { newResult ->
            binding.resultTextView.text = newResult.toString()
        }
        childFragmentManager.beginTransaction()
            .add(R.id.button_panel, ButtonLayoutFragment.newInstance("layouts/main.xml"))
            .commit()

        EventBus.getDefault().register(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputText.setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_ENTER }
        binding.inputText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.setInput(s.toString())
            }
        })
        binding.resultTextView.setOnLongClickListener {
            val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(
                requireContext().getString(R.string.app_name), binding.resultTextView.text)
            clipboardManager.setPrimaryClip(clipData)
            Snackbar.make(requireView(), R.string.copied_to_clipboard, Snackbar.LENGTH_LONG).show()
            true
        }
    }

    @Subscribe
    fun type(event: ButtonEvent) {
        if (event.text == "@backspace@") {
            val end = binding.inputText.selectionEnd
            if (end > 0) {
                binding.inputText.text.replace(end - 1, end, "")
            }
        } else {
            binding.inputText.text.insert(binding.inputText.selectionStart, event.text)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        _binding = null
    }
}