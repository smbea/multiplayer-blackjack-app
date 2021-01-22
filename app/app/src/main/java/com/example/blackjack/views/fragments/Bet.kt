package com.example.blackjack.views.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.blackjack.R
import com.example.blackjack.models.Game
import com.example.blackjack.models.GameInstance
import kotlinx.android.synthetic.main.frag_bet.*
import kotlinx.android.synthetic.main.frag_bet.balance
import kotlinx.android.synthetic.main.frag_bet.bet_amount
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.frag_bet.username
import kotlinx.coroutines.runBlocking

class Bet : Fragment() {

    var tempBetAmount = 0
    var tempAmountAvailable = Game.amountAvailable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

    private fun initObservers() {


        val gameInstance = Observer<GameInstance> { _ ->
            Log.i("i", "gameInstance")
            try {
                findNavController().navigateUp()
                findNavController().navigate(R.id.action_ready_to_play)
            }catch(e:Exception){
                Log.i("e", e.toString())
            }
            loader.visibility = View.INVISIBLE
        }

        Game.currentGame.observe(viewLifecycleOwner, gameInstance)
    }

    fun initListeners(){

        initObservers()


        btn_reset.setOnClickListener {
            this.tempBetAmount = 0
            bet_amount.text = "0€"
            this.tempAmountAvailable = Game.amountAvailable
        }

        poker_chip_5.setOnClickListener {
            this.addAmount(5)
        }

        poker_chip_10.setOnClickListener {
            this.addAmount(10)
        }

        poker_chip_50.setOnClickListener{
            this.addAmount(50)
        }

        poker_chip_100.setOnClickListener{
            this.addAmount(100)
        }

        btn_ready.setOnClickListener {
            bet_view.visibility = View.GONE
            loader.visibility = View.VISIBLE

            runBlocking {
                val job = GlobalScope.launch { // launch a new coroutine in background and continue

                    Game.ready(tempBetAmount)
                }
                job.join()
            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        balance.text = (Game.amountAvailable.toString() + "€")

        initListeners()
        initObservers()

        username.text=Game.myUsername

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("tempBetAmount", this.tempBetAmount)
        outState.putInt("tempAmountAvailable", this.tempAmountAvailable)
        outState.putInt("betViewVisibility", bet_view.visibility)
        outState.putInt("loaderViewVisibility", loader.visibility)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            bet_view.visibility = savedInstanceState.getInt("betViewVisibility")
            loader.visibility = savedInstanceState.getInt("loaderViewVisibility")
            this.tempBetAmount = savedInstanceState.getInt("tempBetAmount")
            this.tempAmountAvailable = savedInstanceState.getInt("tempAmountAvailable")
            bet_amount.text = (this.tempBetAmount.toString() + "€")
        }
    }

}