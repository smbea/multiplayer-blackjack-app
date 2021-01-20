package com.example.blackjack.controllers

import androidx.lifecycle.MutableLiveData
import com.example.blackjack.models.Card
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
        // replace
        val tempCards =  model.myCards.value
        tempCards!!.add(Card("4", "diamonds", false))
        model.myCards.value=tempCards
        model.turn.value = false
    }

    fun stand(){
        val cards = model.opponentCards.value

        //testing only
        cards!!.add(Card("4", "diamonds",false))
        model.opponentCards.value = cards
        model.turn.value = true
    }

    fun getOutcome(): String {
        return model.outcome
    }

    fun getFinalBalance(): Int {
        return model.finalBalance
    }

}