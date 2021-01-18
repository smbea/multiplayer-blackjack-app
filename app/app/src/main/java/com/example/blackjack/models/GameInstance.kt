package com.example.blackjack.models

import androidx.lifecycle.MutableLiveData

data class GameInstance( val bet:Int, val cards:ArrayList<Card>){

    val myPoints: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val opponentCards : MutableLiveData<ArrayList<Card>> by lazy {MutableLiveData<ArrayList<Card>>()}
    val myCards: MutableLiveData<ArrayList<Card>> by lazy {MutableLiveData<ArrayList<Card>>()}

    init {
        myCards.value = cards

        //testing
        val tempCards = ArrayList<Card>()
        tempCards.add(Card("j", "spades", true))
        tempCards.add(Card("j", "spades", false))
        opponentCards.value=tempCards
        //end testing

        //change for new score
        myPoints.postValue(10)
    }

}