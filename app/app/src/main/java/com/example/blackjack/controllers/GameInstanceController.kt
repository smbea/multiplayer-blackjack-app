package com.example.blackjack.controllers

import android.os.SystemClock
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

    fun getPoints(): Int {
        return model.myPoints
    }

    fun hit(){
        val msg = JSONObject("""{"action":"hit", "username":"${Game.myUsername}", "key":"${Game.sessionKey}", "room_id":"${Game.roomId}"}""")
        Game.communicationManager.sendMessage(msg)
        updateTurn(false)
        model.action.postValue("hit")
    }


    fun updateHit(status: String, newCard: JSONObject, handValue: Int) {

        try {
            if (status == "success") {
                val cardValue = newCard.opt("value") as String
                val cardType = newCard.opt("suit") as String
                val cardHidden = !(newCard.opt("show") as Boolean)
                model.myPoints = handValue
                addCard(cardValue, cardType, cardHidden)
                model.action.postValue("updatehit")
            }
        }catch (e:Exception){
            Log.i("updateHit", e.toString())
        }
    }

    private fun addCard(value:String, type:String, hidden:Boolean){
        model.myCards.add(Card(value, type, hidden))
    }

    fun stand(){
        val msg = JSONObject("""{"action":"hold", "username":"${Game.myUsername}", "key":"${Game.sessionKey}", "room_id":"${Game.roomId}"}""")
        Game.communicationManager.sendMessage(msg)
        updateTurn(false)
        model.action.postValue("stand")
    }


    fun updateStand(status: String){
        if(status == "success") {

        }
    }

    fun fold() {
        val msg = JSONObject("""{"action":"fold", "username":"${Game.myUsername}", "key":"${Game.sessionKey}", "room_id":${Game.roomId}"}""")
        Game.communicationManager.sendMessage(msg)

        model.finalBalance = "-${Game.tempBet}€"
        model.outcome = "Folded"
    }

    fun updateTurn(value:Boolean) {
        Log.i("updateTurn", "updateTurn")
        model.turn=value
        Thread.sleep(300)
    }

    fun updateOpponent(newCard: JSONObject, handValue: Int) {
        try {
            Log.i("up","here")

            val tempCards = model.opponentCards
            val cardValue = newCard.opt("value") as String
            val cardType = newCard.opt("suit") as String
            val cardHidden = !(newCard.opt("show") as Boolean)
            tempCards.add(Card(cardValue, cardType, cardHidden))
            model.opponentCards = tempCards
            Log.i("up","here2")

            model.action.postValue("updateop")
            Log.i("up","here3")

        }catch (e:Exception){
            Log.i("up", e.toString())
        }
    }

    fun dealCard(msg: JSONObject) {

        try {
            val allCards = msg.opt("cards") as JSONObject

            val player = allCards.opt(Game.myUsername) as JSONObject
            val cardsObject = (player.opt("hand") as JSONObject).opt("cards") as JSONArray

            for (i in 0 until cardsObject.length()) {
                val cardObject = cardsObject[i] as JSONObject
                val value = cardObject.opt("value") as String
                val suit = cardObject.opt("suit") as String
                val hidden = !(cardObject.opt("show") as Boolean)
                model.myCards.add(Card(value, suit, hidden))
            }
            model.myPoints = player.opt("hand_value") as Int

            val opCards = ArrayList<Card>()
            opCards.add(Card("2", "hearts", true))
            opCards.add(Card("5", "hearts", false))

            model.opponentCards = opCards

            //opponent
            val opplayer = allCards.opt(Game.currentGame.value!!.opponentUsername) as JSONObject
            val opCardsObject = (opplayer.opt("hand") as JSONObject).opt("cards") as JSONArray

            for (i in 0 until opCardsObject.length()) {
                val cardObject = opCardsObject[i] as JSONObject
                val value = cardObject.opt("value") as String
                val suit = cardObject.opt("suit") as String
                val hidden = !(cardObject.opt("show") as Boolean)
                model.opponentCards.add(Card(value, suit, hidden))
            }

            model.action.postValue("deal")

        }catch (e:Exception){
            Log.i("dealCard", e.toString())

        }

        model.started = true
    }

    fun finish(outcome: String, balance: Int) {
        model.outcome = outcome
        val finalBalance = Game.amountAvailable - balance
        model.finalBalance = "${finalBalance}€"

        try {
            Thread.sleep(1500)

            model.finished.postValue(true)
        }catch (e:Exception){
            Log.i("finish", e.toString())
        }
    }


}