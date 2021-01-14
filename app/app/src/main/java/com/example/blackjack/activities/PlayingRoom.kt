package com.example.blackjack.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.blackjack.R
import kotlinx.android.synthetic.main.activity_playroom.*

class PlayingRoom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playroom)
        //setSupportActionBar(findViewById(R.id.toolbar))

    }
}