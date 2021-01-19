package com.example.blackjack.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.blackjack.R
import com.example.blackjack.models.Game
import kotlinx.android.synthetic.main.frag_welcome_menu.*
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WelcomeMenu : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_welcome_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcome.text = "Welcome, ${Game.username}"

        btn_logout.setOnClickListener {
            findNavController().navigate(R.id.action_LogOut)
        }

        btn_join_room.setOnClickListener {
            findNavController().navigate(R.id.action_WelcomeMenu_to_JoinRoom)
        }

        btn_create_room.setOnClickListener {
            findNavController().navigate(R.id.action_WelcomeMenu_to_CreateRoom)
        }
    }
}