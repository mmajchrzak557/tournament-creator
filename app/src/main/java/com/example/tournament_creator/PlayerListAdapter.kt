package com.example.tournament_creator

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView


class PlayerListAdapter(var playerData: MutableList<String>) :
    RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {

    var itemIsEdited = BooleanArray(playerData.size)

    override fun getItemCount() = playerData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemIsEdited[position]) {
            holder.name.setText(playerData[position])
        } else {
            holder.name.text = null
            holder.name.hint = playerData[position]
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.player_list_item, parent, false)
        val holder = ViewHolder(view)

        holder.name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (holder.name.hasFocus()) {

                    playerData[holder.adapterPosition] = p0.toString()
                    itemIsEdited[holder.adapterPosition] = true

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        return holder
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: EditText = itemView.findViewById(R.id.tvName)

    }
}