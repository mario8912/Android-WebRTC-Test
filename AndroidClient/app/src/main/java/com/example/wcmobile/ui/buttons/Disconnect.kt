package com.example.wcmobile.ui.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wcmobile.viewmodel.WebRTCViewModel

@Composable
fun DisconnectButton(webRTCViewModel: WebRTCViewModel){
  Button(
    modifier = Modifier.padding(4.dp),
    onClick = {
      webRTCViewModel.releaseWebRTC()
    }) {
    Text("Desconectar")
  }
}