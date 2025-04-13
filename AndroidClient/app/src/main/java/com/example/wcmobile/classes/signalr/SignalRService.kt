package com.example.wcmobile.classes.signalr

import android.util.Log
import com.example.wcmobile.classes.webrtc.WebRTC
import com.example.wcmobile.classes.webrtc.WebRTC.addIceCandidate
import com.example.wcmobile.classes.webrtc.WebRTC.receiveAnswer
import com.example.wcmobile.classes.webrtc.WebRTC.receiveOffer
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState

object SignalRService {
  private lateinit var hubConnection: HubConnection

  fun startConnection() {
    hubConnection = HubConnectionBuilder.create("http://192.168.1.201:5272/webrtchub").build()
    hubConnection.start().blockingAwait()

    hubConnection.on("ReceiveOffer", { sdp ->
      Log.e("oferta", "recibi oferta")
      Log.e("oferta", sdp)
      receiveOffer(sdp)
    }, String::class.java)

    hubConnection.on("ReceiveAnswer", { sdp ->
      Log.e("respuesta", "recibi respuesta")
      Log.e("respuesta", sdp)
      receiveAnswer(sdp)
    }, String::class.java)

    hubConnection.on("ReceiveIce", { ice ->
      addIceCandidate(ice)
    }, String::class.java)
  }

  internal fun stopConnection() {
    try {
      releaseResources()
    } catch (e: Exception) {
      Log.e("sdp", "Error al cerrar la conexión: ${e.message}")
    }
  }

  internal fun reconnect() {
    releaseResources()
    startConnection()
  }


  internal fun sendOffer(sdp: String) {
    hubConnection.send("SendOffer", sdp)
  }

  internal fun sendAnswer(sdp: String) {
    hubConnection.send("SendAnswer", sdp)
  }

  fun sendIceCandidate(ice: String) {
    hubConnection.send("SendIceCandidate", ice)
  }

  fun sendMessage(method: String, message: String) {
    hubConnection.send(method, message)
  }


  private fun releaseResources() {
    if (hubConnection.connectionState == HubConnectionState.CONNECTED) {
      hubConnection.stop().blockingAwait()
      hubConnection.close()
    }
  }


}