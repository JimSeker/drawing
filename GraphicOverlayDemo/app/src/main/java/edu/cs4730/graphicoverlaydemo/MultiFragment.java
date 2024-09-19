package edu.cs4730.graphicoverlaydemo;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.cs4730.graphicoverlaydemo.databinding.FragmentMultiBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class MultiFragment extends Fragment {


    FragmentMultiBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMultiBinding.inflate(inflater, container, false);

        binding.textView5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                // Retrieve the new x and y touch positions

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        binding.textView5.setText(" Can't draw here!");
                        break;
                    case MotionEvent.ACTION_UP:
                        binding.textView5.setText("Can't draw here!");
                        v.performClick();
                        break;
                }
                return false;
            }
        });
        return binding.getRoot();
    }

}
