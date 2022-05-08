package com.offline.continentalrecipesusingnavgraph.view.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.FragmentScannerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

/**
 * Refer document [https://beakutis.medium.com/using-googles-mlkit-and-camerax-for-lightweight-barcode-scanning-bb2038164cdc]
 */
class ScannerFragment : Fragment() {
    private lateinit var binding: FragmentScannerBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraX: ProcessCameraProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScannerBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "Scanner"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // permission callback
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    bindCameraUseCase()
                } else {
                    Snackbar.make(
                        view,
                        "Required camera permission to run QR scanner",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        if (isCameraPermissionAlreadyGranted()) {
            bindCameraUseCase()
        }
    }

    private fun bindCameraUseCase() {
        cameraX = ProcessCameraProvider.getInstance(binding.root.context).apply {
            addListener({
                val previewUseCase = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.qrPreview.surfaceProvider)
                }

                this.get().bindToLifecycle(
                    this@ScannerFragment,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    previewUseCase,
                    imageAnalysisUseCase()
                )
            }, ContextCompat.getMainExecutor(binding.root.context))
        }.get()
    }

    private fun imageAnalysisUseCase(): UseCase {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()

        val scanner = BarcodeScanning.getClient(options)
        val analysisImage = ImageAnalysis.Builder()
            .build().apply {
                this.setAnalyzer(
                    Executors.newSingleThreadExecutor()
                ) { imageProxy ->
                    processImageProxy(scanner, imageProxy)
                }
            }
        return analysisImage
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(
                image, imageProxy.imageInfo.rotationDegrees
            )
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodeList ->
                    barcodeList.getOrNull(0)?.rawValue?.let {
                        var uri = it
                        if (it.endsWith("\n")) {
                            uri = it.subSequence(0, it.length - 2) as String
                        }
                        findNavController().navigate(Uri.parse(uri))
                    }
                }
                .addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must
                    // call image.close() on received images when finished
                    // using them. Otherwise, new images may not be received
                    // or the camera may stall.
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(2000)
                        imageProxy.image?.close()
                        imageProxy.close()
                    }
                }

        }
    }

    private fun isCameraPermissionAlreadyGranted(): Boolean {
        val isGranted = false

        when {
            isPermissionGranted(Manifest.permission.CAMERA) -> {
                return true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                AlertDialog.Builder(binding.root.context)
                    .setMessage("Allow ${getString(R.string.app_name)}to access your camera")
                    .setPositiveButton("Allow") { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                    .setNegativeButton("Deny") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)

        }
        return isGranted
    }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(binding.root.context, permission) ==
                PackageManager.PERMISSION_GRANTED

    override fun onDestroyView() {
        super.onDestroyView()
        cameraX.unbindAll()
    }

}
