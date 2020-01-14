package com.raywenderlich.android.creatures.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Constants
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.model.Favorites
import kotlinx.android.synthetic.main.list_item_creature_card_jupiter.view.*
import java.util.*


class CreatureCardAdpater(private val creatures: MutableList<Creature>) : RecyclerView.Adapter<CreatureCardAdpater.ViewHolder>(), ItemTouchHelperListener {

    var scrollDirection = ScrollDirection.DOWN
    var jupiterSpanSize = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.OTHER.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card))
            ViewType.MARS.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card_mars))
            ViewType.JUPITER.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card_jupiter))
            else -> throw IllegalArgumentException("Illegal value for viewType")
        }
    }

    override fun getItemCount() = creatures.size

    override fun getItemViewType(position: Int) = when(creatures[position].planet) {
        Constants.JUPITER -> ViewType.JUPITER.ordinal
        Constants.MARS -> ViewType.MARS.ordinal
        else -> ViewType.OTHER.ordinal
    }

    fun spanSizeAtPosition(position: Int): Int {
        return if (creatures[position].planet == Constants.JUPITER) {
            jupiterSpanSize
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //在这里ViewHolder绑定数据
        holder.bind(creatures[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var creature: Creature

        init {
            //注册点击事件
            itemView.setOnClickListener(this)
        }

        //绑定数据
        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            val imageResource = context.resources.getIdentifier(creature.uri, null, context.packageName)
            itemView.creatureImage.setImageResource(imageResource)
            itemView.fullName.text = creature.fullName

            setBackgroundColors(context, imageResource)

            animationView(itemView)
        }

        //实现点击事件方法
        override fun onClick(view: View) {
            val context = view.context
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }

        private fun setBackgroundColors(context: Context, imageResource: Int) {
            val image = BitmapFactory.decodeResource(context.resources, imageResource)
            Palette.from(image).generate { palette ->  
                val backgroundColor = palette.getDominantColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                itemView.creatureCard.setBackgroundColor(backgroundColor)
                itemView.nameHolder.setBackgroundColor(backgroundColor)
                val textColor = if (isColorDark(backgroundColor)) Color.WHITE else Color.BLACK
                itemView.fullName.setTextColor(textColor)
                if (itemView.slogan != null) {
                    itemView.slogan.setTextColor(textColor)
                }
            }
        }

        private fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
            return darkness >= 0.5
        }

        //动画
        private fun animationView(viewToaAnimation: View) {
            if (viewToaAnimation.animation == null) {
                val animId = if (scrollDirection == ScrollDirection.DOWN) R.anim.slide_from_bottom else R.anim.slide_from_top
                val animation = AnimationUtils.loadAnimation(viewToaAnimation.context, animId)
                viewToaAnimation.animation = animation
            }
        }

    }

    //拖动回调
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

        return true
    }

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {

    }

    //滚动的方向
    enum class ScrollDirection {
        UP, DOWN
    }

    enum class ViewType {
        JUPITER, MARS, OTHER
    }

}
