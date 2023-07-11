package com.example.madcamp_week2.ui.chat

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.provider.BaseColumns
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.data.ChatMessage
import com.example.madcamp_week2.api.data.ChatRoom
import com.example.madcamp_week2.api.data.User
import com.example.madcamp_week2.databinding.ActivityChatRoomBinding
import com.example.madcamp_week2.db.ChatLogDBHelper
import com.example.madcamp_week2.db.ChatLogReaderContract
import com.example.madcamp_week2.db.RoomDBHelper
import com.example.madcamp_week2.db.RoomReaderContract
import com.example.madcamp_week2.util.SocketCompanion
import com.example.madcamp_week2.util.getUserInfoFromToken
import com.google.gson.Gson
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.engineio.client.Transport


class ChatRoomActivity : AppCompatActivity() {

    private lateinit var user: User
    private lateinit var otherId: String
    private lateinit var otherUsername: String
    private var otherProfileImage: String? = null
    private lateinit var roomNumber: String
    private lateinit var binding: ActivityChatRoomBinding
    private lateinit var adapter: ChatMessageListAdapter
    private lateinit var mSocket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get user information
        user = getUserInfoFromToken(applicationContext)

        // get receiver information
        otherId = intent.getStringExtra("otherId")!!
        otherUsername = intent.getStringExtra("otherUsername")!!
        otherProfileImage = intent.getStringExtra("otherProfileImage")

        // get room number
        roomNumber = intent.getStringExtra("roomNumber") ?: createRoom(applicationContext)

        mSocket = SocketCompanion.get()
        if (!mSocket.connected()) {
            mSocket.connect()
        }
        mSocket.io().on(Manager.EVENT_TRANSPORT) { args ->
            val transport: Transport = args[0] as Transport
            transport.on(Transport.EVENT_ERROR) { args ->
                val e = args[0] as Exception
                Log.e("chat room activity", "Transport error $e")
                e.printStackTrace()
                e.cause!!.printStackTrace()
            }
        }

        val gson = Gson()
        mSocket.emit(
            "enter",
            gson.toJson(
                ChatRoom(
                    username = user.nickname,
                    roomNumber = roomNumber,
                    profileImage = user.profileImage
                )
            )
        )
        mSocket.on("update") { args: Array<Any?>? ->
            val data: ChatMessage = gson.fromJson(args!![0].toString(), ChatMessage::class.java)
            Log.d("chat room activity", "$data")
            addChat(data, applicationContext)
        }

        // get chat log
        val chatLog = getChatLog(applicationContext, roomNumber)
        adapter = ChatMessageListAdapter(chatLog, user)
        binding.rcvChatMsgList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvChatMsgList.adapter = adapter
        binding.chatEditText.setText("")
        binding.sendButton.setOnClickListener {
            sendMessage()
        }
    }
    private fun sendMessage() {
        val now = System.currentTimeMillis()
        val message = binding.chatEditText.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(message)) {
            return
        }
        val gson = Gson()
        val data = ChatMessage(id = null, senderId = user.id, senderName = user.nickname, msg = binding.chatEditText.text.toString(), roomNumber = roomNumber, senderProfileImage = user.profileImage, receiverId = otherId, timestamp = now)
        mSocket.emit("newMessage", gson.toJson(data))
        binding.chatEditText.setText("")
    }

    private fun getChatLog(context: Context, roomNumber: String): MutableList<ChatMessage> {
        val db = ChatLogDBHelper(context).readableDatabase
        val projection = arrayOf(BaseColumns._ID, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_ROOM_NUMBER, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_MESSAGE, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_NAME, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_PROFILE_IMAGE, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_TIMESTAMP, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_RECEIVER_ID, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_ID)
        val selection = "${ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_ROOM_NUMBER} = ?"
        val selectionArgs = arrayOf(roomNumber)
        val sortOrder = "${ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_TIMESTAMP} ASC"
        val cursor = db.query(
            ChatLogReaderContract.ChatLogEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        val chatLogList = mutableListOf<ChatMessage>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val senderId = getString(getColumnIndexOrThrow(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_ID))
                val receiverId = getString(getColumnIndexOrThrow(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_RECEIVER_ID))
                val roomNumber = getString(getColumnIndexOrThrow(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_ROOM_NUMBER))
                val message = getString(getColumnIndexOrThrow(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_MESSAGE))
                val senderName = getString(getColumnIndexOrThrow(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_NAME))
                val timestamp = getLong(getColumnIndexOrThrow(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_TIMESTAMP))
                val senderProfileImage = getStringOrNull(getColumnIndexOrThrow(ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_PROFILE_IMAGE))
                chatLogList.add(
                    ChatMessage(
                        id = id,
                        senderName = senderName,
                        roomNumber = roomNumber,
                        timestamp = timestamp,
                        senderProfileImage = senderProfileImage,
                        msg = message,
                        receiverId = receiverId,
                        senderId = senderId
                    )
                )
            }
        }
        cursor.close()
        return chatLogList
    }

    private fun addChat(data: ChatMessage, context: Context) {
        runOnUiThread {
            adapter.addItem(data)
            adapter.notifyDataSetChanged()
            if (data.senderId == user.id) {
                addChatLogToDB(context, data)
            }
        }
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

    private fun createRoom(context: Context): String {
        val db = RoomDBHelper(context).writableDatabase
        val values = ContentValues()
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_MY_ID, user.id)
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_ID, otherId)
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_ROOM_NUMBER, user.id + otherId)
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_USERNAME, otherUsername)
        values.put(RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_PROFILE_IMAGE, otherProfileImage)
        val newRowId = db.insert(RoomReaderContract.RoomEntry.TABLE_NAME, null, values)
        if (newRowId == -1L) {
            Log.d("creating chat room", "내역을 추가하지 못했습니다.")
        } else {
            Log.d("creating chat room", "내역을 추가했습니다.")
        }
        db.close()
        return user.id + otherId
    }
}