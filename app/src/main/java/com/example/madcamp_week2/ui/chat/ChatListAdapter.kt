package com.example.madcamp_week2.ui.chat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.db.ChatRoom
import com.example.madcamp_week2.sample.SampleChatRoom
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

class ChatListAdapter(private var list: MutableList<ChatRoom>): RecyclerView.Adapter<ChatListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        private val context = itemView!!.context

        var iv_chat_user_profile_image: ImageView = itemView!!.findViewById(R.id.civ_chat_user_profile_image)
        var tv_chat_user_name: TextView = itemView!!.findViewById(R.id.tv_chat_user_name)
        var tv_chat_user_last_chat: TextView = itemView!!.findViewById(R.id.tv_chat_user_last_chat)

        fun bind(item: ChatRoom, position: Int) {
            tv_chat_user_name.text = item.otherUsername
            tv_chat_user_last_chat.text = item.latestMessage

            itemView.setOnClickListener {
                Intent(context, ChatRoomActivity::class.java).apply {
                    // putExtra
                    putExtra("otherId", item.otherId)
                    putExtra("otherUsername", item.otherUsername)
                    putExtra("roomNumber", item.roomNumber)
                    if (item.otherProfileImage != null) {
                        putExtra("otherProfileImage", item.otherProfileImage)
                    }
                }.run { context.startActivity(this) }
            }

            if (item.otherProfileImage != null && item.otherProfileImage != "") {
                Picasso.get().load(item.otherProfileImage).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(com.example.madcamp_week2.R.drawable.placeholder_image)
                    .into(iv_chat_user_profile_image)
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

    fun addChatRoom(item: ChatRoom) {
        list.add(item)
    }
    fun setChatRoomList(list: MutableList<ChatRoom>) {
        list.clear()
        list.addAll(list)
    }

    fun updateLatestMessage(roomNumber: String, message: String): Int {
        for (i: Int in list.indices) {
            if (list[i].roomNumber == roomNumber) {
                list[i].latestMessage = message
                return i
            }
        }
        return -1
    }
}