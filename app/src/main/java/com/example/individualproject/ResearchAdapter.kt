package com.example.individualproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.individualproject.R
import com.example.individualproject.Research


class ResearchAdapter(
    private val researchList: List<Research>,
    private val onItemClick: (Research) -> Unit
) : RecyclerView.Adapter<ResearchAdapter.ResearchViewHolder>() {

    class ResearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvResearchTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvResearchDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_research, parent, false)
        return ResearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResearchViewHolder, position: Int) {
        val research = researchList[position]
        holder.tvTitle.text = research.title
        holder.tvDescription.text = research.description

        holder.itemView.setOnClickListener {
            onItemClick(research)
        }
    }

    override fun getItemCount(): Int = researchList.size
}
