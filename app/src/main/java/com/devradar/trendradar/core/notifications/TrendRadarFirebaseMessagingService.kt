package com.devradar.trendradar.core.notifications
import com.google.firebase.messaging.FirebaseMessagingService; import com.google.firebase.messaging.RemoteMessage
class TrendRadarFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(m: RemoteMessage) {}
    override fun onNewToken(t: String) {}
}
