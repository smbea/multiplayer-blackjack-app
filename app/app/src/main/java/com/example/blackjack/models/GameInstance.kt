package com.example.blackjack.models

import androidx.lifecycle.MutableLiveData

data class GameInstance( val bet:Int, val cards:ArrayList<String>){
    val myPoints: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val opponentCards : MutableLiveData<ArrayList<String>> by lazy {MutableLiveData<ArrayList<String>>()}
    var myCards: ArrayList<String> = ArrayList()

    init {
        myCards = cards
        val cards:ArrayList<String> = ArrayList()
        cards.add("i")
        opponentCards.value=cards
        //change for new score
        myPoints.postValue(10)
    }

}