package com.example.tournament_creator

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


class TournamentListAdapter : RecyclerView.Adapter<TournamentListAdapter.ViewHolder>() {

    var data = mutableListOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.btnDelete.setOnClickListener {
            val f =
                File(holder.itemView.context.filesDir.absolutePath.plus("/".plus(data[position])))
            if (f.exists()) {
                if (f.delete()) {
                    Toast.makeText(holder.itemView.context, "Tournament deleted", Toast.LENGTH_LONG).show()
                    data.removeAt(position)
                } else{
                    Toast.makeText(holder.itemView.context, "Couldn't delete", Toast.LENGTH_LONG).show()
                }
                this.notifyDataSetChanged()
            }
        }
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnLoad: Button = itemView.findViewById(R.id.btnLoad)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(item: String) {
            btnLoad.text = item
            btnLoad.setOnClickListener {
                val fis = itemView.context.openFileInput(item)
                val ois = InputStreamReader(fis)
                val bufferedReader = BufferedReader(ois)
                val json = bufferedReader.use(BufferedReader::readText)

                ois.close()
                fis.close()

                val intent = Intent(itemView.context, TournamentActivity::class.java)
                intent.putExtra("json", json)
                startActivity(itemView.context, intent, null)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.tournament_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }
}