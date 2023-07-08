package com.example.madcamp_week2.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.sample.SampleKaraoke

class KaraokeHistoryListAdapter(private var list: MutableList<SampleKaraoke>): RecyclerView.Adapter<KaraokeHistoryListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        private val context = itemView!!.context

        var tv_mypage_history_karaoke_name: TextView = itemView!!.findViewById(R.id.tv_mypage_history_karaoke_name)
        var tv_mypage_history_karaoke_addr: TextView = itemView!!.findViewById(R.id.tv_mypage_history_karaoke_addr)
        var tv_mypage_history_karaoke_road_addr: TextView = itemView!!.findViewById(R.id.tv_mypage_history_karaoke_road_addr)
        var tv_mypage_history_karaoke_phone: TextView = itemView!!.findViewById(R.id.tv_mypage_history_karaoke_phone)

        fun bind(item: SampleKaraoke, position: Int) {
            tv_mypage_history_karaoke_name.text = item.name
            tv_mypage_history_karaoke_addr.text = item.address
            tv_mypage_history_karaoke_road_addr.text = item.roadAddress

            if(item.phoneNumber == "") {
                tv_mypage_history_karaoke_phone.text = "가게 번호 없음"
            } else {
                tv_mypage_history_karaoke_phone.text = item.phoneNumber
            }


//            itemView.setOnClickListener {  }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.karaoke_history_item_list, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }
}