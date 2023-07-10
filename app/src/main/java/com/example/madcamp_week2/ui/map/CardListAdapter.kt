package com.example.madcamp_week2.ui.map

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.sample.KaraokeOrPost
import com.example.madcamp_week2.sample.genres
import com.example.madcamp_week2.sample.user1

class CardListAdapter(private var list: MutableList<KaraokeOrPost>): RecyclerView.Adapter<CardListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        private val context = itemView!!.context

        // karaoke info cardview
        var ll_map_karaoke_info_container: LinearLayout = itemView!!.findViewById(R.id.ll_map_karaoke_info_container)
        var tv_map_karaoke_name: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_name)
        var tv_map_karaoke_addr: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_addr)
        var tv_map_karaoke_road_addr: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_road_addr)
        var tv_map_karaoke_phone: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_phone)
        var ll_map_new_post_button_container: LinearLayout = itemView!!.findViewById(R.id.ll_map_new_post_button_container)
        var tv_map_new_post_button: TextView = itemView!!.findViewById(R.id.tv_map_new_post_button)

        // post info cardview
        var ll_map_post_info_container: LinearLayout = itemView!!.findViewById(R.id.ll_map_post_info_container)
        var tv_map_post_username: TextView = itemView!!.findViewById(R.id.tv_map_post_username)
        var tv_map_post_user_comment: TextView = itemView!!.findViewById(R.id.tv_map_post_user_comment)
        var ll_map_post_user_genre_container1: LinearLayout = itemView!!.findViewById(R.id.ll_map_post_user_genre_container1)
        var ll_map_post_user_genre_container2: LinearLayout = itemView!!.findViewById(R.id.ll_map_post_user_genre_container2)
        var ll_map_chat_and_join_button_container: LinearLayout = itemView!!.findViewById(R.id.ll_map_chat_and_join_button_container)
        var tv_map_post_chat_button: TextView = itemView!!.findViewById(R.id.tv_map_post_chat_button)
        var tv_map_post_join_button: TextView = itemView!!.findViewById(R.id.tv_map_post_join_button)


        fun bind(item: KaraokeOrPost, position: Int) {
            println("\n\n\nAdapter position: ${position}\n\n\n")
            if(item.post == null) {
                // karaoke info
                val karaoke = item.karaoke

                // visibility setting
                ll_map_post_info_container.visibility = View.GONE
                ll_map_karaoke_info_container.visibility = View.VISIBLE
                ll_map_chat_and_join_button_container.visibility = View.GONE
                ll_map_new_post_button_container.visibility = View.VISIBLE

                // name, addr, road_addr setting
                tv_map_karaoke_name.text = karaoke!!.name
                tv_map_karaoke_addr.text = karaoke.address
                tv_map_karaoke_road_addr.text = karaoke.roadAddress

                // phone number setting
                if(karaoke.phoneNumber == "") {
                    tv_map_karaoke_phone.text = "전화번호가 없어요!"
                    tv_map_karaoke_phone.setTextColor(Color.parseColor("#D3D3D3"))
                } else {
                    tv_map_karaoke_phone.text = karaoke.phoneNumber
                    tv_map_karaoke_phone.setTextColor(Color.parseColor("#000000"))
                }

                // if i already uploaded the post, cannot press the button
                tv_map_new_post_button.setOnClickListener {  }

            } else {
                // post info
                val user = item.post.host

                // visibility setting
                ll_map_karaoke_info_container.visibility = View.GONE
                ll_map_post_info_container.visibility = View.VISIBLE
                ll_map_new_post_button_container.visibility = View.GONE
                ll_map_chat_and_join_button_container.visibility = View.VISIBLE

                // setting user name
                tv_map_post_username.text = user.name
                // setting gender icon
//                if(user.gender)
//                    tv_map_post_username.setCompoundDrawables(context.getDrawable(R.drawable.ic_male), null, null, null)
//                else
//                    tv_map_post_username.setCompoundDrawables(context.getDrawable(R.drawable.ic_female), null, null, null)

                // setting user comment
                if(item.post.comment == "")
                    tv_map_post_user_comment.text = "안녕하세요."
                else
                    tv_map_post_user_comment.text = item.post.comment

                // setting user genre list
                for(i: Int in 0 until user.genreInIndex.size) {
                    val textView = TextView(context)
                    textView.text = genres[user.genreInIndex[i]]
                    textView.textSize = 10f
                    textView.setPadding(8, 8, 8, 8)
                    textView.gravity = Gravity.CENTER
                    textView.setBackgroundResource(R.drawable.background_circular_dynamic)
                    textView.setTextColor(Color.parseColor("#E45477"))
                    textView.id = i

                    val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    param.marginStart = 8
                    param.marginEnd = 8
                    param.topMargin = 4
                    param.bottomMargin = 4

                    textView.layoutParams = param

                    if(i > 5) {
                        ll_map_post_user_genre_container2.visibility = View.VISIBLE
                        ll_map_post_user_genre_container2.addView(textView)
                    } else {
                        ll_map_post_user_genre_container1.addView(textView)
                    }
                }

                val tempView1 = View(context)
                val tempParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                tempParam.weight = 1f
                tempView1.layoutParams = tempParam
                ll_map_post_user_genre_container1.addView(tempView1)
                if(ll_map_post_user_genre_container2.visibility == View.VISIBLE) {
                    val tempView2 = View(context)
                    val tempParam: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                    tempParam.weight = 1f
                    tempView2.layoutParams = tempParam
                    ll_map_post_user_genre_container2.addView(tempView2)
                }

                // if it is my post
                if(user.hashCode() == user1.hashCode()) {
                    tv_map_post_chat_button.setTextColor(Color.parseColor("#D3D3D3"))
                    tv_map_post_join_button.setTextColor(Color.parseColor("#D3D3D3"))
                } else {
                    tv_map_post_chat_button.setTextColor(Color.parseColor("#000000"))
                    tv_map_post_join_button.setTextColor(Color.parseColor("#000000"))

                    // button setOnClickListener
                    tv_map_post_chat_button.setOnClickListener {  }
                    tv_map_post_join_button.setOnClickListener {  }
                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.info_card_view_item_list, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }
}