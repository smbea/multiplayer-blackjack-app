package com.example.blackjack.views.fragments

import CardsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blackjack.R
import com.example.blackjack.models.Game
import kotlinx.android.synthetic.main.frag_play.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blackjack.DeckDecorator


class Play : Fragment() {

    private lateinit var cardsAdapter: CardsAdapter
    var cardsDecorator = DeckDecorator()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.frag_play, container, false)
    }

    private fun initCardsView() {
        val recyclerView = recycler_view_cards
        cardsAdapter = CardsAdapter(Game.currentGame.myCards)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(cardsDecorator)
        recyclerView.adapter = cardsAdapter
        cardsAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCardsView()

        bet_amount.text = (Game.currentGameController.getBet().toString() + "€")
        my_points.text = Game.currentGameController.getPoints().toString()

        btn_quit.setOnClickListener {
            val quitDialog = QuitFragment()
            quitDialog.show(parentFragmentManager, "quit")
        }

        btn_hit.setOnClickListener {
            Game.currentGameController.hit()
            cardsAdapter.notifyDataSetChanged()
        }
    }




}