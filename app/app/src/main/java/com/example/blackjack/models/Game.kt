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
        val msg = JSONObject("""{"type":"join_room", "room_id":"$roomId"}""")
        communicationManager.sendMessage(msg)
        while (!this.responseStatus) {
        }

        if (response.opt("status") == "success") {
            gameID = response.opt("key") as String
            return "ok"
        } else return response.opt("error_message") as String
    }

    fun createRoom(): String {
        this.responseStatus = false
        val msg = JSONObject("""{"type":"create_room"}""")
        communicationManager.sendMessage(msg)
        while (!this.responseStatus) {
        }

        if (response.opt("status") == "success") {
            gameID = response.opt("key") as String
            return response.opt("room_id") as String
        } else return "error"
    }


    fun ready(bet: Int): Boolean {

        val msg = JSONObject("""{"type":"bet", "username":"$bet"}""")
        communicationManager.sendMessage(msg)
        tempBet = bet
        return true
    }


    fun login(token: String, username: String) {

        this.myUsername = username
        this.token = token
        val msg = JSONObject("""{"type":"new_player", "username":$username}""")
        communicationManager.sendMessage(msg)
    }

    fun startGame(opponentUsername: String) {
        currentGame.postValue(GameInstance(tempBet,opponentUsername))
        currentGameController = GameInstanceController(currentGame.value!!)
    }


}
