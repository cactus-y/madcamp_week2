package com.example.madcamp_week2.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.madcamp_week2.MainActivity
import com.example.madcamp_week2.R
import com.example.madcamp_week2.api.data.ChatMessage
import com.example.madcamp_week2.db.ChatRoom
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FirebaseTest"

    // 메세지가 수신되면 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.data.isNotEmpty()){
            val data = remoteMessage.data
            val roomNumber: String =  data["roomNumber"]!!
            val receiverId: String = data["receiverId"]!!
            val senderId: String = data["senderId"]!!
            val senderName: String = data["senderName"]!!
            val senderProfileImage: String = data["senderProfileImage"]!!
            val message: String = data["message"]!!
            val timestamp: String = data["timestamp"]!!
            val applicationContext: Context = applicationContext
            var roomAdded = false
            if (!isRoomExist(applicationContext, roomNumber)) {
                val roomData = ChatRoom(
                    id = null,
                    roomNumber = roomNumber,
                    myId = receiverId,
                    otherId = senderId,
                    otherUsername = senderName,
                    otherProfileImage = senderProfileImage,
                    latestMessage = message,
                )
                createRoom(applicationContext, roomData)
                roomAdded = true
            }
            val chatRoomIntent = Intent()
            chatRoomIntent.action = "com.chatroom.notification"
            chatRoomIntent.putExtra("receiverId", receiverId)
            chatRoomIntent.putExtra("roomNumber", roomNumber)
            chatRoomIntent.putExtra("message", data["message"])
            chatRoomIntent.putExtra("senderName", data["senderName"])
            chatRoomIntent.putExtra("senderId", data["senderId"])
            chatRoomIntent.putExtra("senderProfileImage", data["senderProfileImage"])
            chatRoomIntent.putExtra("timestamp", data["timestamp"])
            chatRoomIntent.putExtra("roomAdded", roomAdded)
            sendBroadcast(chatRoomIntent)

            val messageData = ChatMessage(
                id = null,
                receiverId = receiverId,
                roomNumber = roomNumber,
                senderName =  senderName,
                msg = message,
                senderId = senderId,
                senderProfileImage = senderProfileImage,
                timestamp = timestamp.toLong()
            )
            val id = addChatLogToDB(applicationContext, messageData)
            val chatMessageIntent = Intent()
            chatMessageIntent.action = "com.chatmessage.notification"
            chatMessageIntent.putExtra("id", id)
            chatMessageIntent.putExtra("receiverId", receiverId)
            chatMessageIntent.putExtra("roomNumber", roomNumber)
            chatMessageIntent.putExtra("message", data["message"])
            chatMessageIntent.putExtra("senderName", data["senderName"])
            chatMessageIntent.putExtra("senderId", data["senderId"])
            chatMessageIntent.putExtra("senderProfileImage", data["senderProfileImage"])
            chatMessageIntent.putExtra("timestamp", data["timestamp"])
            sendBroadcast(chatMessageIntent)
            if (ForegroundDetector.instance!!.isBackground) {
                sendNotification(remoteMessage.data["title"], remoteMessage.data["body"]!!)
            }
            Log.d("data", "$data")
        }
    }

    // Firebase Cloud Messaging Server 가 대기중인 메세지를 삭제 시 호출
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    // 메세지가 서버로 전송 성공 했을때 호출
    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
    }

    // 메세지가 서버로 전송 실패 했을때 호출
    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
    }

    // 새로운 토큰이 생성 될 때 호출
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, token)
    }

    private fun sendNotification(title: String?, body: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("openChat", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        ) // 일회성


        val channelId = getString(com.example.madcamp_week2.R.string.default_notification_channel_id) // 채널 아이디
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 소리
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title) // 제목
            .setContentText(body) // 내용
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setSmallIcon(R.drawable.google_icon)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val localTime: Long = System.currentTimeMillis()
        notificationManager.notify(localTime.toInt() , notificationBuilder.build()) // 알림 생성
    }
}