package com.example.blackjack.views.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.blackjack.models.Game
import com.example.blackjack.views.activities.MainActivity

class QuitFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("If you quite you will lose the amount you bet. Do you want to continue?")
                .setPositiveButton(
                    "Yes"
                ) { _, _ ->


                    val intent = Intent(activity, MainActivity::class.java).apply {
                        putExtra("beginning", "false")
                    }
                    startActivity(intent)
                    //Game.quit()
                }
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}