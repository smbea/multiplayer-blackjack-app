package com.example.blackjack.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blackjack.controllers.GameInstanceController
import com.example.blackjack.R
import com.example.blackjack.models.GameInstance


class PlayingRoom : AppCompatActivity() {
    public lateinit var gameInstance : GameInstance
    public lateinit var gameInstanceController: GameInstanceController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playroom)
        //setSupportActionBar(findViewById(R.id.toolbar))

    }
}