package edu.cs4730.flappyalien;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.cs4730.flappyalien.databinding.MainBinding;

/**
 * nothing to see here.  The layout.main has the myTextView in it.
 * see myTextureView for the code.
 */

public class MainActivity extends AppCompatActivity {
    MainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
