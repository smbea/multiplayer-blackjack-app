package com.example.blackjack.controllers

import com.example.blackjack.models.GameInstance
import com.example.blackjack.views.activities.PlayingRoom

class GameInstanceController(var model:GameInstance, var view:PlayingRoom) {

    fun getBet(): Number {
        return model.bet
    }
}