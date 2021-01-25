package com.example.desafio_4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio_4.databinding.RecyclerViewGamesBinding
import com.google.firebase.firestore.DocumentSnapshot

class GameAdapter(
    private val gameList: List<DocumentSnapshot>,
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
            binding.tvTitleGames.text = doc["title"].toString()
            binding.tvYearGames.text = doc["release"].toString()

            itemView.setOnClickListener {
                onItemClick(doc)
            }
        }
    }
}