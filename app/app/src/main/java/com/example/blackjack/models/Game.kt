package com.example.blackjack.models

import com.example.blackjack.controllers.GameInstanceController
import org.java_websocket.client.WebSocketClient

object Game {
    var amountAvailable = 500
    lateinit var currentGame : GameInstance
    lateinit var currentGameController : GameInstanceController

    fun newGame(bet: Int){
        currentGame = GameInstance(bet)
        currentGameController = GameInstanceController(currentGame)
    }

    fun quit(){
        amountAvailable = currentGame.bet
    }

}
