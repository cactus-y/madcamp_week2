package com.example.madcamp_week2.ui.chat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.sample.SampleChatMessage

class ChatMessageListAdapter(private var list: MutableList<SampleChatMessage>): RecyclerView.Adapter<ChatMessageListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        private val context = itemView!!.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}