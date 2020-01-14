package com.raywenderlich.android.creatures.ui

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.CompositeItem
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.model.Favorites
import kotlinx.android.synthetic.main.list_item_creature.view.*
import kotlinx.android.synthetic.main.list_item_planet_header.view.*
import java.lang.IllegalArgumentException
import java.util.*

class CreatureAdpaterNoHeader(private val creatures: MutableList<Creature>, private val itemDragListener: ItemDragListener) :
        RecyclerView.Adapter<CreatureAdpaterNoHeader.ViewHolder>(), ItemTouchHelperListener { //ItemTouchHelperListener监听drag FavoritesFragment实现了ItemDragListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //创建ViewHolder，这是使用的是ViewGroup上的一个扩展方法
        /*
        fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
            return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
        }
         */
        return ViewHolder(parent.inflate(R.layout.list_item_creature))


    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //在这里ViewHolder绑定数据
        holder.bind(creatures[position])
    }

    fun updateCreatures(creatures: List<Creature>) {
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, ItemSelectedListener {//ItemSelectedListener监听选中 未选中

        private lateinit var creature: Creature

        init {
            //注册点击事件
            itemView.setOnClickListener(this)
        }

        //绑定数据
        fun bind(creature: Creature) {


            this.creature = creature
            val context = itemView.context
            //itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            val resId = context.resources.getIdentifier(creature.uri, null, context.packageName)
            var drawable = context.getDrawable(resId)
            itemView.creatureImage.setImageDrawable(drawable)
            itemView.fullName.text = creature.fullName

            itemView.nickName.text = creature.nickname
            animationView(itemView)


            //drag handle
            itemView.handle.setOnTouchListener{_, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemDragListener.onItemDrag(this) //开始drag了，通知FavoritesFragment的ItemTouchHelper
                }
                false
            }

        }

        //实现点击事件方法
        override fun onClick(view: View) {
            val context = view.context
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }

        private fun animationView(viewToAnimation: View) {
            if (viewToAnimation.animation == null) {
                val animation = AnimationUtils.loadAnimation(viewToAnimation.context, R.anim.scale_xy)
                viewToAnimation.animation = animation
            }
        }

        override fun onItemSelected() {
            itemView.listItemContainer.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.selectedItem))
        }

        override fun onItemCleared() {
            itemView.listItemContainer.setBackgroundColor(0)
        }

    }

    //移动
    override fun onItemMove(recyclerView: RecyclerView, fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(creatures, i, i + 1)
            }

        } else {
            for (i in fromPosition downTo  toPosition + 1) {
                Collections.swap(creatures, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)


        //保存调整后的顺序
        Favorites.saveFavorites(creatures.map { it.id }, recyclerView.context)

        return true
    }

    //swipe删除
    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
        Favorites.removeFavorite(creatures[position], viewHolder.itemView.context)
        creatures.removeAt(position)
        notifyItemRemoved(position)
    }

    enum class ViewType {
        HEADER, CREATURE
    }



}
