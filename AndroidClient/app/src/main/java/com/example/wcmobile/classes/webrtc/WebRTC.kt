package com.example.wcmobile.classes.webrtc

import org.webrtc.PeerConnectionFactory
import android.content.Context
import android.util.Log
import com.example.wcmobile.classes.signalr.SignalRService
import com.example.wcmobile.classes.webrtc.ICECandidatesActions.onSetIceCandidate
import com.example.wcmobile.classes.webrtc.SessionDescriptionActions.onCreateSDPObserver
import com.example.wcmobile.classes.webrtc.SessionDescriptionActions.onSetSDPObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import org.webrtc.*

object WebRTC{
  private val eglBaseContext = EglBase.create().eglBaseContext
  private lateinit var webRTCameraConfig: CameraConfig
  internal var surfacesVR = mapOf<String, SurfaceViewRenderer>()

  private lateinit var peerConnectionFactory: PeerConnectionFactory
  private lateinit var peerConnection: PeerConnection

  private val _videoTrackFlow = MutableStateFlow<VideoTrack?>(null)
  val videoTrackFlow: StateFlow<VideoTrack?> get() = _videoTrackFlow

  private val _onBeingCalled = MutableStateFlow(false)
  val beingCalled: StateFlow<Boolean> get() = _onBeingCalled

  fun initialize(context: Context, onError: (String, String) -> Unit ): Boolean {

    try {
      initializePeer(context)
      initializeCamera(context)

      return true
    } catch (ex: Exception) {
      onError(
        "Error WebRTC.initialize: ${ex.message}",
        ex.printStackTrace().toString(),
      )

      return false
    }
  }

  private fun initializePeer(context: Context) {
    try {
      val pConManager = PeerConnectionManager(context, eglBaseContext)
      peerConnectionFactory = pConManager.getPCFactoryInitialized()
        ?: throw NullPointerException("PeerConnectionFactory fue nulo.")
      peerConnection = pConManager.getPCInitialized(peerConnectionFactory, pcObserver)
        ?: throw NullPointerException("PeerConnection fue nulo.")
    } catch (exception: Exception) {
      throw Exception("Error en initializePeerConnection", exception)
    }
  }

  private fun initializeCamera(context: Context) {
    webRTCameraConfig = CameraConfig(context, eglBaseContext, peerConnectionFactory)
    webRTCameraConfig.innitVideoConfig(context, peerConnection)
    surfacesVR = webRTCameraConfig.surfacesVR
  }

  private val pcObserver: PeerConnection.Observer = object : PeerConnection.Observer {
    override fun onIceCandidate(candidate: IceCandidate) {
      SignalRService.sendIceCandidate(candidate.toString())
      Log.d("ice", "Creado")
      Log.d("ice", candidate.toString())
    }
    override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
      Log.i("ice", state.toString())
    }
    override fun onTrack(transceiver: RtpTransceiver) {
      Log.wtf("ice", "Nuevo flujo de medios añadido")
      val track = transceiver.receiver.track()

      if (track is VideoTrack) {
        _videoTrackFlow.value = track
      }
    }
    override fun onAddStream(stream: MediaStream) {}
    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
      Log.d("ice", p0.toString())
    }
    override fun onIceConnectionReceivingChange(p0: Boolean) {}
    override fun onSignalingChange(state: PeerConnection.SignalingState) {}
    override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>) {}
    override fun onRemoveStream(stream: MediaStream) {}
    override fun onDataChannel(channel: DataChannel) {}
    override fun onRenegotiationNeeded() {}
  }

  //PEER A
  internal fun createOffer() {
    peerConnection.createOffer(
      onCreateSDPObserver("OFERTA") {
        peerConnection.setLocalDescription(onSetSDPObserver("LOCAL"), it)
        SignalRService.sendOffer(it.description)
      },
      getMediaConstrains()
    )
  }

  internal fun receiveAnswer(sdp: String) {
    peerConnection.setRemoteDescription(
      onSetSDPObserver("REMOTA"),
      SessionDescription(SessionDescription.Type.ANSWER, sdp)
    )
  }

  //PEER B
  internal fun receiveOffer(sdp: String) {
    _onBeingCalled.value = true
    peerConnection.setRemoteDescription(
      onSetSDPObserver("REMOTA"),
      SessionDescription(SessionDescription.Type.OFFER, sdp)
    )

    createAnswer()
  }

  private fun createAnswer() {
    peerConnection.createAnswer(
      onCreateSDPObserver("RESPUESTA") {
        peerConnection.setLocalDescription(onSetSDPObserver("LOCAL"), it)
        SignalRService.sendAnswer(it.description)
      },
      getMediaConstrains()
    )
  }

  private fun getMediaConstrains(): MediaConstraints {
    try {
      return MediaConstraints().apply {
        mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
      }
    } catch (exception: Exception) {
      throw Exception("Error en addMediaConstraints.", exception)
    }
  }

  internal fun addIceCandidate(ice: String) {
    Log.d("ice", "recibido")
    Log.d("ice", ice)
    val candidate = parseIceCandidate(ice)
    peerConnection.addIceCandidate(candidate, onSetIceCandidate())
  }

  private fun parseIceCandidate(jsonString: String): IceCandidate {
    val json = JSONObject(jsonString)
    return IceCandidate(
      json.getString("sdpMid"),
      json.getInt("sdpMLineIndex"),
      json.getString("candidate")
    )
  }

  fun releaseResources() {
    webRTCameraConfig.dispose()
    peerConnectionFactory.dispose()
    peerConnection.dispose()
  }
}