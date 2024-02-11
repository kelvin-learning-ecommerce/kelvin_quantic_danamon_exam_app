package com.kelvinquantic.danamon.ui.mlkit.facedetection

import android.graphics.Rect
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.kelvinquantic.danamon.ui.mlkit.MlData
import com.kelvinquantic.danamon.ui.mlkit.MlListener
import com.kelvinquantic.danamon.ui.mlkit.camerax.BaseImageAnalyzer
import com.kelvinquantic.danamon.ui.mlkit.camerax.GraphicOverlay
import timber.log.Timber
import java.io.IOException

@ExperimentalGetImage
class FaceContourDetectionProcessor(
    private val view: GraphicOverlay,
    private val listener: MlListener
) :
    BaseImageAnalyzer<List<Face>>(), ImageAnalysis.Analyzer {

    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(realTimeOpts)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Timber.tag(TAG).e("Exception thrown while trying to close Face Detector: $e")
        }
    }

    override fun onSuccess(
        results: List<Face>,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    ) {
        graphicOverlay.clear()
        results.forEach { face ->
            val faceGraphic = FaceContourGraphic(graphicOverlay, face, rect)
            graphicOverlay.add(faceGraphic)

            val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
            val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

            // If classification was enabled:
            var smileProb = 0F
            if (face.smilingProbability != null) {
                face.smilingProbability?.let {
                    smileProb = it
                }
            }

            if (rotY > 30 || smileProb > 0.9)
                listener.getData(MlData(smileProb, rotY, rotZ))
        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Timber.tag(TAG).w("Face Detector failed.$e")
    }

    companion object {
        const val TAG = "FaceDetectorProcessor"
    }

}
