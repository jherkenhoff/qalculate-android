package com.mrkenhoff.qalculate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrkenhoff.qalculate.databinding.FragmentMainBinding
import com.mrkenhoff.libqalculate.Calculator


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calcButton.setOnClickListener {

            var c = Calculator()

            binding.resultTextView.text = c.calculateAndPrint(binding.inputText.text.toString(), 2000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}