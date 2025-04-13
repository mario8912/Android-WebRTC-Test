package com.example.wcmobile.ui.androidview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.wcmobile.classes.webrtc.WebRTC
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoTrack

@Composable
fun AndroidView(track: VideoTrack?) {
  Column(Modifier.fillMaxSize()) {
    AndroidView(
      factory = {
        WebRTC.surfacesVR["receive"] as SurfaceViewRenderer
      },
      update = { renderer ->
        track?.addSink(renderer)
      },
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .background(Color.Black)
    )
    AndroidView(
      factory = {
        WebRTC.surfacesVR["send"] as SurfaceViewRenderer
      },
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
    )
  }
}
