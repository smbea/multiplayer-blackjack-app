package com.example.blackjack.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blackjack.controllers.GameInstanceController
import com.example.blackjack.R
import com.example.blackjack.views.activities.PlayingRoom
import kotlinx.android.synthetic.main.frag_play.*


class Play : Fragment() {

    private lateinit var gameInstanceController : GameInstanceController

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        this.gameInstanceController = (activity as PlayingRoom).gameInstanceController
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bet_amount.text = (gameInstanceController.getBet().toString() + "€")
        my_points.text = gameInstanceController.getBet().toString()
    }



}