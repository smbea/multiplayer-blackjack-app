package com.example.blackjack.models

data class GameInstance( val bet:Number) {
    var myScore = 0
    var myCards = arrayOf<String>()
    var opponentCards = arrayOf<String>()
}