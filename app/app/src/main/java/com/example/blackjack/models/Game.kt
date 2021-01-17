package com.example.blackjack.models

import com.example.blackjack.controllers.GameInstanceController

object Game {
    var amountAvailable = 500
    lateinit var currentGame : GameInstance
    lateinit var currentGameController : GameInstanceController

    private fun newGame(bet: Int, cards: ArrayList<Card>){
        currentGame = GameInstance(bet, cards)
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
        val cards = ArrayList<Card>()
        cards.add(Card("a", "spades", false))
        cards.add(Card("a", "spades", false))
        this.newGame(bet, cards)
    }

}
