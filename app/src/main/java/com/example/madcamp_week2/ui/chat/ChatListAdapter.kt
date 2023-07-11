package com.example.madcamp_week2.ui.chat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.sample.SampleChatRoom

class ChatListAdapter(private var list: MutableList<SampleChatRoom>): RecyclerView.Adapter<ChatListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        private val context = itemView!!.context

        var iv_chat_user_profile_image: ImageView = itemView!!.findViewById(R.id.civ_chat_user_profile_image)
        var tv_chat_user_name: TextView = itemView!!.findViewById(R.id.tv_chat_user_name)
        var tv_chat_user_last_chat: TextView = itemView!!.findViewById(R.id.tv_chat_user_last_chat)

        fun bind(item: SampleChatRoom, position: Int) {
            tv_chat_user_name.text = item.otherUser.name
            tv_chat_user_last_chat.text = item.chatlog[item.chatlog.size - 1].msg

            itemView.setOnClickListener {
                Intent(context, ChatRoomActivity::class.java).apply {
                    // putExtra
                    putExtra("chatRoomIndex", position)
                }.run { context.startActivity(this) }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_list, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }
}