package com.example.wcmobile.classes.webrtc

sealed class WebRTCState {
  data object Idle : WebRTCState()
  data object Initializing : WebRTCState()
  data object Connected : WebRTCState()
  data object Initialized: WebRTCState()
  data object Closed: WebRTCState()
  data class Error(val message: String) : WebRTCState()
}