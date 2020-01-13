package com.raywenderlich.android.creatures.ui

import android.support.v7.widget.RecyclerView

interface ItemTouchHelperListener  {

    fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean

}