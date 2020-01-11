package com.raywenderlich.android.creatures.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class FoodItemDecoration(color: Int, private var dividerWidthInPixels: Int) : RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
        paint.isAntiAlias = true
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {

            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            //top diver
            var left = parent.paddingLeft
            var right = parent.width - parent.paddingRight
            var top = child.top + params.topMargin
            var bottom = top + dividerWidthInPixels
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

            //right
            left = child.right - params.rightMargin
            right = left + dividerWidthInPixels
            top = child.top
            bottom = child.bottom
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

        }

    }
}