package com.example.madcamp_week2.ui.map

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.APIObject
import com.example.madcamp_week2.api.data.KaraokeOrBoard
import com.example.madcamp_week2.api.data.guest.PostGuestRequestBody
import com.example.madcamp_week2.api.data.guest.PostGuestResponseBody
import com.example.madcamp_week2.getUserInfoFromToken
import com.example.madcamp_week2.getUserToken
import com.example.madcamp_week2.ui.chat.ChatRoomActivity
import retrofit2.Call
import retrofit2.Response

class CardListAdapter(private var list: MutableList<KaraokeOrBoard>): RecyclerView.Adapter<CardListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        private val context = itemView!!.context
        var validPostGuest = true
        var validChat = true

        // user info
        val me = getUserInfoFromToken(context)
        val token = "Bearer ${getUserToken(context)}"

        // karaoke info cardview
        var ll_map_karaoke_info_container: LinearLayout = itemView!!.findViewById(R.id.ll_map_karaoke_info_container)
        var tv_map_karaoke_name: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_name)
        var tv_map_karaoke_addr: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_addr)
        var tv_map_karaoke_road_addr: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_road_addr)
        var tv_map_karaoke_phone: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_phone)
        var tv_map_karaoke_distance: TextView = itemView!!.findViewById(R.id.tv_map_karaoke_distance)
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


        fun bind(item: KaraokeOrBoard, position: Int) {
//            println("\n\n\nAdapter position: ${position}\n\n\n")
            if(item.board == null) {
                // karaoke info
                val karaoke = item.karaoke

                // visibility setting
                ll_map_post_info_container.visibility = View.GONE
                ll_map_karaoke_info_container.visibility = View.VISIBLE
                ll_map_chat_and_join_button_container.visibility = View.GONE
                ll_map_new_post_button_container.visibility = View.VISIBLE

                // name, addr, road_addr, distance setting
                tv_map_karaoke_name.text = karaoke!!.name
                tv_map_karaoke_addr.text = karaoke.address
                tv_map_karaoke_road_addr.text = karaoke.roadAddress
                tv_map_karaoke_distance.text = karaoke.distance.toString() + "m"

                // phone number setting
                if(karaoke.phone == "") {
                    tv_map_karaoke_phone.text = "전화번호가 없어요!"
                    tv_map_karaoke_phone.setTextColor(Color.parseColor("#D3D3D3"))
                } else {
                    tv_map_karaoke_phone.text = karaoke.phone
                    tv_map_karaoke_phone.setTextColor(Color.parseColor("#000000"))
                }

                var myBoardExist = false

                list.forEach {
                    if(it.board?.author?.id == me.id)
                        myBoardExist = true
                }


                // if i already uploaded the post, cannot press the button
                if(myBoardExist)
                    tv_map_new_post_button.setTextColor(Color.parseColor("#D3D3D3"))
                else {
                    tv_map_new_post_button.setOnClickListener {
                        Intent(context, NewBoardActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            putExtra("karaokeObjectId", karaoke.karaokeObjectId)
                            putExtra("karaokeName", karaoke.name)
                            putExtra("karaokeAddr", karaoke.address)
                            putExtra("karaokeRoadAddr", karaoke.roadAddress)
                            putExtra("karaokePhone", karaoke.phone)
                        }.run { context.startActivity(this) }
                    }
                }
            } else {
                // board info
                val author = item.board.author
                val guestList = item.board.guestList
                var validJoin = true

                // visibility setting
                ll_map_karaoke_info_container.visibility = View.GONE
                ll_map_post_info_container.visibility = View.VISIBLE
                ll_map_new_post_button_container.visibility = View.GONE
                ll_map_chat_and_join_button_container.visibility = View.VISIBLE

                // setting user name
                tv_map_post_username.text = author.nickname
                // setting gender icon
//                if(user.gender)
//                    tv_map_post_username.setCompoundDrawables(context.getDrawable(R.drawable.ic_male), null, null, null)
//                else
//                    tv_map_post_username.setCompoundDrawables(context.getDrawable(R.drawable.ic_female), null, null, null)

                // setting user comment
                if(item.board.comment == "")
                    tv_map_post_user_comment.text = "안녕하세요."
                else
                    tv_map_post_user_comment.text = item.board.comment

                // setting user genre list
                for(i: Int in 0 until author.musicGenre.size) {
                    val textView = TextView(context)
                    textView.text = author.musicGenre[i]
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

                guestList.forEach {
                    // already joined
                    if(it.id == me.id)
                        validJoin = false
                }

                // if it is my post
                if(author.id == me.id) {
                    validJoin = false
                    validChat = false
                }

                // setting join button
                if(validJoin) {
                    tv_map_post_join_button.setTextColor(Color.parseColor("#000000"))
                    tv_map_post_join_button.setOnClickListener {
                        val call = APIObject.getGuestService.postNewGuest(
                            token,
                            PostGuestRequestBody(item.board.boardObjectId)
                        )
                        call.enqueue(object: retrofit2.Callback<PostGuestResponseBody> {
                            override fun onResponse(
                                call: Call<PostGuestResponseBody>,
                                response: Response<PostGuestResponseBody>
                            ) {
                                if(response.isSuccessful) {
                                    val data: PostGuestResponseBody? = response.body()
                                    println("\n\n\n${response.body()}\n\n\n")
                                    Log.d("POST guest", response.body()?.success.toString())

                                    // make button invalid
                                    tv_map_post_join_button.setTextColor(Color.parseColor("#D3D3D3"))
                                    validJoin = false

                                    Toast.makeText(context, "${author.nickname}님과 함께 가요!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "이미 약속을 잡았어요!", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<PostGuestResponseBody>, t: Throwable) {
                                Toast.makeText(context, "Failed to join!", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    tv_map_post_join_button.setTextColor(Color.parseColor("#D3D3D3"))
                }

                // setting chat button
                if(validChat) {
                    tv_map_post_chat_button.setTextColor(Color.parseColor("#000000"))
                    tv_map_post_chat_button.setOnClickListener {
                        Intent(context, ChatRoomActivity::class.java).apply {
                            putExtra("otherId", author.id)
                            putExtra("otherUsername", author.nickname)
                        }.run { context.startActivity(this) }
                    }
                } else
                    tv_map_post_chat_button.setTextColor(Color.parseColor("#D3D3D3"))
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