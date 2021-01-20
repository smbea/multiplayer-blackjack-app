package com.example.blackjack.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.blackjack.CommunicationManager
import com.example.blackjack.controllers.GameInstanceController
import kotlinx.coroutines.*
import kotlinx.coroutines.delay
import org.json.JSONObject


object Game {
    var amountAvailable = 500
    var token: String = String()
    var myUsername: String = String()
    val currentGame: MutableLiveData<GameInstance> by lazy {
        MutableLiveData<GameInstance>()
    }
    lateinit var currentGameController: GameInstanceController
    val communicationManager = CommunicationManager()
    var responseStatus = false
    var response = JSONObject()
    var gameID = String()
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
            JSONObject("""{"username":${Game.myUsername},"type":"join_room", "room_id":"$roomId"}""")
        communicationManager.sendMessage(msg)
        while (!this.responseStatus) {
        }

        Log.i("aqui", response.opt("status").toString() )

        if (response.opt("status")!!.toString() == "success") {
            gameID = response.opt("key")!!.toString()
            return "ok"
        } else return response.opt("error_message")!!.toString()
    }

    fun createRoom(): String {
        this.responseStatus = false
        val msg = JSONObject("""{"username":${myUsername},"type":"create_room"}""")
        communicationManager.sendMessage(msg)
        Log.i("oi", "before")

        while (!this.responseStatus) {
        }

        if (response.opt("status")!!.toString() == "success") {
            gameID = response.opt("key")!!.toString()
            roomId = response.opt("room_id")!!.toString().toInt()
            return "ok"
        } else return "error"
    }


    fun ready(bet: Int) {
        this.responseStatus = false
        val msg = JSONObject("""{"username":${myUsername},"type":"bet", "username":"$bet"}""")
        communicationManager.sendMessage(msg)
        tempBet = bet
    }



    fun login(token: String, username: String) {

        this.myUsername = username
        this.token = token
        val msg =
            JSONObject("""{"username":${myUsername},"type":"new_player", "username":$username}""")
        communicationManager.sendMessage(msg)
    }

    fun startGame(opponentUsername: String) {

        Log.i("game_start", "what")


        val game  =  GameInstance(tempBet, opponentUsername)
        Log.i("game_start", "after game")
        game.initGame()

        currentGame.postValue(game)
        Log.i("game_start", "post value")

        currentGameController = GameInstanceController(currentGame.value!!)
        responseStatus = true
    }


}
