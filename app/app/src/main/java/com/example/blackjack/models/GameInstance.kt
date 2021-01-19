package com.example.blackjack.models

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
    val opponentCards: MutableLiveData<ArrayList<Card>> by lazy { MutableLiveData<ArrayList<Card>>() }
    val myCards: MutableLiveData<ArrayList<Card>> by lazy { MutableLiveData<ArrayList<Card>>() }
    val turn: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {
        turn.value = false

        val tempCards = ArrayList<Card>()
        myCards.postValue(tempCards)
        opponentCards.postValue(tempCards)

        myPoints.postValue(0)
    }

}