package com.example.blackjack.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.blackjack.CommunicationManager
import com.example.blackjack.controllers.GameInstanceController
import kotlinx.coroutines.*


object Game {
    var amountAvailable = 500
    val currentGame :  MutableLiveData<GameInstance> by lazy {
        MutableLiveData<GameInstance>()
    }
    lateinit var currentGameController : GameInstanceController
    private val communicationManager = CommunicationManager()

     fun establishCommunication(){
        Log.v("WSS", "connecting2")

        GlobalScope.launch {
            try {
                coroutineScope {
                    val task = async {
                        communicationManager.connect()
                    }
                }
            } catch (e: Throwable) {
                Log.e("Erro!", e.message.toString())
            }
        }
    }

    private fun newGame(bet: Int, cards: ArrayList<Card>){
        currentGame.postValue(GameInstance(bet, cards))
        currentGameController = GameInstanceController(currentGame.value!!)

    }

    fun quit(){
        amountAvailable = currentGame.value!!.bet
        //currentGame.value=null
    }

    fun joinRoom(roomId:Int): Boolean {
        //send message and wait
        return true
    }

    suspend fun ready(bet:Int): Boolean {

        delay(3000)

        //send message  and wait
        val cards = ArrayList<Card>()
        cards.add(Card("a", "spades", false))
        cards.add(Card("a", "spades", false))
        this.newGame(bet, cards)

        return true
    }


}
