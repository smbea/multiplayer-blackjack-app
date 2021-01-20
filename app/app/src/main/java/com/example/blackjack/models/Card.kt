package com.example.blackjack.models

import android.app.PendingIntent.getActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.blackjack.R


class Card (var value:String, type:String, hidden:Boolean) {

    var imageName:String

    init{
        if (hidden)
            imageName = "card_back"
        else imageName = "_$value$type"
    }

    fun getCardImageName(): String {
        return imageName
    }

}