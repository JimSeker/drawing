package edu.cs4730.graphicoverlaydemo_kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.cs4730.graphicoverlaydemo_kt.databinding.FragmentSimpleBinding

/**
 * A simple [Fragment] subclass.
 */
class SimpleFragment : Fragment() {
    private lateinit var binding: FragmentSimpleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSimpleBinding.inflate(inflater, container, false)
        return binding.root
    }
}