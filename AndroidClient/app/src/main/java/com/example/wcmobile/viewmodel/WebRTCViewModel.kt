package com.example.wcmobile.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcmobile.classes.webrtc.WebRTCState
import com.example.wcmobile.classes.webrtc.WebRTC
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.webrtc.VideoTrack

class WebRTCViewModel : ViewModel() {
  val webRTCState = mutableStateOf<WebRTCState>(WebRTCState.Idle)

  val videoTrack: StateFlow<VideoTrack?> = WebRTC.videoTrackFlow
  val beingCalled: StateFlow<Boolean> = WebRTC.beingCalled

  fun initializeWebRTC(context: Context) {
    webRTCState.value = WebRTCState.Initializing


    viewModelScope.launch {
      try {
        WebRTC.initialize(context) { error, stackTrace ->
          Log.e("WebRTC", "Error en inicialización: $error", Exception(stackTrace))
          webRTCState.value = WebRTCState.Error("Error: $error")
        }.let { success ->
          if (success) webRTCState.value = WebRTCState.Connected
          else webRTCState.value = WebRTCState.Error("Error al iniciar WebRTC")
        }


      } catch (ex: Exception) {
        Log.e("WebRTC", "Excepción en inicialización", ex)
        webRTCState.value = WebRTCState.Error("Exception: ${ex.message}")
      } finally {
        webRTCState.value = WebRTCState.Initialized
      }
    }
  }

  fun createWebRTCOffer() {
    viewModelScope.launch {
      try {
        WebRTC.createOffer()
      } catch (ex: Exception) {
        webRTCState.value = WebRTCState.Error("Excepción en createWebRTCOffer: ${ex.message}")
      }
    }
  }

  fun releaseWebRTC() {
    viewModelScope.launch {
      webRTCState.value = WebRTCState.Idle
      withContext(Dispatchers.IO) {
        WebRTC.releaseResources()
      }
    }
  }
}