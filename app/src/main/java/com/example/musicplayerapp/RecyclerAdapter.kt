package com.example.musicplayerapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private var songs: List<Song>) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val performer: TextView = itemView.findViewById(R.id.tv_performer)
        val poster: ImageView = itemView.findViewById(R.id.iv_icon)

        init {
            itemView.setOnClickListener{v: View ->
                val position: Int = adapterPosition
                val intent = Intent(v.context, MusicActivity::class.java).apply {
                    putExtra("song", songs[position])
//                    putExtra("isAutoPlay", false)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = songs[position].title
        holder.performer.text = songs[position].performer
        holder.poster.setImageResource(songs[position].icon)
    }

}