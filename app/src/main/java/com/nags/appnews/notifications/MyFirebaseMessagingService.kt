package com.nags.appnews.notifications

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.nags.appnews.model.PushNotificationModel
import com.nags.appnews.utils.sendNotification
import org.json.JSONObject

/**
 * Firebase Messaging Service responsible for receiving and handling push notifications.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when a new token is generated for the device.
     *
     * @param token The new token generated for the device.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("token", "onNewToken: $token")
        // Store the new token in SharedPreferences
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply()
    }

    /**
     * Called when a new push notification message is received.
     *
     * @param remoteMessage The received remote message.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        remoteMessage.data.let {
            Log.d("onMessageReceived", "onMessageReceived: ${remoteMessage.data}")
            val json = JSONObject(remoteMessage.data as Map<*, *>)
            handleDataMessage(json)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("onMessageReceived", "onMessageReceived: ${it.body}")
        }
    }

    /**
     * Sends a notification based on the received push notification model.
     *
     * @param pushNotificationModel The push notification model.
     */
    private fun sendNotification(pushNotificationModel: PushNotificationModel) {
        val notificationManager: NotificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(
            applicationContext,
            pushNotificationModel
        )
    }

    /**
     * Handles the received data message in JSON format.
     *
     * @param json The JSON object representing the data message.
     */
    private fun handleDataMessage(json: JSONObject) {
        Log.d("handleDataMessage", "handleDataMessage: $json")
        try {
            val pushNotificationModel =
                Gson().fromJson(json.toString(), PushNotificationModel::class.java)
            sendNotification(pushNotificationModel)
        } catch (e: Exception) {
            Log.d("handleDataMessage", "handleDataMessage: ${e.message.toString()}")
        }
    }
}
