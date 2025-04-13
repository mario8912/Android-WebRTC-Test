package com.example.wcmobile

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.wcmobile.ui.theme.WCMobileTheme
import com.example.wcmobile.classes.permissions.PermissionManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.wcmobile.classes.webrtc.WebRTC
import com.example.wcmobile.ui.theme.MyYellow
import com.example.wcmobile.ui.theme.MyPastelRedDark
import android.Manifest
import android.widget.Button
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wcmobile.classes.webrtc.WebRTCState
import com.example.wcmobile.ui.buttons.ConnectToHubButton
import com.example.wcmobile.ui.buttons.CreateWebRTCOfferButton
import com.example.wcmobile.ui.buttons.WebRTCInitializationButton
import com.example.wcmobile.viewmodel.SignalRViewModel
import com.example.wcmobile.viewmodel.WebRTCViewModel
import com.example.wcmobile.ui.androidview.AndroidView
import com.example.wcmobile.ui.buttons.BeingCalled
import com.example.wcmobile.ui.toolbar.ToolBar

class MainActivity : ComponentActivity() {
  private val permissionManager = PermissionManager(this)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    permissionManager.requestPermissions(
      arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    ) { allGranted, deniedPermissions ->
      if (allGranted) {
        println("Todos los permisos fueron concedidos")
      } else {
        println("Permisos denegados: $deniedPermissions")
      }
    }

    setContent {
      WCMobileTheme {
        MyAppScaffold(this)
      }
    }
  }

  override fun onDestroy() {
      super.onDestroy()
      WebRTC.releaseResources()
  }
}

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppScaffold(
  context: Context,
  webRTCViewModel: WebRTCViewModel = viewModel(),
  signalRViewModel: SignalRViewModel = SignalRViewModel(webRTCViewModel)
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("WebCamarón", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
          containerColor = MyPastelRedDark,
          titleContentColor = MyYellow
        )
      )
    }
  ) { innerPadding ->
    val colModifier: Modifier = Modifier
      .fillMaxWidth()
      .height(100.dp)
      .padding(16.dp)

    val videoTrack by webRTCViewModel.videoTrack.collectAsState()
    val beingCalled by webRTCViewModel.beingCalled.collectAsState()

    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)) {


      when (val webRTCState = webRTCViewModel.webRTCState.value) {
        is WebRTCState.Idle -> {
          Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = colModifier.align(Alignment.BottomCenter)
          ) {
              WebRTCInitializationButton(webRTCViewModel, context)
          }
        }
        is WebRTCState.Initializing -> {}
        is WebRTCState.Initialized -> {
          AndroidView(videoTrack)

          Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = colModifier.align(Alignment.BottomCenter)) {
            ConnectToHubButton(signalRViewModel)
          }
        }
        is WebRTCState.Connected -> {
          AndroidView(videoTrack)

          ToolBar(webRTCViewModel, signalRViewModel, context)
          if(beingCalled) BeingCalled(webRTCViewModel)

          Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = colModifier.align(Alignment.BottomCenter)) {
            CreateWebRTCOfferButton(webRTCViewModel)
          }
        }
        is WebRTCState.Closed -> {
          webRTCViewModel.releaseWebRTC()
        }
        is WebRTCState.Error -> {
          Text("Error: ${webRTCState.message}")
          webRTCViewModel.releaseWebRTC()
        }
      }
    }
  }
}

fun makeToast(context: Context, message: String) {
  Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}


