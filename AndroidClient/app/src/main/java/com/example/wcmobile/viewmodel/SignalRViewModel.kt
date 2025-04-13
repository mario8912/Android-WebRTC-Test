package com.example.wcmobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wcmobile.classes.signalr.SignalRService
import com.example.wcmobile.classes.webrtc.WebRTCState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignalRViewModel(
  private val webRTCViewModel: WebRTCViewModel
) : ViewModel() {
  private val signalRService = SignalRService

  internal fun connect(onTriedToConect: (String) -> Unit) {
    viewModelScope.launch {
      try {
        signalRService.startConnection()
        onTriedToConect("Conectado Correctamnete")
        webRTCViewModel.webRTCState.value = WebRTCState.Connected
      } catch (ex: Exception) {
        Log.e("connect", "Error al conectarse al hub: ${ex.message}")
        onTriedToConect("Error al conectarse")
        webRTCViewModel.webRTCState.value = WebRTCState.Error("Error al conectarse al hub.")
      }
    }
  }

  private fun disconnect() {
    viewModelScope.launch {
      signalRService.stopConnection()
    }
  }

  internal fun reconect() {
    viewModelScope.launch {
      signalRService.reconnect()
    }
  }

  private fun sendMessage(message: String) {
    signalRService.sendMessage("ReceiveMessage", message)
  }

  override fun onCleared() {
    super.onCleared()
    disconnect()
  }
}