package edu.cs4730.graphicoverlaydemo_kt

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 */
class MultiFragment : Fragment() {
    private lateinit var tv5: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_multi, container, false)
        tv5 = myView.findViewById<View>(R.id.textView5) as TextView
        tv5.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> tv5.text = " Can't draw here!"
                MotionEvent.ACTION_UP -> {
                    tv5.text = "Can't draw here!"
                    v.performClick()
                }
            }
            false
        }
        return myView
    }
}