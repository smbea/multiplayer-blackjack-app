package com.example.blackjack.controllers

import androidx.lifecycle.MutableLiveData
import com.example.blackjack.models.Card
import com.example.blackjack.models.Game
import com.example.blackjack.models.GameInstance
import com.example.blackjack.views.activities.PlayingRoom
import org.json.JSONObject

class GameInstanceController(var model:GameInstance) {


    fun getBet(): Int {
        return model.bet
    }

    fun getPoints(): MutableLiveData<Int> {
        return model.myPoints
    }

    fun hit(){
        val msg = JSONObject("""{"type":"hit", "username":"${Game.myUsername}"}""")
        Game.communicationManager.sendMessage(msg)
    }


    fun updateHit(status: String, newCard: JSONObject, handValue: String) {
        if(status == "success"){
            val cardValue = newCard.opt("value") as String
            val cardType = newCard.opt("value") as String
            val cardHidden = (newCard.opt("value") as String) == "true"
            addCard(cardValue, cardType, cardHidden)
            model.turn.value = false
            model.myPoints.value = handValue.toInt()
        }
    }

    private fun addCard(value:String, type:String, hidden:Boolean){
        val tempCards =  model.myCards.value
        tempCards!!.add(Card(value, type, hidden))
        model.myCards.value=tempCards
    }

    fun stand(){
        val msg = JSONObject("""{"type":"stand", "username":"${Game.myUsername}"}""")
        Game.communicationManager.sendMessage(msg)
    }


    fun updateStand(status: String){
        if(status == "success")
            model.turn.value = false
    }


    fun fold() {
        val msg = JSONObject("""{"type":"fold", "username":"${Game.myUsername}"}""")
        Game.communicationManager.sendMessage(msg)
    }

    fun updateTurn() {
        model.turn.value = true
    }

    fun updateOpponent(newCard: JSONObject, handValue: String) {
        val tempCards = model.opponentCards.value!!
        val cardValue = newCard.opt("value") as String
        val cardType = newCard.opt("value") as String
        val cardHidden = (newCard.opt("value") as String) == "true"
        tempCards.add(Card(cardValue, cardType, cardHidden))
        model.opponentCards.value = tempCards
        model.opponentPoints.value = handValue.toInt()
    }

    fun dealCards(msg: JSONObject) {
        val myCards = msg.opt(Game.myUsername)
        val opCards = msg.opt(model.opponentUsername)

        //tbd
    }



}