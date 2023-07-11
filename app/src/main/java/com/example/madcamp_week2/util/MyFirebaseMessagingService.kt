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
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


public class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FirebaseTest"

    // 메세지가 수신되면 호출
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.data.isNotEmpty()){
            val notification = remoteMessage.notification
            val data = remoteMessage.data
            val intent = Intent()
            intent.action = "com.chat.notification"
            intent.putExtra("receiverId", data["receiverId"])
            intent.putExtra("roomNumber", data["roomNumber"])
            intent.putExtra("message", data["message"])
            intent.putExtra("senderName", data["senderName"])
            intent.putExtra("senderId", data["senderId"])
            intent.putExtra("senderProfileImage", data["senderProfileImage"])
            intent.putExtra("timestamp", data["timestamp"])
            sendBroadcast(intent)

            Log.d("notification", "$notification")
            Log.d("data", "$data")
            sendNotification(notification?.title, notification?.body!!)
        }
        else{

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
        intent.putExtra("openChat", true)
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

        notificationManager.notify(0 , notificationBuilder.build()) // 알림 생성
    }
}