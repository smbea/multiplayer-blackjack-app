package com.example.blackjack.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blackjack.R
import com.example.blackjack.fragments.Bet


class PlayingRoom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playroom)
        //setSupportActionBar(findViewById(R.id.toolbar))

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container_view, Bet())
        ft.commit()

    }
}