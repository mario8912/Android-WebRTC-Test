package com.example.wcmobile.ui.buttons


import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.wcmobile.viewmodel.SignalRViewModel

@Composable
fun ConnectToHubButton(signalRViewModel: SignalRViewModel){
  Button(
    onClick = {
      signalRViewModel.connect {
      }
    }) {
    Text("Conectarse al Hub")
  }
}