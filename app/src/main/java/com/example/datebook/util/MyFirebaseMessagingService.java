package com.example.datebook.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.datebook.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();

        String recipient_id = remoteMessage.getData().get("recipient_id");
        NotificationCompat.Builder mMessageBuilder =
                new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.drawable.ic_datebook).setContentTitle(messageTitle).setContentText(messageBody);

        Intent resultChatIntent = new Intent(click_action);
        resultChatIntent.putExtra("recipient_id", recipient_id);

        PendingIntent resultChatPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultChatIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        mMessageBuilder.setContentIntent(resultChatPendingIntent);

        int mChatNotificationId = (int) System.currentTimeMillis();
        NotificationManager mChatNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mChatNotifyManager.notify(mChatNotificationId, mMessageBuilder.build());
    }
}
