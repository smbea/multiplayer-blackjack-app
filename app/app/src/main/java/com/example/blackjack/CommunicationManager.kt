package com.example.blackjack

import android.util.Log
import com.example.blackjack.models.Game
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketFrame
import org.json.JSONObject

class CommunicationManager {

    private val ws = WebSocketFactory().createSocket("ws://10.0.2.2:8080", 5000)

    fun connect() {
        // Create a WebSocket with a socket connection timeout value.
        WebSocketFactory().verifyHostname = false



        // Register a listener to receive WebSocket events.
        ws.addListener(object : WebSocketAdapter() {
            override fun onTextMessage(websocket: WebSocket?, text: String?) {
                super.onTextMessage(websocket, text)
                Log.i("i", text.toString())

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

    fun sendMessage(msg:JSONObject){
        ws.sendText(msg.toString())
    }

    fun parseMessage(message:String){

        Log.i("parseMessage", message)


        val msg = JSONObject(message)
        Log.i("parseMessage", msg.toString())

        when(msg.opt("type")){
            "res_hit"->{
                val status = msg.opt("status") as String
                val newCard = JSONObject(msg.opt("new_card") as String)
                val handValue = msg.opt("hand_value") as String
                Game.currentGameController.updateHit(status, newCard, handValue)
            }
            "res_fold"->{
                val status = msg.opt("status") as String
            }
            "res_bet"->{
                val status = msg.opt("status") as String
            }
            "res_stand"->{
                val status = msg.opt("status") as String
                Game.currentGameController.updateStand(status)
            }
            "res_join_room"->{
                val status = msg.opt("status") as String
                Game.responseStatus = true
                Game.response = msg
                Log.i("res_join_room", msg.toString())
            }
            "res_create_room"->{
                val status = msg.opt("status") as String
                Game.responseStatus = true
                Game.response = msg
                Log.i("res_create_room", "ready")
            }
            "your_turn"->{
                Game.currentGameController.updateTurn()
            }
            "update_op"->{
                val handValue = msg.opt("hand_value") as String
                val newCard = JSONObject(msg.opt("new_card") as String)
                Game.currentGameController.updateOpponent(newCard, handValue)
            }
            "game_start"->{
                val opponentUsername = msg.opt("opponent_username") as String
                Game.startGame(opponentUsername)
            }
            "deal_cards"->{
                Game.currentGameController.dealCards(msg)
            }

        }

    }
}