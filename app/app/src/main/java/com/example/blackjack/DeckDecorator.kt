package com.example.blackjack

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DeckDecorator : RecyclerView.ItemDecoration() {

    private val overlapValue = -200

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        var itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == 0) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(overlapValue, 0, 0, 0);
        }
    }
}