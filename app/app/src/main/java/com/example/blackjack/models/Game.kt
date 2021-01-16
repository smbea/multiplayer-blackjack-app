package com.example.blackjack.models

import com.example.blackjack.controllers.GameInstanceController

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

    fun joinRoom(roomId:Int): Boolean {
        //send message and wait
        return true
    }

    fun ready(bet:Int) {
        //send message  and wait
        this.newGame(bet)
    }

}
