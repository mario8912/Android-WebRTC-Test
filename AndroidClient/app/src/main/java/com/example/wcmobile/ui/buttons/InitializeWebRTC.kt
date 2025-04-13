package com.example.wcmobile.ui.buttons

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wcmobile.classes.webrtc.WebRTC
import com.example.wcmobile.makeToast
import com.example.wcmobile.viewmodel.WebRTCViewModel

@Composable
fun WebRTCInitializationButton(webRTCViewModel: WebRTCViewModel, context: Context) {
  Button(
    modifier = Modifier.padding(4.dp),
    onClick = {
      webRTCViewModel.initializeWebRTC(context)
    }) {
    Text("Iniciar Cámara")
  }
}