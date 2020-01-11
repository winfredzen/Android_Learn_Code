package com.raywenderlich.android.creatures.ui

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        outRect.top = spacing / 2
        outRect.bottom = spacing / 2
        outRect.left = spacing / 2
        outRect.right = spacing / 2

        //top
        if (position < spanCount) {
            outRect.top = spacing
        }

        //left
        if (position % spanCount == 0) {
            outRect.left = spacing
        }

        //right
        if ((position + 1) % spanCount == 0) {
            outRect.right = spacing
        }
    }

}