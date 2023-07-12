package com.example.madcamp_week2.util

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import com.example.madcamp_week2.api.data.ChatMessage
import com.example.madcamp_week2.db.ChatLogDBHelper
import com.example.madcamp_week2.db.ChatLogReaderContract
import com.example.madcamp_week2.db.ChatRoom
import com.example.madcamp_week2.db.RoomDBHelper
import com.example.madcamp_week2.db.RoomReaderContract

fun getRoomNumber(context: Context, data: ChatRoom): String {
    val db = RoomDBHelper(context).readableDatabase
    val projection = arrayOf(BaseColumns._ID, RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_ID, RoomReaderContract.RoomEntry.COLUMN_NAME_ROOM_NUMBER)
    val selection = "${RoomReaderContract.RoomEntry.COLUMN_NAME_OTHER_ID} = ?"
    val selectionArgs = arrayOf(data.otherId)
    val cursor = db.query(
        RoomReaderContract.RoomEntry.TABLE_NAME,   // The table to query
        projection,             // The array of columns to return (pass null to get all)
        selection,              // The columns for the WHERE clause
        selectionArgs,          // The values for the WHERE clause
        null,                   // don't group the rows
        null,                   // don't filter by row groups
        null               // The sort order
    )
    return if (cursor.moveToNext()) {
        cursor.getString(cursor.getColumnIndexOrThrow(RoomReaderContract.RoomEntry.COLUMN_NAME_ROOM_NUMBER))
    }
    else {
        createRoom(context, data)
    }
}

fun createRoom(context: Context, data: ChatRoom): String {
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
    return data.roomNumber
}

fun isRoomExist(context: Context, roomNumber: String): Boolean {
    val db = RoomDBHelper(context).readableDatabase
    val projection = arrayOf(BaseColumns._ID, RoomReaderContract.RoomEntry.COLUMN_NAME_ROOM_NUMBER)
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
    val exist = cursor.moveToNext()
    db.close()
    return exist
}

fun checkDuplicateMessage(context: Context, data: ChatMessage): Boolean {
    val db = ChatLogDBHelper(context).readableDatabase
    val projection = arrayOf(BaseColumns._ID, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_ID, ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_TIMESTAMP)
    val selection = "${ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_SENDER_ID} = ? AND ${ChatLogReaderContract.ChatLogEntry.COLUMN_NAME_TIMESTAMP} = ?"
    val selectionArgs = arrayOf(data.senderId, data.timestamp.toString())
    val cursor = db.query(
        ChatLogReaderContract.ChatLogEntry.TABLE_NAME,   // The table to query
        projection,             // The array of columns to return (pass null to get all)
        selection,              // The columns for the WHERE clause
        selectionArgs,          // The values for the WHERE clause
        null,                   // don't group the rows
        null,                   // don't filter by row groups
        null               // The sort order
    )
    val exist = cursor.moveToNext()
    db.close()
    return exist
}

fun addChatLogToDB(context: Context, data: ChatMessage): Long {
    if (checkDuplicateMessage(context, data)) return -1L;
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
    return newRowId
}