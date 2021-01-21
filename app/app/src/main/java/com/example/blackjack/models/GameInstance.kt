package com.example.blackjack.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.blackjack.CommunicationManager
import kotlin.properties.Delegates

data class GameInstance(val bet:Int,val opponentUsername: String) {

    var finalBalance by Delegates.notNull<Int>()
    val myPoints: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val opponentPoints: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    var outcome: String = String()
    val opponentCards: MutableLiveData<ArrayList<Card>> = MutableLiveData(ArrayList())
    val myCards: MutableLiveData<ArrayList<Card>>  by lazy {
        MutableLiveData<ArrayList<Card>>()
    }
    val turn: MutableLiveData<Boolean> = MutableLiveData()
    var started = false

    init{

        //testing
        val tempCards = ArrayList<Card>()
        tempCards.add(Card("j", "spades", true))
        tempCards.add(Card("j", "spades", false))
        myCards.postValue(tempCards)
        //end testing

        //change for new score
        myPoints.postValue(10)

    }

    fun initGame(){

        myPoints.postValue(0)
    }

}