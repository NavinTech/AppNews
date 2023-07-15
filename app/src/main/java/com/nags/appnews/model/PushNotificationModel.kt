package com.nags.appnews.model

/**
 * Model class representing a push notification.
 *
 * @param message The message content of the notification.
 * @param title The title of the notification.
 * @param imgURL The URL of the image associated with the notification.
 */
data class PushNotificationModel(
    val message: String?,
    val title: String?,
    val imgURL: String?
)
