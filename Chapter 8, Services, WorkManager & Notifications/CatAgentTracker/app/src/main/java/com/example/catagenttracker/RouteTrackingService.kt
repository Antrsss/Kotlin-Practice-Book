package com.example.catagenttracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RouteTrackingService : Service() {

    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var serviceHandler: Handler
    //we don't rely on this function & it's safe to return null
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        notificationBuilder = startForegroundService()
        val handlerThread = HandlerThread("RouteTracking").apply { start() }
        serviceHandler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val returnValue = super.onStartCommand(intent, flags, startId)

        val agentId = intent?.getStringExtra(EXTRA_SECRET_CAT_AGENT_ID)
            ?: throw IllegalStateException("Agent ID must be provided")
        serviceHandler.post {
            trackToDestination(notificationBuilder)
            notifyCompletion(agentId)
            stopForeground(true)
            stopSelf()
        }
        return returnValue
    }

    //launching MainActivity from notification
    private fun getPendingIntent() : PendingIntent {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            FLAG_IMMUTABLE else 0
        return PendingIntent.getActivity(this, 0, Intent(
            this, MainActivity::class.java), flag
        )
    }

    private  fun createNotificationChannel(): String =
        //checking Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Id is unique for a package
            val newChannelId = "CatDispatch"
            //name which visible to user (it should be localized)
            val channelName = "Cat Dispatch Tracking"
            val channel = NotificationChannel(
                newChannelId, channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val service = requireNotNull(
                ContextCompat.getSystemService(
                    this, NotificationManager::class.java
                )
            )
            service.createNotificationChannel(channel)
            newChannelId
        } else { "" }

    //provides us with Notification.Builder
    private fun getNotificationBuilder(
        pendingIntent: PendingIntent, channelId: String
    ) = NotificationCompat
        .Builder(this, channelId)
        .setContentTitle("Agent approaching destination")
        .setContentText("Agent dispatched")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentIntent(pendingIntent)
        .setTicker("Agent dispatched, tracking movement")
        //prevents from dismissing notification by user
        .setOngoing(true)

    private fun startForegroundService(): NotificationCompat.Builder {
        val pendingIntent = getPendingIntent()
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        } else { "" }
        val notificationBuilder = getNotificationBuilder(
            pendingIntent, channelId
        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(NOTIFICATION_ID, notificationBuilder.build())
        } else {
            startForeground(NOTIFICATION_ID, notificationBuilder.build(),
                FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        }
        return notificationBuilder
    }

    private fun trackToDestination(notificationBuilder: NotificationCompat.Builder) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        for (i in 10 downTo 0) {
            Thread.sleep(1000L)
            notificationBuilder.setContentText(
                "$i seconds to destination"
            ).setSilent(true)
            notificationManager.notify(
                NOTIFICATION_ID,
                notificationBuilder.build()
            )
        }
    }

    private fun notifyCompletion(agentId: String) {
        Handler(Looper.getMainLooper()).post {
            mutableTrackingCompletion.value = agentId
        }
    }

    companion object {
        const val NOTIFICATION_ID = 0xCA7
        const val EXTRA_SECRET_CAT_AGENT_ID = "scaId"

        private val mutableTrackingCompletion = MutableLiveData<String>()
        val trackingCompletion: LiveData<String> = mutableTrackingCompletion
    }
}