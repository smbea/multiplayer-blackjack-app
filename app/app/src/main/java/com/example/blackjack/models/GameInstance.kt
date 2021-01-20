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
    val myCards: MutableLiveData<ArrayList<Card>> = MutableLiveData(ArrayList())
    val turn: MutableLiveData<Boolean> = MutableLiveData()
    val started = false


    init{

    }

    fun initGame(){
        Log.i("game_start", "initing")


        Log.i("game_start", "turn")


        val tempCards = ArrayList<Card>()

        //myCards.postValue(tempCards)

        Log.i("game_start", "myCards")

        //opponentCards.postValue(tempCards)

        myPoints.postValue(0)

        Log.i("game_start", "after initing")
    }

}