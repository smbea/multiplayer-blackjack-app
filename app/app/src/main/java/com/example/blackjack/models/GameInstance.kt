package com.example.blackjack.models

data class GameInstance( val bet:Int, val cards:ArrayList<String>) {
    var myPoints = 10
    var myCards: MutableList<String> = ArrayList()
    var opponentCards : MutableList<String> = ArrayList()


    init {
        myCards = cards
    }

}