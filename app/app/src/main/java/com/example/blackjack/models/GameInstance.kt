package com.example.blackjack.models

import androidx.lifecycle.MutableLiveData
import kotlin.properties.Delegates

data class GameInstance( val bet:Int, val cards:ArrayList<Card>){

    var finalBalance by Delegates.notNull<Int>()
    val myPoints: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    var outcome:String = String()
    val opponentCards : MutableLiveData<ArrayList<Card>> by lazy {MutableLiveData<ArrayList<Card>>()}
    val myCards: MutableLiveData<ArrayList<Card>> by lazy {MutableLiveData<ArrayList<Card>>()}
    val turn: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    init {
        myCards.postValue(cards)

        //testing
        val tempCards = ArrayList<Card>()
        tempCards.add(Card("j", "spades", true))
        tempCards.add(Card("j", "spades", false))
        opponentCards.postValue(tempCards)
        //end testing

        //change for new score
        myPoints.postValue(10)
    }

}