package com.example.madcamp_week2.util

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.BaseColumns
import android.util.Log
import com.example.madcamp_week2.api.data.ChatMessage
import com.example.madcamp_week2.db.ChatLogDBHelper
import com.example.madcamp_week2.db.ChatLogReaderContract
import com.example.madcamp_week2.db.ChatRoom
import com.example.madcamp_week2.db.RoomDBHelper
import com.example.madcamp_week2.db.RoomReaderContract
import com.example.madcamp_week2.ui.chat.ChatListAdapter

class MyBroadcastReceiver(adapter: ChatListAdapter) : BroadcastReceiver() {
    private var adapter: ChatListAdapter;
    init {
        this.adapter = adapter
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d("MyReceiver", "Intent: $intent")
        val receiverId = intent.getStringExtra("receiverId")!!
        val roomNumber = intent.getStringExtra("roomNumber")!!
        val message = intent.getStringExtra("message")!!
        val senderName = intent.getStringExtra("senderName")!!
        val senderId = intent.getStringExtra("senderId")!!
        val senderProfileImage = intent.getStringExtra("senderProfileImage")

        val roomData = ChatRoom(
            id = null,
            roomNumber = roomNumber,
            myId = receiverId,
            otherId = senderId,
            otherUsername = senderName,
            otherProfileImage = senderProfileImage,
            latestMessage = message
        )
        Log.d("receiverId", receiverId!!)
        Log.d("roomNumber", roomNumber!!)
        Log.d("message", message!!)
        Log.d("senderName", senderName!!)
        Log.d("senderId", senderId!!)
        Log.d("senderProfileImage", "$senderProfileImage")

        if (!isRoomExist(context, roomNumber)) {
            createRoom(context, roomData)
            adapter.addChatRoom(roomData)
            adapter.notifyDataSetChanged()
        }
        val messageData = ChatMessage(
            id = null,
            receiverId = receiverId,
            roomNumber = roomNumber,
            senderName =  senderName,
            msg = message,
            senderId = senderId,
            senderProfileImage = senderProfileImage,
            timestamp = 1L
        )
        addChatLogToDB(context, messageData)
    }

    private fun isRoomExist(context: Context, roomNumber: String): Boolean {
        val db = RoomDBHelper(context).readableDatabase
        val projection = arrayOf(BaseColumns._ID)
        val selection = "${RoomReaderContract.RoomEntry.COLUMN_NAME_ROOM_NUMBER} = ?"
        val selectionArgs = arrayOf(roomNumber)
        val cursor = db.query(
            RoomReaderContract.RoomEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        return cursor.moveToNext()
    }

    private fun createRoom(context: Context, data: ChatRoom): Long {
        val db = RoomDBHelper(context).writableDatabase
        val values = ContentValues()
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_MY_ID, data.myId)
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_ID, data.otherId)
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_USERNAME, data.otherUsername)
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_PROFILE_IMAGE, data.otherProfileImage)
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_ROOM_NUMBER, data.roomNumber)
        val newRowId = db.insert(RoomReaderContract.RoomEntry.TABLE_NAME, null, values)
        if (newRowId == -1L) {
            Log.d("creating chat room", "내역을 추가하지 못했습니다.")
        } else {
            Log.d("creating chat room", "내역을 추가했습니다.")
        }
        db.close()
        return newRowId
    }

    private fun addChatLogToDB(context: Context, data: ChatMessage) {
        val db = ChatLogDBHelper(context).writableDatabase
        val values = ContentValues()
        values.put(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_MESSAGE, data.msg)
        values.put(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_ROOM_NUMBER, data.roomNumber)
        values.put(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_NAME, data.senderName)
        values.put(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_ID, data.senderId)
        values.put(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_PROFILE_IMAGE, data.senderProfileImage)
        values.put(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_RECEIVER_ID, data.receiverId)
        values.put(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_TIMESTAMP, System.currentTimeMillis())
        val newRowId = db.insert(ChatLogReaderContract.ChatLogEntry.TABLE_NAME, null, values)
        if (newRowId == -1L) {
            Log.d("creating chat log", "내역을 추가하지 못했습니다.")
        } else {
            Log.d("creating chat log", "내역을 추가했습니다.")
        }
        db.close()
    }

    fun setAdapter(adapter: ChatListAdapter) {
        this.adapter = adapter
    }
}