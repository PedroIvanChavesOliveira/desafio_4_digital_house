package com.example.desafio_4.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafio_4.databinding.RecyclerViewGamesBinding
import com.example.desafio_4.utils.Constants.AdapterFields.PHOTO
import com.example.desafio_4.utils.Constants.AdapterFields.RELEASE
import com.example.desafio_4.utils.Constants.AdapterFields.TITLE
import com.google.firebase.firestore.DocumentSnapshot

class GameAdapter(
        private var gameList: List<DocumentSnapshot>,
        private val onItemClick: (DocumentSnapshot) -> Unit
): RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewGamesBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(gameList[position], onItemClick)
    }

    class ViewHolder(private val binding: RecyclerViewGamesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(doc: DocumentSnapshot, onItemClick: (DocumentSnapshot) -> Unit) = with(binding) {
            binding.tvTitleGames.text = doc[TITLE].toString()
            binding.tvYearGames.text = doc[RELEASE].toString()
            Glide.with(itemView.context).load(doc[PHOTO]).into(binding.imageViewGamesRV)

            itemView.setOnClickListener {
                onItemClick(doc)
            }
        }
    }
}