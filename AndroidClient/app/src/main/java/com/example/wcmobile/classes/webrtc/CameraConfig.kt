package com.example.wcmobile.classes.webrtc

import android.content.Context
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.EglBase
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RendererCommon
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

class CameraConfig(
  context: Context,
  private val eglBaseContext: EglBase.Context,
  private val pFactory: PeerConnectionFactory
) {
  internal val surfacesVR = mapOf(
    "send" to createSurfaceViewRenderer(context, false),
    "receive" to createSurfaceViewRenderer(context, true)
  )
  private val surfaceTextureHelper = createSurfaceTextureHelper()
  private val videoCapturer = createCameraCapturer(context)
    ?: throw NullPointerException("VideoCapturer fue nulo.")

  private fun createSurfaceViewRenderer(context: Context, overlay: Boolean): SurfaceViewRenderer {
    val surfaceViewRenderer =  SurfaceViewRenderer(context).apply {
      init(eglBaseContext, null)
      setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
      setEnableHardwareScaler(true)
      setMirror(true)
      setZOrderOnTop(overlay)
    }

    return surfaceViewRenderer;
  }

  private fun createSurfaceTextureHelper(): SurfaceTextureHelper {
    return SurfaceTextureHelper.create("CaptureThread", eglBaseContext)
  }

  private fun createCameraCapturer(context: Context): CameraVideoCapturer? {
    return Camera2Enumerator(context).createCapturer("1", null)
  }

  internal fun innitVideoConfig(
    context: Context,
    pConnection: PeerConnection
  ) {
    val videoSource: VideoSource = createVideoSource()
    val videoTrack: VideoTrack = createVideoTrack(videoSource)

    try {
      val sendSVR = surfacesVR["send"]
        ?: throw IllegalStateException("SurfaceViewRenderer no inicializado.")

      videoTrack.addSink(sendSVR)
      pConnection.addTrack(videoTrack)

      videoCapturer.initialize(surfaceTextureHelper, context, videoSource.capturerObserver)
      videoCapturer.startCapture(1080, 720, 60)
    } catch (exception: Exception) {
      throw Exception("Error en video el video.", exception)
    }
  }

  internal fun dispose() {
    videoCapturer.stopCapture()
    surfaceTextureHelper.dispose()
    videoCapturer.dispose()
  }

  private fun createVideoSource(): VideoSource{
    try {
      return pFactory.createVideoSource(videoCapturer.isScreencast)
        ?: throw NullPointerException("Video source fue nulo.")
    } catch (exception: Exception) {
      throw Exception("Error en createVideosource.", exception)
    }
  }

  private fun createVideoTrack(videoSource: VideoSource): VideoTrack {
    try {
      return pFactory.createVideoTrack("1234", videoSource)
        ?: throw NullPointerException("VideoTrack fue nulo,")
    } catch (exception: Exception) {
      throw Exception("Error en createVideoTrack.", exception)
    }
  }
}