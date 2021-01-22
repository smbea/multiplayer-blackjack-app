package com.example.blackjack.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blackjack.R
import com.example.blackjack.models.Game
import com.example.blackjack.views.activities.MainActivity
import kotlinx.android.synthetic.main.frag_results.*

class Results : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.frag_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lost_or_won.text = Game.currentGame.value!!.outcome
        value.text = Game.currentGame.value!!.finalBalance

        if(Game.currentGame.value!!.outcome == "win")
            confetti.visibility = View.VISIBLE

        back_to_menu.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("beginning", "false")
            startActivity(intent)
        }
    }
}