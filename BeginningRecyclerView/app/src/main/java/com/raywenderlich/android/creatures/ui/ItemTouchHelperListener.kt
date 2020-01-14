package com.raywenderlich.android.creatures.ui

import android.support.v7.widget.RecyclerView

interface ItemTouchHelperListener  {

    //移动
    fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean

    //删除
    fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int)

}