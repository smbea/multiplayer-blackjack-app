package com.example.blackjack.models

data class GameInstance( val bet:Int) {
    var myPoints = 10
    var myCards = arrayOf<String>()
    var opponentCards = arrayOf<String>()
}