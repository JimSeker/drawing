package edu.cs4730.graphicoverlaydemo_kt;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import edu.cs4730.graphicoverlaydemo_kt.camera.Camera2Preview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * This fragment shows the camera and drawing overlay
 */
public class CameraFragment extends Fragment {

    Camera2Preview mPreview;
    FrameLayout preview;

    String TAG = "CameraFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_camera, container, false);

        preview = (FrameLayout) myView.findViewById(R.id.camera2_preview);

        //we have to pass the camera id that we want to use to the surfaceview
        CameraManager manager = (CameraManager) requireActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];
            mPreview = new Camera2Preview(requireContext(), cameraId);
            preview.addView(mPreview);

        } catch (CameraAccessException e) {
            Log.v(TAG, "Failed to get a camera ID!");
            e.printStackTrace();
        }
        return myView;
    }

}
