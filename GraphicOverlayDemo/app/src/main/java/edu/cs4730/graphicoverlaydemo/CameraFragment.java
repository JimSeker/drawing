package edu.cs4730.graphicoverlaydemo;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import edu.cs4730.graphicoverlaydemo.databinding.FragmentCameraBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.Map;


/**
 * This fragment shows the camera and drawing overlay
 */

public class CameraFragment extends Fragment {

    FragmentCameraBinding binding;

    private ImageCapture imageCapture;

    String TAG = "CameraFragment";
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
    ActivityResultLauncher<String[]> rpl;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false);


        //this allows us to check in the fragment instead of doing it all in the activity.
        rpl = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> isGranted) {
                    boolean granted = true;
                    for (Map.Entry<String, Boolean> x : isGranted.entrySet())
                        if (!x.getValue()) granted = false;
                    if (granted) startCamera();
                }
            }
        );
        if (!allPermissionsGranted())
            rpl.launch(REQUIRED_PERMISSIONS);
        else
            startCamera();
        return binding.getRoot();
    }


    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(
            new Runnable() {
                @Override
                public void run() {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        Preview preview = (new Preview.Builder()).build();
                        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

                        imageCapture = new ImageCapture.Builder()
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                            .build();

                        CameraSelector cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();

                        // Unbind use cases before rebinding
                        cameraProvider.unbindAll();

                        // Bind use cases to camera
                        cameraProvider.bindToLifecycle(
                            requireActivity(), cameraSelector, preview, imageCapture);


                    } catch (Exception e) {
                        Log.e(TAG, "Use case binding failed", e);
                    }
                }
            }, ContextCompat.getMainExecutor(requireContext())
        );
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


}
