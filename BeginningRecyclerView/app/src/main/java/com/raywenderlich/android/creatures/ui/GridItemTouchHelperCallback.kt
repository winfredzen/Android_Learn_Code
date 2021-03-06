package com.raywenderlich.android.creatures.ui

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class GridItemTouchHelperCallback(private val listener: ItemTouchHelperListener): ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled() = true //启动长按拖动

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0)
    }

    //drag的时候，调用此方法
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return listener.onItemMove(recyclerView, viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

    }


}