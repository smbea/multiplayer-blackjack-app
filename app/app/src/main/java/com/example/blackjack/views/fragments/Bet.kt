package com.example.blackjack.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.blackjack.controllers.GameInstanceController
import com.example.blackjack.models.Game
import com.example.blackjack.R
import com.example.blackjack.models.GameInstance
import com.example.blackjack.views.activities.PlayingRoom
import kotlinx.android.synthetic.main.frag_bet.*

class Bet : Fragment() {

    var tempBetAmount = 0
    var tempAmountAvailable = Game.amountAvailable

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_bet, container, false)
    }

    private fun addAmount(value: Int): Boolean {
        return if (this.tempAmountAvailable - (this.tempBetAmount + value) > 0) {
            this.tempBetAmount += value
            val newBetAmount = this.tempBetAmount.toString() + "€"
            bet_amount.text = newBetAmount
            this.tempAmountAvailable -= value
            true
        } else false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.tempBetAmount = 0
        this.tempAmountAvailable = Game.amountAvailable
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_ready.setOnClickListener {
            Game.ready(this.tempBetAmount)
            findNavController().navigate(R.id.action_ready_to_play)
        }

        poker_chip_5.setOnClickListener {
            this.addAmount(5)
        }

        poker_chip_10.setOnClickListener {
            this.addAmount(10)
        }

        poker_chip_50.setOnClickListener {
            this.addAmount(50)
        }

        poker_chip_100.setOnClickListener {
            this.addAmount(100)
        }


    }
}