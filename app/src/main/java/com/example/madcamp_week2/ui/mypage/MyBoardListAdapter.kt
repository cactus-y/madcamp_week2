package com.example.madcamp_week2.ui.mypage

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.data.board.MyBoard
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyBoardListAdapter(private var list: MutableList<MyBoard>): RecyclerView.Adapter<MyBoardListAdapter.ListItemViewHolder>() {
    inner class ListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        private val context = itemView!!.context

        // karaoke info variable
        var tv_mypage_my_board_karaoke_name: TextView = itemView!!.findViewById(R.id.tv_mypage_my_board_karaoke_name)
        var tv_mypage_my_board_karaoke_address: TextView = itemView!!.findViewById(R.id.tv_mypage_my_board_karaoke_address)
        var tv_mypage_my_board_karaoke_road_address: TextView = itemView!!.findViewById(R.id.tv_mypage_my_board_karaoke_road_address)
        var tv_mypage_my_board_karaoke_phone: TextView = itemView!!.findViewById(R.id.tv_mypage_my_board_karaoke_phone)

        // guest info variable
        var tv_mypage_my_board_deadline: TextView = itemView!!.findViewById(R.id.tv_mypage_my_board_deadline)
        var ll_mypage_my_board_item_guest_container: LinearLayout = itemView!!.findViewById(R.id.ll_mypage_my_board_item_guest_container)
        var tv_mypage_my_board_guest_empty: TextView = itemView!!.findViewById(R.id.tv_mypage_my_board_guest_empty)
        var tv_mypage_my_board_comment: TextView = itemView!!.findViewById(R.id.tv_mypage_my_board_comment)

        fun bind(item: MyBoard, position: Int) {
            // date format
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val currentDate = Calendar.getInstance().time
            val deadLineDate = dateFormat.parse(item.deadLine)

            val karaoke = item.karaoke
            tv_mypage_my_board_karaoke_name.text = karaoke.name
            tv_mypage_my_board_karaoke_address.text = karaoke.address
            tv_mypage_my_board_karaoke_road_address.text = karaoke.roadAddress
            println("\n\n\n${karaoke.roadAddress}\n\n\n")

            if(karaoke.phone == "") {
                tv_mypage_my_board_karaoke_phone.text = "전화번호가 없어요!"
                tv_mypage_my_board_karaoke_phone.setTextColor(Color.parseColor("#D3D3D3"))
            } else
                tv_mypage_my_board_karaoke_phone.text = karaoke.phone

            tv_mypage_my_board_deadline.text = item.deadLine

            if(item.comment == "")
                tv_mypage_my_board_comment.text = "안녕하세요."
            else
                tv_mypage_my_board_comment.text = item.comment

            if(item.guestList.isEmpty()) {
                tv_mypage_my_board_guest_empty.visibility = View.VISIBLE
            } else {
                tv_mypage_my_board_guest_empty.visibility = View.GONE
                for(i: Int in 0 until item.guestList.size) {
                    if(i > 2) {
                        // etc textview and break
                        val textView = TextView(context)
                        textView.text = "외 ${item.guestList.size - 3}명"
                        textView.textSize = 10f
                        textView.setPadding(8, 8, 8, 8)
                        textView.gravity = Gravity.CENTER
                        textView.setBackgroundResource(R.drawable.background_circular_dynamic)
                        if(currentDate.after(deadLineDate))
                            textView.setTextColor(Color.parseColor("#808080"))
                        else
                            textView.setTextColor(Color.parseColor("#E45477"))
                        textView.id = i

                        val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        param.marginStart = 4
                        param.marginEnd = 4
                        param.topMargin = 4
                        param.bottomMargin = 4

                        textView.layoutParams = param

                        ll_mypage_my_board_item_guest_container.addView(textView)

                        break
                    }
                    val textView = TextView(context)
                    textView.text = item.guestList[i].nickname
                    textView.textSize = 10f
                    textView.setPadding(8, 8, 8, 8)
                    textView.gravity = Gravity.CENTER
                    textView.setBackgroundResource(R.drawable.background_circular_dynamic)
                    if(currentDate.after(deadLineDate))
                        textView.setTextColor(Color.parseColor("#808080"))
                    else
                        textView.setTextColor(Color.parseColor("#E45477"))
                    textView.id = i

                    val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    param.marginStart = 4
                    param.marginEnd = 4
                    param.topMargin = 4
                    param.bottomMargin = 4

                    textView.layoutParams = param
                    ll_mypage_my_board_item_guest_container.addView(textView)
                }
            }


            // if current date is after the deadline
            // set text color as gray
            if(currentDate.after(deadLineDate)) {
                tv_mypage_my_board_karaoke_name.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_board_karaoke_address.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_board_karaoke_road_address.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_board_karaoke_phone.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_board_deadline.setTextColor(Color.parseColor("#D3D3D3"))
                tv_mypage_my_board_comment.setTextColor(Color.parseColor("#D3D3D3"))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mypage_my_board_item_list, parent, false)
        return ListItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }
}