package com.raywenderlich.android.creatures.ui

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class ItemTouchHelperCallback(private val listener: ItemTouchHelperListener): ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = true //允许滑动删除

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    //drag的时候，调用此方法
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return listener.onItemMove(recyclerView, viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemDismiss(viewHolder, viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) { //选中，itemView改变颜色
            if (viewHolder is ItemSelectedListener) {
                viewHolder.onItemSelected()
            }
        }

        super.onSelectedChanged(viewHolder, actionState)

    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is ItemSelectedListener) { //不再选中，清除颜色
            viewHolder.onItemCleared()
        }
    }

}