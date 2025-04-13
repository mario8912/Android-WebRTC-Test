package com.example.wcmobile.ui.buttons

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.wcmobile.viewmodel.SignalRViewModel
import com.example.wcmobile.viewmodel.WebRTCViewModel

@Composable
fun BeingCalled(webRTCViewModel: WebRTCViewModel){
  Button(
    onClick = {
      webRTCViewModel.createWebRTCOffer()
    }) {
    Text(":D")
  }
}