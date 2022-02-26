package com.shrewd.poppydealers.utilities;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shrewd.poppydealers.R;

public class FcmService extends FirebaseMessagingService {
    private static final int id = 123;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        String clickAction = remoteMessage.getNotification().getClickAction();

        triggerNotification(title,body,clickAction);
    }

    public void triggerNotification(String title, String body, String myclick) {

        Intent intent = new Intent(myclick);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.id)
                .setContentTitle(title).setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title))
                .setSmallIcon(R.mipmap.app_logo)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(id, builder.build());

    }
}
