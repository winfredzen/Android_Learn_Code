/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.creatures.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.CreatureStore
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.android.synthetic.main.fragment_favorites.*


class AllFragment : Fragment() {

    //  private val adpater = CreatureAdpater(CreatureStore.getCreatures().toMutableList())
//  private val adpater = CreatureWithFoodAdpater(CreatureStore.getCreatures().toMutableList())
    private val adpater = CreatureCardAdpater(CreatureStore.getCreatures().toMutableList())

    private lateinit var layoutManager: GridLayoutManager

    private lateinit var itemTouchHelper: ItemTouchHelper

    private enum class GridState {
        LIST, GRID
    }

    private lateinit var listItemDecoration: RecyclerView.ItemDecoration
    private lateinit var gridItemDecoration: RecyclerView.ItemDecoration
    private lateinit var listMenuItem: MenuItem
    private lateinit var gridMenuItem: MenuItem
    private var gridState = GridState.GRID


    companion object {
        fun newInstance(): AllFragment {
            return AllFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_all, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        listMenuItem = menu.findItem(R.id.action_span_1)
        gridMenuItem = menu.findItem(R.id.action_span_2)

        when(gridState) {

            GridState.LIST -> {
                listMenuItem.isEnabled = false
                gridMenuItem.isEnabled = true
            }
            GridState.GRID -> {
                listMenuItem.isEnabled = true
                gridMenuItem.isEnabled = false
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.itemId
        when(id) {
            R.id.action_span_1 -> {
                gridState = GridState.LIST
                updateRecyclerView(1, listItemDecoration, gridItemDecoration)
                return true
            }
            R.id.action_span_2 -> {
                gridState = GridState.GRID
                updateRecyclerView(2, gridItemDecoration, listItemDecoration)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//    creatureRecyclerView.layoutManager = LinearLayoutManager(activity)


//    creatureRecyclerView.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
//    creatureRecyclerView.adapter = adpater

//    val layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
//    layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//      override fun getSpanSize(position: Int): Int {
//        return if((position + 1) % 3 == 0) 2 else 1
//      }
//
//    }
//    creatureRecyclerView.layoutManager = layoutManager
//    creatureRecyclerView.adapter = adpater

        setupRecyclerView()

        setupItemDecoration()

        //滚动监听
        setupScrollListener()

        //设置ItemTouchHelper
        setupItemTouchHelper()

    }

    private fun setupRecyclerView() {
        layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            //获取spansize
            override fun getSpanSize(position: Int): Int {
                return adpater.spanSizeAtPosition(position)
            }
        }
        creatureRecyclerView.layoutManager = layoutManager
        creatureRecyclerView.adapter = adpater
    }

    private fun setupItemDecoration() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.creature_card_grid_layout_margin)
        listItemDecoration = SpacingItemDecoration(1, spacingInPixels)
        gridItemDecoration = SpacingItemDecoration(2, spacingInPixels)

        creatureRecyclerView.addItemDecoration(gridItemDecoration)
    }

    private fun setupScrollListener() {
        creatureRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                Log.d("Scroll", dy.toString())

                adpater.scrollDirection = if (dy > 0) {
                    CreatureCardAdpater.ScrollDirection.DOWN
                } else {
                    CreatureCardAdpater.ScrollDirection.UP
                }
            }
        })
    }

    private fun updateRecyclerView(spanCount: Int, addItemDecoration: RecyclerView.ItemDecoration, removeItemDecoration: RecyclerView.ItemDecoration) {

        layoutManager.spanCount = spanCount
        adpater.jupiterSpanSize = spanCount
        creatureRecyclerView.removeItemDecoration(removeItemDecoration)
        creatureRecyclerView.addItemDecoration(addItemDecoration)

    }


    private fun showListView() {
        layoutManager.spanCount = 1
    }

    private fun showGridView() {
        layoutManager.spanCount = 2
    }

    private fun setupItemTouchHelper() {
        itemTouchHelper = ItemTouchHelper(GridItemTouchHelperCallback(adpater))
        itemTouchHelper.attachToRecyclerView(creatureRecyclerView)
    }

}