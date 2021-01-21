package com.example.blackjack.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.blackjack.CommunicationManager
import kotlin.properties.Delegates

data class GameInstance(val bet:Int,val opponentUsername: String) {

    var finalBalance by Delegates.notNull<String>()
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


        val tempCards = ArrayList<Card>()
        myCards.postValue(tempCards)
        opponentCards.postValue(tempCards)
        myPoints.postValue(0)

    }

}