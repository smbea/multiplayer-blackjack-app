package com.example.blackjack.views.fragments

import CardsAdapter
import android.os.Bundle
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
import kotlinx.android.synthetic.main.frag_play.*
import kotlinx.android.synthetic.main.frag_play.balance


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
        val pointsObserver = Observer<Int> { updatedPoints ->
            my_score.text = updatedPoints.toString()
        }
        Game.currentGame.value!!.myPoints.observe(viewLifecycleOwner, pointsObserver)

        val myCardsObserver = Observer<ArrayList<Card>> { _ ->
            recycler_view_cards.adapter!!.notifyDataSetChanged()
        }

        Game.currentGame.value!!.myCards.observe(viewLifecycleOwner, myCardsObserver)

        val opponentCardsObserver = Observer<ArrayList<Card>> { _ ->
            recycler_view_cards_opponent.adapter!!.notifyDataSetChanged()
        }

        Game.currentGame.value!!.opponentCards.observe(viewLifecycleOwner, opponentCardsObserver)

        val turnObserver = Observer<Boolean> { turn ->
            if (turn) {
                opponent_turn.visibility=View.INVISIBLE
                btn_hit.isEnabled = turn
                //btn_stand.isEnabled = true
            } else {
                opponent_turn.visibility=View.VISIBLE
                btn_hit.isEnabled = turn
                //btn_stand.isEnabled = false
            }
        }

        Game.currentGame.value!!.turn.observe(viewLifecycleOwner, turnObserver)
    }


    private fun initCardsView() {
        val myRecyclerView = recycler_view_cards
        myCardsAdapter = CardsAdapter(Game.currentGame.value!!.myCards.value!!, true)
        myRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        myRecyclerView.itemAnimator = DefaultItemAnimator()
        myRecyclerView.addItemDecoration(DeckDecorator())
        myRecyclerView.adapter = myCardsAdapter
        myCardsAdapter.notifyDataSetChanged()

        val opRecyclerView = recycler_view_cards_opponent
        opponentCardsAdapter = CardsAdapter(Game.currentGame.value!!.opponentCards.value!!, false)
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
    }

    private fun setListeners() {

        /*btn_quit.setOnClickListener {
            val quitDialog = QuitFragment()
            quitDialog.show(parentFragmentManager, "quit")
        }*/

        btn_fold.setOnClickListener {
            Game.end()
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

