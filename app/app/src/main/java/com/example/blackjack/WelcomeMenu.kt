package com.example.blackjack

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.welcome_menu.*
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WelcomeMenu : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        /*button_play.setOnClickListener {
            val intent = Intent(activity, SecondFragment::class.java)
            startActivity(intent)
        }*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.welcome_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view.findViewById<Button>(R.id.button_play).setOnClickListener {
            findNavController().navigate(R.id.action_WelcomeMenu_to_SecondFragment)
        }
    }
}