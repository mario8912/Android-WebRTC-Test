package com.example.wcmobile.classes.webrtc


import android.util.Log
import org.webrtc.AddIceObserver

object ICECandidatesActions {
  internal fun onSetIceCandidate(): AddIceObserver {
    return object : AddIceObserver {
      override fun onAddSuccess() {
        Log.i("ice", "ICE añadido correctamente")
      }

      override fun onAddFailure(p0: String?) {
        Log.e("ice", "Error al añadir ice")
      }
    }
  }
}