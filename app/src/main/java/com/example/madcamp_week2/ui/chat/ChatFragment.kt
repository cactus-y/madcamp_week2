package com.example.madcamp_week2.ui.chat

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.database.getStringOrNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.databinding.FragmentChatBinding
import com.example.madcamp_week2.db.ChatLogDBHelper
import com.example.madcamp_week2.db.ChatLogReaderContract
import com.example.madcamp_week2.db.ChatRoom
import com.example.madcamp_week2.db.RoomDBHelper
import com.example.madcamp_week2.db.RoomReaderContract
import com.example.madcamp_week2.util.MyBroadcastReceiver


class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var receiver: MyBroadcastReceiver;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
//        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        val root: View = binding.root
        binding.rcvChatList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val roomList = getRoomListFromDB(requireContext())
        val adapter = ChatListAdapter(roomList)
        binding.rcvChatList.adapter = adapter
        binding.floatingActionButton.setOnClickListener {
            Intent(requireContext(), ChatRoomActivity::class.java).apply {
                // putExtra
                putExtra("otherId", "64abee0a50f6ef1791f3653c")
                putExtra("otherUsername", "전산")
            }.run { requireContext().startActivity(this) }
        }
        receiver = MyBroadcastReceiver()
        receiver.setAdapter(adapter)
        val filter = IntentFilter("com.chat.notification")
        registerReceiver(requireContext(), receiver, filter, ContextCompat.RECEIVER_EXPORTED)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val roomList = getRoomListFromDB(requireContext())
        val adapter = ChatListAdapter(roomList)
        receiver.setAdapter(adapter)
        binding.rcvChatList.adapter = adapter
    }


    private fun getRoomListFromDB(context: Context): MutableList<ChatRoom>  {
        val db = RoomDBHelper(context).readableDatabase
        val projection = arrayOf(BaseColumns._ID, RoomReaderContract.RoomEntry.COLUMN_NAME_MY_ID, RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_ID, RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_USERNAME, RoomReaderContract.RoomEntry.COLUMN_NAME_ROOM_NUMBER, RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_PROFILE_IMAGE)
        val cursor = db.query(
            RoomReaderContract.RoomEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        val roomList = mutableListOf<com.example.madcamp_week2.db.ChatRoom>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val roomNumber = getString(getColumnIndexOrThrow(RoomReaderContract.RoomEntry.COLUMN_NAME_ROOM_NUMBER))
                val myId = getString(getColumnIndexOrThrow(RoomReaderContract.RoomEntry.COLUMN_NAME_MY_ID))
                val otherId = getString(getColumnIndexOrThrow(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_ID))
                val otherUsername = getString(getColumnIndexOrThrow(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_USERNAME))
                val otherProfileImage = getStringOrNull(getColumnIndexOrThrow(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_PROFILE_IMAGE))
                val message = getLatestMessage(context, roomNumber)
                if (message == null) {
                    val selection = "${BaseColumns._ID} = ?"
                    val selectionArgs = arrayOf(id.toString())
                    db.delete(RoomReaderContract.RoomEntry.TABLE_NAME, selection, selectionArgs)
                }
                else {
                    roomList.add(
                        ChatRoom(
                            id = id,
                            myId = myId,
                            otherId = otherId,
                            otherUsername = otherUsername,
                            otherProfileImage = otherProfileImage,
                            latestMessage = message,
                            roomNumber = roomNumber
                        )
                    )
                }
            }
        }
        cursor.close()
        db.close()
        return roomList
    }
    private fun getLatestMessage(context: Context, roomNumber: String): String? {
        val db = ChatLogDBHelper(context).readableDatabase
        val projection = arrayOf(BaseColumns._ID, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_MESSAGE, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_ROOM_NUMBER, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_TIMESTAMP)
        val selection = "${ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_ROOM_NUMBER} = ?"
        val selectionArgs = arrayOf(roomNumber)
        val sortOrder = "${ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_TIMESTAMP} DESC"
        val cursor = db.query(
            ChatLogReaderContract.ChatLogEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,             // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        if (cursor.moveToNext()) {
            val message =  cursor.getString(cursor.getColumnIndexOrThrow(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_MESSAGE));
            cursor.close()
            db.close()
            return message
        }
        else {
            cursor.close()
            db.close()
            return null;
        }

    }
}