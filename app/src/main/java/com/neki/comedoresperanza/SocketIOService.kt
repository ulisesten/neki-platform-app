package com.neki.comedoresperanza

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.neki.comedoresperanza.utils.Message
import com.neki.comedoresperanza.utils.NotificationsHandler
import com.neki.comedoresperanza.utils.WebSocket


class SocketIOService : Service() {
    private val CHANNEL_ID = "Neki listener"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, SocketIOService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, SocketIOService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Toast.makeText(this, "Service",Toast.LENGTH_SHORT).show()

        val prefs = this.getSharedPreferences("storage", Context.MODE_PRIVATE)
        val userId = prefs.getString("seller_id", "")

        WebSocket.getInstance().createSocket(userId!!)
        val io = WebSocket.getInstance().getSocket()
        //val notifications = NotificationsHandler()

        io!!.on("message",
        WebSocket.getInstance().onNewMessage { fromId, toId, senderName, receiverName, message, productId, timestamp ->

            NotificationsHandler.getInstance().setMessage(Message(fromId, toId, senderName, receiverName, message, productId, timestamp))
            notificationHandler(application, senderName, message)
        })

        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel(CHANNEL_ID)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Mensajería ML Ciudad Valles")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }


    private fun createNotificationChannel(channelId:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId, "Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }

    }

    private fun notificationHandler(activity: Context , name: String, message: String){

        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent
            .getActivity(activity, 0 /* Request code */, intent, 0)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        createMessageNotificationChannel(activity)
        val builder = NotificationCompat.Builder(activity, "0")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("${name} Te envió un mensaje"))
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setChannelId("0")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setWhen(System.currentTimeMillis())
            .setDefaults(
                NotificationCompat.DEFAULT_SOUND
                        or NotificationCompat.DEFAULT_VIBRATE
                        or NotificationCompat.DEFAULT_LIGHTS
            )

        with(NotificationManagerCompat.from(activity)){
            notify(0, builder.build())
        }

    }

    private fun createMessageNotificationChannel(activity: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = activity.getString(R.string.channel_name)
            val descriptionText = activity.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("0", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
