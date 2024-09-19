package edu.cs4730.graphicoverlaydemo_kt

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.cs4730.graphicoverlaydemo_kt.databinding.FragmentMultiBinding

/**
 * A simple [Fragment] subclass.
 */
class MultiFragment : Fragment() {
    private lateinit var binding: FragmentMultiBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMultiBinding.inflate(inflater, container, false)

        binding.textView5.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> binding.textView5.text = " Can't draw here!"
                MotionEvent.ACTION_UP -> {
                    binding.textView5.text = "Can't draw here!"
                    v.performClick()
                }
            }
            false
        }
        return binding.root
    }
}