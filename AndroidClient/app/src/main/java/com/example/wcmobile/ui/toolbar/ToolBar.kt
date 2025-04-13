package com.example.wcmobile.ui.toolbar

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wcmobile.ui.buttons.DisconnectButton
import com.example.wcmobile.ui.buttons.ResetConnection
import com.example.wcmobile.viewmodel.SignalRViewModel
import com.example.wcmobile.viewmodel.WebRTCViewModel

@Composable
fun ToolBar(
  webRTCViewModel: WebRTCViewModel,
  signalRViewModel: SignalRViewModel,
  context: Context
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically
  ) {
    DisconnectButton(webRTCViewModel)
    ResetConnection(signalRViewModel, webRTCViewModel, context)
  }
}