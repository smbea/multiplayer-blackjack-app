package com.example.blackjack.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.blackjack.CommunicationManager
import com.example.blackjack.controllers.GameInstanceController
import kotlinx.coroutines.*
import org.json.JSONObject


object Game {
    var amountAvailable = 500
    var myUsername: String = String()
    val currentGame: MutableLiveData<GameInstance> by lazy {
        MutableLiveData<GameInstance>()
    }
    lateinit var currentGameController: GameInstanceController
    val communicationManager = CommunicationManager()
    var responseStatus = false
    var response = JSONObject()
    var sessionKey = String()
    var tempBet = 0
    var roomId = 0

    fun establishCommunication() {
        Log.v("WSS", "connecting2")

        GlobalScope.launch {
            try {
                coroutineScope {
                    val task = async {
                        communicationManager.connect()
                    }
                }
            } catch (e: Throwable) {
                Log.e("Erro!", e.message.toString())
            }
        }
    }

    fun quit() {
        currentGame.value!!.finalBalance = 500
        currentGame.value!!.outcome = "folded"

    }


    fun joinRoom(roomId: Int): String {
        this.responseStatus = false
        val msg =
            JSONObject("""{"username":${myUsername},"action":"join_room", "room_id":"$roomId"}""")
        communicationManager.sendMessage(msg)
        while (!this.responseStatus) {
        }

        if (response.opt("status")!!.toString() == "success") {
            sessionKey = response.opt("key")!!.toString()
            return "ok"
        } else return response.opt("error_message")!!.toString()
    }

    fun createRoom(): String {
        this.responseStatus = false
        val msg = JSONObject("""{"username":${myUsername},"action":"create_room"}""")
        communicationManager.sendMessage(msg)

        while (!this.responseStatus) {
        }

        if (response.opt("status")!!.toString() == "success") {
            sessionKey = response.opt("key")!!.toString()
            roomId = response.opt("room_id")!!.toString().toInt()
            return "ok"
        } else return "error"
    }

    fun ready_up()
    {
        this.responseStatus = false
        val msg =
            JSONObject("""{"username":${myUsername},"action":"ready_up", "key":"$sessionKey", "room_id":$roomId}}""")
        communicationManager.sendMessage(msg)
    }
    fun ready(bet: Int) {
        this.responseStatus = false
        val msg =
            JSONObject("""{"username":${myUsername},"action":"bet", "key":"$sessionKey", "value":$bet,"room_id":$roomId}}""")
        communicationManager.sendMessage(msg)
        tempBet = bet
    }


    fun startGame(opponentUsername: String) {
        val game = GameInstance(tempBet, opponentUsername)
        game.initGame()

        currentGame.postValue(game)

        currentGameController = GameInstanceController(currentGame.value!!)
        responseStatus = true
    }


}
