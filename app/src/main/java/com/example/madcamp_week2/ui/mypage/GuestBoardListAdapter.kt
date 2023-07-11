package com.example.madcamp_week2.ui.mypage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.data.guest.MyGuest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class GuestBoardListAdapter(private var list: MutableList<MyGuest>): RecyclerView.Adapter<GuestBoardListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        // info variables
        var tv_mypage_my_guest_karaoke_name: TextView = itemView!!.findViewById(R.id.tv_mypage_my_guest_karaoke_name)
        var tv_mypage_my_guest_karaoke_address: TextView = itemView!!.findViewById(R.id.tv_mypage_my_guest_karaoke_address)
        var tv_mypage_my_guest_karaoke_road_address: TextView = itemView!!.findViewById(R.id.tv_mypage_my_guest_karaoke_road_address)
        var tv_mypage_my_guest_karaoke_phone: TextView = itemView!!.findViewById(R.id.tv_mypage_my_guest_karaoke_phone)
        var tv_mypage_my_guest_deadline: TextView = itemView!!.findViewById(R.id.tv_mypage_my_guest_deadline)
        var tv_mypage_my_guest_author: TextView = itemView!!.findViewById(R.id.tv_mypage_my_guest_author)
        var tv_mypage_my_guest_comment: TextView = itemView!!.findViewById(R.id.tv_mypage_my_guest_comment)

        fun bind(item: MyGuest, position: Int) {
            val karaoke = item.karaoke
            tv_mypage_my_guest_karaoke_name.text = karaoke.name
            tv_mypage_my_guest_karaoke_address.text = karaoke.address
            tv_mypage_my_guest_karaoke_road_address.text = karaoke.roadAddress

            if(karaoke.phone == "") {
                tv_mypage_my_guest_karaoke_phone.text = "전화번호가 없어요!"
                tv_mypage_my_guest_karaoke_phone.setTextColor(Color.parseColor("#D3D3D3"))
            } else
                tv_mypage_my_guest_karaoke_phone.text = karaoke.phone

            tv_mypage_my_guest_deadline.text = item.deadLine
            tv_mypage_my_guest_author.text = item.author.nickname

            if(item.comment == "")
                tv_mypage_my_guest_comment.text = "안녕하세요."
            else
                tv_mypage_my_guest_comment.text = item.comment

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val currentDate = Calendar.getInstance().time

            val deadLineDate = dateFormat.parse(item.deadLine)

            // if current date is after the deadline
            // set text color as gray
            if(currentDate.after(deadLineDate)) {
                tv_mypage_my_guest_karaoke_name.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_guest_karaoke_address.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_guest_karaoke_road_address.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_guest_karaoke_phone.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_guest_deadline.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_guest_comment.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_guest_author.setTextColor(Color.parseColor("#808080"))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mypage_my_guest_item_list, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }
}