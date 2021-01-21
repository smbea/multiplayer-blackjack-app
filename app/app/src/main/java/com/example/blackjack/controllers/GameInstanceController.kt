package com.example.blackjack.controllers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.blackjack.models.Card
import com.example.blackjack.models.Game
import com.example.blackjack.models.GameInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class GameInstanceController(var model:GameInstance) {


    fun getBet(): Int {
        return model.bet
    }

    fun getPoints(): MutableLiveData<Int> {
        return model.myPoints
    }

    fun hit(){
        val msg = JSONObject("""{"action":"hit", "username":"${Game.myUsername}", "key":"${Game.sessionKey}", "room_id":"${Game.roomId}"}""")
        Game.communicationManager.sendMessage(msg)
        updateTurn(false)
    }


    fun updateHit(status: String, newCard: JSONObject, handValue: String) {
        if(status == "success"){
            val cardValue = newCard.opt("value") as String
            val cardType = newCard.opt("value") as String
            val cardHidden = (newCard.opt("value") as String) == "true"
            addCard(cardValue, cardType, cardHidden)
            updateTurn(false)
            model.myPoints.value = handValue.toInt()
        }
    }

    private fun addCard(value:String, type:String, hidden:Boolean){
        val tempCards =  model.myCards.value
        tempCards!!.add(Card(value, type, hidden))
        model.myCards.value=tempCards
    }

    fun stand(){
        val msg = JSONObject("""{"action":"hold", "username":"${Game.myUsername}", "key":"${Game.sessionKey}", "room_id":"${Game.roomId}"}""")
        Game.communicationManager.sendMessage(msg)
    }


    fun updateStand(status: String){
        if(status == "success")
            updateTurn(false)
    }


    fun fold() {
        val msg = JSONObject("""{"action":"fold", "username":"${Game.myUsername}", "key":"${Game.sessionKey}", "room_id":${Game.roomId}"}""")
        Game.communicationManager.sendMessage(msg)

        model.finalBalance = "-${Game.tempBet}€"
        model.outcome = "Folded"
    }

    fun updateTurn(value:Boolean) {
        Log.i("updateTurn", "updateTurn")
        model.turn.postValue(value)
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

    fun dealCard(msg: JSONObject) {
        val player = (msg.opt("cards") as JSONObject).opt(Game.myUsername) as JSONObject
        val cardsObject = player.opt("cards") as JSONArray

        val cards = ArrayList<Card>()
        var tempScore = model.myPoints.value!!

        for (i in 0 until cardsObject.length()) {
            val cardObject = cardsObject[i] as JSONObject
            val value = cardObject.opt("value") as String
            val suit = cardObject.opt("suit") as String
            val hidden = !(cardObject.opt("show") as Boolean)
            cards.add(Card(value, suit, hidden))
            Log.i("in loop", cards.size.toString())
            tempScore += value.toInt()

            model.myCards.postValue(cards)
        }

       model.myPoints.postValue(tempScore)

       //temp

        val opCards = ArrayList<Card>()
        opCards.add(Card("2", "hearts", true))
        opCards.add(Card("5", "hearts", false))

        model.opponentCards.postValue(opCards)

        //

        /*val opponentPlayer = msg.opt(model.opponentUsername) as JSONObject

        val opCardsObject = player.opt("cards") as Array<JSONObject>
        val opCards = ArrayList<Card>()

        for(card in opCardsObject){
            val value = card.opt("value") as String
            val suit = card.opt("suit") as String
            val hidden = !(card.opt("show") as Boolean)
            opCards.add(Card(value, suit, hidden))
            model.opponentCards.postValue(cards)
        }
*/

        model.started = true
    }



}