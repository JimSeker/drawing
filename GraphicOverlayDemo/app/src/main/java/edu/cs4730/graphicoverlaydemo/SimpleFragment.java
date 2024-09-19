package edu.cs4730.graphicoverlaydemo;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cs4730.graphicoverlaydemo.databinding.FragmentSimpleBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleFragment extends Fragment {
    FragmentSimpleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSimpleBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

}
