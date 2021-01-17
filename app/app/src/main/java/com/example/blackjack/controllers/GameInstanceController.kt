package com.example.blackjack.controllers

import androidx.lifecycle.MutableLiveData
import com.example.blackjack.models.Game
import com.example.blackjack.models.GameInstance
import com.example.blackjack.views.activities.PlayingRoom

class GameInstanceController(var model:GameInstance) {


    fun getBet(): Int {
        return model.bet
    }

    fun getPoints(): MutableLiveData<Int> {
        return model.myPoints
    }

    fun hit(){
        model.myCards.add("c")
    }

    fun stand(){
        val cards = model.opponentCards.value
        cards!!.add("i")
        model.opponentCards.value = cards
    }

}