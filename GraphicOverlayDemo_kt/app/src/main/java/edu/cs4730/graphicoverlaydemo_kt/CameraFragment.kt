package edu.cs4730.graphicoverlaydemo_kt

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import edu.cs4730.graphicoverlaydemo_kt.camera.Camera2Preview

/**
 * This fragment shows the camera and drawing overlay
 */
class CameraFragment : Fragment() {
    var mPreview: Camera2Preview? = null
    var preview: FrameLayout? = null
    var TAG = "CameraFragment"
    private val REQUIRED_PERMISSIONS = arrayOf("android.permission.CAMERA")
    var rpl: ActivityResultLauncher<Array<String>>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_camera, container, false)
        preview = myView.findViewById<View>(R.id.camera2_preview) as FrameLayout
        //this allows us to check in the fragment instead of doing it all in the activity.
        rpl = registerForActivityResult<Array<String>, Map<String, Boolean>>(
            ActivityResultContracts.RequestMultiplePermissions(),
            ActivityResultCallback<Map<String, Boolean>> { isGranted ->
                var granted = true
                isGranted.forEach{ entry ->
                    if (!entry.value) granted = false
                }
                if (granted) setup()
            }
        )
        if (!allPermissionsGranted()) rpl!!.launch(REQUIRED_PERMISSIONS) else setup()
        return myView
    }

    fun setup() {
        //we have to pass the camera id that we want to use to the surfaceview
        val manager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = manager.cameraIdList[0]
            mPreview = Camera2Preview(requireContext(), cameraId)
            preview!!.addView(mPreview)
        } catch (e: CameraAccessException) {
            Log.v(TAG, "Failed to get a camera ID!")
            e.printStackTrace()
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}