package com.example.blackjack.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.blackjack.R
import com.example.blackjack.views.activities.PlayingRoom
import kotlinx.android.synthetic.main.frag_join_room_menu.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class JoinRoom : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_join_room_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn_join_room.setOnClickListener {
            val intent = Intent(activity, PlayingRoom::class.java)
            startActivity(intent)
        }

    }
}