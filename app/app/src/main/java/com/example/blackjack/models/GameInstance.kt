package com.example.blackjack.models

import androidx.lifecycle.MutableLiveData

data class GameInstance( val bet:Int, val cards:ArrayList<Card>){

    val myPoints: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val opponentCards : MutableLiveData<ArrayList<Card>> by lazy {MutableLiveData<ArrayList<Card>>()}
    var myCards: ArrayList<Card> = ArrayList()

    init {
        myCards = cards

        //testing
        val cards = ArrayList<Card>()
        cards.add(Card("j", "spades", true))
        cards.add(Card("j", "spades", false))
        opponentCards.value=cards
        //end testing

        //change for new score
        myPoints.postValue(10)
    }

}