package com.example.blackjack

import android.util.Log
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketFrame

class CommunicationManager {

    val ws = WebSocketFactory().createSocket("ws://10.0.2.2:8080", 5000)

    fun connect() {
        // Create a WebSocket with a socket connection timeout value.
        WebSocketFactory().verifyHostname = false

        Log.v("WSS", "connecting")


        // Register a listener to receive WebSocket events.
        ws.addListener(object : WebSocketAdapter() {
            override fun onTextMessage(websocket: WebSocket?, text: String?) {
                super.onTextMessage(websocket, text)
                if (text != null) {
                    parseMessage(text)
                    Log.v("WSS", text)
                }
            }

            override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
                super.onCloseFrame(websocket, frame)
                Log.v("WSS", "closing socket")
            }
        })

        ws.connect()
    }

    fun sendAliveMessage(){
        //ws.sendText("hello !")
    }

    fun sendMessage(){
    }

    fun parseMessage(message:String){
    }
}