package com.example.wcmobile.classes.webrtc

import org.webrtc.PeerConnectionFactory
import android.content.Context
import android.util.Log
import org.json.JSONObject
import org.webrtc.*
import org.webrtc.PeerConnection.RTCConfiguration
import kotlin.reflect.KProperty0


class PeerConnectionManager(
    private val context: Context,
    private val eglBaseContext: EglBase.Context
) {

    internal fun getPCFactoryInitialized(): PeerConnectionFactory? {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .createInitializationOptions()
        )

        val peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(
                DefaultVideoEncoderFactory(
                    eglBaseContext,
                    true,
                    true
                )
            )
            .setVideoDecoderFactory(
                DefaultVideoDecoderFactory(eglBaseContext)
            )
            .createPeerConnectionFactory()

        return peerConnectionFactory
    }

    internal fun getPCInitialized(pcFactory: PeerConnectionFactory, observer: PeerConnection.Observer
        ): PeerConnection? {

        return pcFactory.createPeerConnection(
            configureRTC(),
            observer
        )
    }

    private fun configureRTC(): RTCConfiguration{
        return RTCConfiguration(
            listOf(
                PeerConnection.IceServer
                    .builder("stun:stun.l.google.com:19302")
                    .createIceServer()
            )
        ).apply {
            iceTransportsType = PeerConnection.IceTransportsType.ALL
        }
    }

}
