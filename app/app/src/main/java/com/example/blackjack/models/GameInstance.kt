package com.example.blackjack.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.blackjack.CommunicationManager
import kotlin.properties.Delegates

data class GameInstance(val bet: Int, val opponentUsername: String) {

    val finished: MutableLiveData<Boolean> = MutableLiveData(false)
    var finalBalance by Delegates.notNull<String>()
    var myPoints = 0


    val opponentPoints: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    var outcome: String = String()
    var opponentCards: ArrayList<Card> = ArrayList<Card>()
    var myCards: ArrayList<Card> = ArrayList<Card>()
    var turn by Delegates.notNull<Boolean>()
    var started = false
    val action: MutableLiveData<String> = MutableLiveData("")

    init {

    }

}