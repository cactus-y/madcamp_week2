package com.example.madcamp_week2.db

import android.provider.BaseColumns

object RoomReaderContract {
    object RoomEntry : BaseColumns {
        const val TABLE_NAME = "room"
        const val COLUMN_NAME_MY_ID = "myId"
        const val COLUMN_NAME_OTHER_ID = "otherId"
        const val COLUMN_NAME_OTHER_USERNAME = "otherUsername"
        const val COLUMN_NAME_OTHER_PROFILE_IMAGE = "otherProfileImage"
        const val COLUMN_NAME_ROOM_NUMBER = "roomNumber"
        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${RoomEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${RoomEntry.COLUMN_NAME_MY_ID} TEXT," +
                    "${RoomEntry.COLUMN_NAME_ROOM_NUMBER} TEXT," +
                    "${RoomEntry.COLUMN_NAME_OTHER_ID} TEXT," +
                    "${RoomEntry.COLUMN_NAME_OTHER_USERNAME} TEXT," +
                    "${RoomEntry.COLUMN_NAME_OTHER_PROFILE_IMAGE} TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${RoomEntry.TABLE_NAME}"
    }
}