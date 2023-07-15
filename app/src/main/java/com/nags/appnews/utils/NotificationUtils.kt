package com.nags.appnews.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.nags.appnews.R
import com.nags.appnews.model.PushNotificationModel

/**
 * Sends a notification using the provided push notification data.
 *
 * @param applicationContext The application context.
 * @param pushNotificationModel The push notification data.
 */
fun NotificationManager.sendNotification(
    applicationContext: Context,
    pushNotificationModel: PushNotificationModel
) {
    showNotification(applicationContext, pushNotificationModel)
}

/**
 * Shows the notification with the provided push notification data.
 *
 * @param applicationContext The application context.
 * @param pushNotificationModel The push notification data.
 */
private fun showNotification(
    applicationContext: Context,
    pushNotificationModel: PushNotificationModel?
) {
    // Customize the notification appearance and behavior
    val channelId = CHANNEL_ID
    val channelName = CHANNEL_NAME
    val notificationId = NOTIFICATION_ID

    val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(pushNotificationModel?.title)
        .setContentText(pushNotificationModel?.message)
        .setAutoCancel(true)

    val notificationManager = getSystemService(
        applicationContext,
        NotificationManager::class.java
    ) as NotificationManager

    // Create a notification channel (for Android 8.0 and above)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    // Show the notification
    notificationManager.notify(notificationId, notificationBuilder.build())
}
