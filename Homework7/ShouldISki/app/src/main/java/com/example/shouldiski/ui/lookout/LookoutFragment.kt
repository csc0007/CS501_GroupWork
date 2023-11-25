package com.example.shouldiski.ui.lookout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shouldiski.databinding.FragmentLookoutBinding

class LookoutFragment : Fragment() {

    private var _binding: FragmentLookoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lookoutViewModel =
            ViewModelProvider(this).get(LookoutViewModel::class.java)

        _binding = FragmentLookoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLookout
        lookoutViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}