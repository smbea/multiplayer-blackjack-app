package com.example.blackjack.views.fragments

import CardsAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blackjack.DeckDecorator
import com.example.blackjack.R
import com.example.blackjack.SensorsManager
import com.example.blackjack.models.Card
import com.example.blackjack.models.Game
import kotlinx.android.synthetic.main.frag_bet.*
import kotlinx.android.synthetic.main.frag_play.*
import kotlinx.android.synthetic.main.frag_play.balance
import kotlinx.android.synthetic.main.frag_play.bet_amount
import kotlinx.android.synthetic.main.frag_play.username


class Play : Fragment() {

    private lateinit var myCardsAdapter: CardsAdapter
    private lateinit var opponentCardsAdapter: CardsAdapter
    private lateinit var sensorManager: SensorsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        initObservers()

        sensorManager = SensorsManager(requireContext())

        return inflater.inflate(R.layout.frag_play, container, false)
    }


    private fun initObservers() {

        val actionObserver = Observer<String> { action ->

            try {
                Log.i("initObservers", "detected")

                //update cards
                myCardsAdapter = CardsAdapter(Game.currentGame.value!!.myCards, true)
                recycler_view_cards.adapter = myCardsAdapter
                recycler_view_cards.adapter!!.notifyDataSetChanged()


                //update cards
                opponentCardsAdapter = CardsAdapter(Game.currentGame.value!!.myCards, true)
                recycler_view_cards_opponent.adapter = opponentCardsAdapter
                recycler_view_cards_opponent.adapter!!.notifyDataSetChanged()


                //update turn
                val turn = Game.currentGame.value!!.turn
                Log.i("initObservers", turn.toString())

                btn_hit.isEnabled = turn
                btn_stand.isEnabled = turn

                if (Game.currentGame.value!!.started) {

                    if (turn)
                        opponent_turn.visibility = View.INVISIBLE
                    else
                        opponent_turn.visibility = View.VISIBLE
                }


                //update score
                val newScore = Game.currentGame.value!!.myPoints
                my_score.text = newScore.toString()
                if (newScore > 21) {
                    opponent_turn.text = "BUSTED"
                    opponent_turn.visibility = View.VISIBLE
                } else if (newScore == 21) {
                    opponent_turn.text = "BLACKJACK"
                    opponent_turn.visibility = View.VISIBLE
                }


            } catch (e: Exception) {
                Log.i("observer", e.toString())
            }
        }
        Game.currentGame.value!!.action.observe(viewLifecycleOwner, actionObserver)


        /*val pointsObserver = Observer<Int> { updatedPoints ->
            my_score.text = updatedPoints.toString()
            if(updatedPoints>21){
                opponent_turn.text = "BUSTED"
                opponent_turn.visibility = View.VISIBLE
            } else if(updatedPoints==21){
                opponent_turn.text = "BLACKJACK"
                opponent_turn.visibility = View.VISIBLE
            }
        }
        Game.currentGame.value!!.myPoints.observe(viewLifecycleOwner, pointsObserver)*/

        val endObserver = Observer<Boolean> { gameEnd ->
            Log.i("endObserver", gameEnd.toString())

            if (gameEnd) {
                findNavController().navigate(R.id.action_results)
            }
        }
        Game.currentGame.value!!.finished.observe(viewLifecycleOwner, endObserver)

        /*val myCardsObserver = Observer<ArrayList<Card>> { _ ->

            myCardsAdapter = CardsAdapter(Game.currentGame.value!!.myCards.value!!, true)
            recycler_view_cards.adapter = myCardsAdapter
            recycler_view_cards.adapter!!.notifyDataSetChanged()

            if (Game.currentGame.value!!.started) {
                btn_hit.isEnabled = true
                btn_stand.isEnabled = true
            }
        }

        Game.currentGame.value!!.myCards.observe(viewLifecycleOwner, myCardsObserver)*/

        /*val opponentCardsObserver = Observer<ArrayList<Card>> { _ ->
            opponentCardsAdapter =
                CardsAdapter(Game.currentGame.value!!.opponentCards.value!!, false)
            recycler_view_cards_opponent.adapter = opponentCardsAdapter
            recycler_view_cards_opponent.adapter!!.notifyDataSetChanged()
        }

        Game.currentGame.value!!.opponentCards.observe(viewLifecycleOwner, opponentCardsObserver)*/

        /*val turnObserver = Observer<Boolean> { turn ->

            btn_hit.isEnabled = turn
            btn_stand.isEnabled = turn

            if (Game.currentGame.value!!.started) {

            if (turn)
                opponent_turn.visibility = View.INVISIBLE
            else
                opponent_turn.visibility = View.VISIBLE
            }

        }

        Game.currentGame.value!!.turn.observe(viewLifecycleOwner, turnObserver)*/
    }


    private fun initCardsView() {
        val myRecyclerView = recycler_view_cards

        myCardsAdapter = CardsAdapter(Game.currentGame.value!!.myCards, true)
        myRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        myRecyclerView.itemAnimator = DefaultItemAnimator()
        myRecyclerView.addItemDecoration(DeckDecorator())
        myRecyclerView.adapter = myCardsAdapter
        myCardsAdapter.notifyDataSetChanged()

        val opRecyclerView = recycler_view_cards_opponent
        opponentCardsAdapter = CardsAdapter(Game.currentGame.value!!.opponentCards, false)
        opRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        opRecyclerView.itemAnimator = DefaultItemAnimator()
        opRecyclerView.addItemDecoration(DeckDecorator())
        opRecyclerView.adapter = opponentCardsAdapter
        opponentCardsAdapter.notifyDataSetChanged()
    }

    private fun initViewValues() {
        balance.text = (Game.amountAvailable.toString() + "€")
        bet_amount.text = (Game.currentGameController.getBet().toString() + "€")
        my_score.text = Game.currentGameController.getPoints().toString()
        username.text = Game.myUsername
        opponentUsername.text = Game.currentGame.value!!.opponentUsername
    }

    private fun setListeners() {

        btn_fold.setOnClickListener {
            Game.currentGameController.fold()
            findNavController().navigate(R.id.action_results)
        }

        btn_hit.setOnClickListener {
            Game.currentGameController.hit()
        }

        btn_stand.setOnClickListener {
            Game.currentGameController.stand()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCardsView()
        initViewValues()
        setListeners()


    }


}

