package com.example.blackjack.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blackjack.controllers.GameInstanceController
import com.example.blackjack.R
import com.example.blackjack.models.Game
import com.example.blackjack.views.activities.MainActivity
import com.example.blackjack.views.activities.PlayingRoom
import kotlinx.android.synthetic.main.frag_play.*
import androidx.fragment.app.FragmentManager


class Play : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bet_amount.text = (Game.currentGameController.getBet().toString() + "€")
        my_points.text = Game.currentGameController.getPoints().toString()

        btn_quit.setOnClickListener {
            var quitDialog = QuitFragment()
            quitDialog.show(parentFragmentManager, "quit")
        }
    }




}