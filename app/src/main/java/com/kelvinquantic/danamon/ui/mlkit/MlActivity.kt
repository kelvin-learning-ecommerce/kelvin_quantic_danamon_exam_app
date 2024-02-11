package com.kelvinquantic.danamon.ui.mlkit

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.kelvinquantic.danamon.databinding.MlKitLayoutBinding
import com.kelvinquantic.danamon.ui.mlkit.camerax.CameraManager
import com.kelvinquantic.danamon.ui.mlkit.facedetection.FaceContourDetectionProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@ExperimentalGetImage
class MlActivity : AppCompatActivity() {
    private lateinit var cameraManager: CameraManager
    private lateinit var binding: MlKitLayoutBinding

    var timer: Int = 50

    var mlData = MlData(0F, 0F, 0F)

    var isSmile = false
    var nodLeft = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MlKitLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        createCameraManager()
        checkForPermission()
        onClicks()

        lifecycleScope.launch {
            timer()
        }
    }

    private fun checkForPermission() {
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun onClicks() {
        binding.btnSwitch.setOnClickListener {
            cameraManager.changeCameraSelector()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            binding.previewViewFinder,
            this,
            binding.graphicOverlayFinder,
            object : MlListener {
                override fun getData(data: MlData) {
                    mlData = data
                    Timber.tag(FaceContourDetectionProcessor.TAG).w("ML Data $data")

                }
            }
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun timer() {
        withContext(Dispatchers.Main) {
            while (timer > 1) {
                timer--
                delay(1000)

                if (!isSmile) {
                    if (mlData.isSmilingProb > 0.9) {
                        isSmile = true
                        binding.txtInstruction.text = "please node left"
                    }
                } else if (!nodLeft) {
                    if (mlData.rotY < -45 || mlData.rotY > 45) {
                        nodLeft = true
                        binding.txtInstruction.text = "please node down"
                    }
                }

                if (isSmile && nodLeft) {
                    Toast.makeText(
                        applicationContext,
                        "Liveness check is Success",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }

            Toast.makeText(
                applicationContext,
                "Liveness check is timeout, please try again",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }
}
