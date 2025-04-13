package com.example.wcmobile.classes.webrtc

import android.util.Log
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

object SessionDescriptionActions {
  internal fun onCreateSDPObserver(
    type: String,
    onSdpReady: (SessionDescription) -> Unit
  ) : SdpObserver {
    return object : SdpObserver {
      override fun onCreateSuccess(sdp: SessionDescription) {
        Log.i("sdp", "${type}: SDP crada correctamente.")
        val modifiedSdp = sdp.description
          .replace("m=video 9 UDP/TLS/RTP/SAVPF 0", "m=video 9 UDP/TLS/RTP/SAVPF 96")
          .plus("a=rtpmap:96 VP8/90000\r\n")

        val updatedSessionDescription = SessionDescription(
          sdp.type,
          modifiedSdp
        )

        onSdpReady(updatedSessionDescription)
      }

      override fun onCreateFailure(error: String) {
        Log.wtf("sdp","${type}: error al crear el SDP: $error")
      }

      override fun onSetSuccess() {}
      override fun onSetFailure(error: String) {}
    }
  }

  internal fun onSetSDPObserver(
    type: String,
  ) : SdpObserver {
    return object : SdpObserver {
      override fun onCreateSuccess(sdp: SessionDescription) {}
      override fun onCreateFailure(error: String) {}

      override fun onSetSuccess() {
        Log.i("sdp", "Descripción $type establecida correctamente.")
      }

      override fun onSetFailure(error: String) {
        Log.wtf("sdp","Error al establecer la descripción $type: $error")
      }
    }
  }
}