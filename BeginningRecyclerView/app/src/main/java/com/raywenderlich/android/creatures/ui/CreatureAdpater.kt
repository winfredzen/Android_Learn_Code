package com.raywenderlich.android.creatures.ui

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.CompositeItem
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creature.view.*
import kotlinx.android.synthetic.main.list_item_planet_header.view.*
import java.lang.IllegalArgumentException

class CreatureAdpater(private val compositeItems: MutableList<CompositeItem>) : RecyclerView.Adapter<CreatureAdpater.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //创建ViewHolder，这是使用的是ViewGroup上的一个扩展方法
        /*
        fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
            return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
        }
         */
//        return ViewHolder(parent.inflate(R.layout.list_item_creature))

        return when(viewType) {

            ViewType.CREATURE.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature))
            ViewType.HEADER.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_planet_header))
            else -> throw IllegalArgumentException()

        }

    }

    override fun getItemCount() = compositeItems.size

    override fun getItemViewType(position: Int): Int {
        if (compositeItems[position].isHeader) {
            return ViewType.HEADER.ordinal
        } else {
            return ViewType.CREATURE.ordinal
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //在这里ViewHolder绑定数据
        holder.bind(compositeItems[position])
    }

    fun updateCreatures(creatures: List<CompositeItem>) {
        this.compositeItems.clear()
        this.compositeItems.addAll(creatures)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var creature: Creature

        init {
            //注册点击事件
            itemView.setOnClickListener(this)
        }

        //绑定数据
        fun bind(compositeItem: CompositeItem) {

            if (compositeItem.isHeader) {
                itemView.headerName.text = compositeItem.header.name
            } else {

                this.creature = compositeItem.creature
                val context = itemView.context
                //itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
                val resId = context.resources.getIdentifier(creature.uri, null, context.packageName)
                var drawable = context.getDrawable(resId)
                itemView.creatureImage.setImageDrawable(drawable)
                itemView.fullName.text = creature.fullName

                itemView.nickName.text = creature.nickname
                animationView(itemView)

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

    }

    enum class ViewType {
        HEADER, CREATURE
    }

}
