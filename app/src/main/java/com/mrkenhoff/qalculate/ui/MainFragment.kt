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
import androidx.fragment.app.activityViewModels
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
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

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
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

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item, viewModel.getAutoCompletionList())
        binding.inputText.setAdapter(adapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Find the activity's DrawerLayout
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout);

        val navController = NavHostFragment.findNavController(this);

        NavigationUI.setupWithNavController(toolbar, navController)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)



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
    fun type(event: BackspaceEvent) {
        val start = binding.inputText.selectionStart
        val end = binding.inputText.selectionEnd
        // TODO: Why is start and end always the same? Possibly an upstream bug?
        if (start == end && end > 0) {
            binding.inputText.text.replace(end - 1, end, "")
        } else {
            binding.inputText.text.replace(start, end, "")
        }
    }
    @Subscribe
    fun type(event: ClearEvent) {
        binding.inputText.setText("")
    }

    @Subscribe
    fun type(event: TypeEvent) {
        binding.inputText.text.insert(binding.inputText.selectionStart, event.textFront)
        val cursorPos = binding.inputText.selectionStart
        binding.inputText.text.insert(binding.inputText.selectionStart, event.textBack)
        binding.inputText.setSelection(cursorPos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        _binding = null
    }
}