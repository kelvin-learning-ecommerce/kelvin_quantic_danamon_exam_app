package com.kelvin.pastisystem.ui.mlkit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.face.Face
import com.kelvin.pastisystem.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceRecognitionViewModel @Inject constructor(
    private val movieRepositoryImpl: MovieRepository
) : ViewModel(){

//    private val _state = MutableStateFlow(FaceRecognitionState())
//    val mlState: StateFlow<FaceRecognitionState> get() = _state

    fun processFaces(faces: List<Face>){
        viewModelScope.launch {
            if (faces.isNotEmpty()) {
                for (element in faces){
                    val leftEyeOpenProbability = element.leftEyeOpenProbability
                    val rightEyeOpenProbability = element.rightEyeOpenProbability
                    val smilingProbability = element.smilingProbability

                    //Smiling Face
                    if((smilingProbability ?: 0f) > 0.3f) {
//                        _state.update {
//                            it.copy(
//                                isSmiling = true
//                            )
//                        }
                    }

                    //Eyes are open
                    if((leftEyeOpenProbability ?: 0F) > 0.9F && (rightEyeOpenProbability ?: 0F) > 0.9F
                    ){
//                        _state.update {
//                            it.copy(
//                                areEyesOpen = true
//                            )
//                        }
                    }

                    //Blinking face
                    if(((leftEyeOpenProbability ?: 0F) < 0.4 && (leftEyeOpenProbability != 0f)) && ((rightEyeOpenProbability ?: 0F) < 0.4F && (leftEyeOpenProbability != 0f))
                    ){
//                        _state.update {
//                            it.copy(
//                                isBlinking = true
//                            )
//                        }
                    }
                }
            }
        }
    }
}
