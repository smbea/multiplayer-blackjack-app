package com.example.blackjack.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.blackjack.controllers.GameInstanceController
import com.example.blackjack.R
import com.example.blackjack.models.GameInstance


class PlayingRoom : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playroom)
        //setSupportActionBar(findViewById(R.id.toolbar))


    }
}