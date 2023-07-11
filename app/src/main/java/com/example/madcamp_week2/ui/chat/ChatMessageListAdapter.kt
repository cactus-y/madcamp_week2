package com.example.madcamp_week2.ui.chat

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.api.data.ChatMessage
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.data.User
import java.text.SimpleDateFormat
import java.util.Date

class ChatMessageListAdapter(private var list: MutableList<ChatMessage>, private var user: User): RecyclerView.Adapter<ChatMessageListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        private val context = itemView!!.context

        var tv_chat_msg_changed_date: TextView = itemView!!.findViewById(R.id.tv_chat_msg_changed_date)
        var iv_chat_msg_user_profile_image: ImageView = itemView!!.findViewById(R.id.iv_chat_msg_user_profile_image)
        var tv_chat_msg_content: TextView = itemView!!.findViewById(R.id.tv_chat_msg_content)
        var tv_chat_msg_other_timestamp: TextView = itemView!!.findViewById(R.id.tv_chat_msg_other_timestamp)
        var tv_chat_msg_my_timestamp: TextView = itemView!!.findViewById(R.id.tv_chat_msg_my_timestamp)
        var ll_chat_img_and_msg_container: LinearLayout = itemView!!.findViewById(R.id.ll_chat_img_and_msg_container)
        var view_put_msg_to_right:View = itemView!!.findViewById(R.id.view_put_msg_to_right)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: ChatMessage, position: Int) {
//            val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")
//            val date = LocalDateTime.parse(item.timestamp, formatter)

            // msg content
            tv_chat_msg_content.text = item.msg

            // stick to right or left
            if(item.senderName != user.nickname) {
                if(position == 0) {
                    iv_chat_msg_user_profile_image.visibility = View.VISIBLE
                } else {
                    if(list[position - 1].senderName == item.senderName) {
                        iv_chat_msg_user_profile_image.visibility = View.INVISIBLE
                    } else {
                        iv_chat_msg_user_profile_image.visibility = View.VISIBLE
                    }
                }
//                ll_chat_img_and_msg_container.gravity = Gravity.START
            } else {
//                ll_chat_img_and_msg_container.gravity = Gravity.END
                iv_chat_msg_user_profile_image.visibility = View.GONE
                view_put_msg_to_right.visibility = View.VISIBLE
            }

            // show new date
//            if(position == 0) {
//                tv_chat_msg_changed_date.text = "${date.year}년 ${date.month}월 ${date.dayOfMonth}일"
//            } else {
//                val tempDate = LocalDateTime.parse(list[position - 1].timestamp, formatter)
//                if(tempDate.dayOfMonth != date.dayOfMonth) {
//                    tv_chat_msg_changed_date.text = "${date.year}년 ${date.month}월 ${date.dayOfMonth}일"
//                }
//            }

            // timestamp

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_message_item_list, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun addItem(item: ChatMessage) {
        list.add(item)
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private fun toDate(currentMiliis: Long): String? {
        return SimpleDateFormat("hh:mm a").format(Date(currentMiliis))
    }
}