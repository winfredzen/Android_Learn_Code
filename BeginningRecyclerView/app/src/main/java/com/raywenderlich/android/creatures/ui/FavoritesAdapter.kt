package com.raywenderlich.android.creatures.ui

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creature.view.*

class FavoritesAdapter(private val creatures: List<Creature>): RecyclerView.Adapter<CreatureAdpater.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatureAdpater.ViewHolder {

        return CreatureAdpater.ViewHolder(parent.inflate(R.layout.list_item_creature, false))

    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: CreatureAdpater.ViewHolder, position: Int) {

        holder.bind(creatures[position])

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(View.OnClickListener { view ->

                val context = view.context
                val intent = CreatureActivity.newIntent(context, creature.id)
                context.startActivity(intent)

            })
        }

        fun bind(creature: Creature) {

            val context = itemView.context
            itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))

            itemView.fullName.text = creature.fullName
            itemView.nickName.text = creature.nickname

        }


    }



}