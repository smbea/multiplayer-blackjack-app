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
import com.example.blackjack.views.activities.PlayingRoom
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.frag_create_room_menu.*
import java.lang.NumberFormatException

class CreateRoom:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_create_room_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        welcome.text = "MUDEI"
       playroom_id_field.setText(Game.roomId.toString())

        btn_join_room.setOnClickListener {
            try {
                val intent = Intent(activity, PlayingRoom::class.java)
                startActivity(intent)

            }catch (e: NumberFormatException) { }
        }

    }
}