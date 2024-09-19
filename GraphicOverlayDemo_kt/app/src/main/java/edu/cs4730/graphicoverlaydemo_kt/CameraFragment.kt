package edu.cs4730.graphicoverlaydemo_kt

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import edu.cs4730.graphicoverlaydemo_kt.databinding.FragmentCameraBinding

/**
 * This fragment shows the camera and drawing overlay
 */
class CameraFragment : Fragment() {
    var TAG = "CameraFragment"

    private val REQUIRED_PERMISSIONS = arrayOf("android.permission.CAMERA")
    lateinit var binding: FragmentCameraBinding
    lateinit var rpl: ActivityResultLauncher<Array<String>>
    private lateinit var imageCapture: ImageCapture
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false)

        //this allows us to check in the fragment instead of doing it all in the activity.
        rpl = registerForActivityResult<Array<String>, Map<String, Boolean>>(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted ->
            var granted = true
            isGranted.forEach { entry ->
                if (!entry.value) granted = false
            }
            if (granted) startCamera()
        }
        if (!allPermissionsGranted())
            rpl.launch(REQUIRED_PERMISSIONS)
        else
            startCamera()
        return binding.root
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            {
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build()
                    preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

                    imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        requireActivity(), cameraSelector, preview, imageCapture
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Use case binding failed", e)
                }
            }, ContextCompat.getMainExecutor(requireContext())
        )
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