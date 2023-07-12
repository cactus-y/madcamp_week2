package com.example.madcamp_week2.db

import android.provider.BaseColumns

object ChatLogReaderContract {
    object ChatLogEntry : BaseColumns {
        const val TABLE_NAME = "chatLog"
        const val COLUMN_NAME_ROOM_NUMBER = "roomNumber"
        const val COLUMN_NAME_MESSAGE = "message"
        const val COLUMN_NAME_RECEIVER_ID = "receiverId"
        const val COLUMN_NAME_SENDER_ID = "senderId"
        const val COLUMN_NAME_SENDER_NAME = "senderName"
        const val COLUMN_NAME_SENDER_PROFILE_IMAGE = "senderProfileImage"
        const val COLUMN_NAME_TIMESTAMP = "timestamp"
        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${ChatLogEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${ChatLogEntry.COLUMN_NAME_ROOM_NUMBER} TEXT," +
                    "${ChatLogEntry.COLUMN_NAME_MESSAGE} TEXT," +
                    "${ChatLogEntry.COLUMN_NAME_SENDER_NAME} TEXT," +
                    "${ChatLogEntry.COLUMN_NAME_RECEIVER_ID} TEXT," +
                    "${ChatLogEntry.COLUMN_NAME_SENDER_ID} TEXT," +
                    "${ChatLogEntry.COLUMN_NAME_TIMESTAMP} INTEGER," +
                    "${ChatLogEntry.COLUMN_NAME_SENDER_PROFILE_IMAGE} TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ChatLogEntry.TABLE_NAME}"
    }

}