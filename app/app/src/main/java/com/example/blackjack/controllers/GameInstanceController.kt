package com.example.blackjack.controllers

import com.example.blackjack.models.GameInstance
import com.example.blackjack.views.activities.PlayingRoom

class GameInstanceController(var model:GameInstance) {

    fun getBet(): Int {
        return model.bet
    }

    fun getPoints():Int {
        return model.myPoints
    }

    fun stand(){

    }

    fun hit(){

    }
}