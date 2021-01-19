package com.e.hardwarewalaseller;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String title, message, image;
    public static final String TAG = "My Tag";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "onMessageRecieved Called");
        Log.e(TAG, "onMessageRecieved from" + remoteMessage.getFrom());
        if (remoteMessage.getData() != null) {
            title = remoteMessage.getData().get("Title");
            message = remoteMessage.getData().get("Message");
            image = remoteMessage.getData().get("Image");
        } else {
//            Toast.makeText(this, "no notifcation", Toast.LENGTH_SHORT).show();
        }

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        }
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.bell).setContentTitle(title).setContentText(message).build();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(1002,notification);
        manager.notify((int) System.currentTimeMillis(), notification);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("s", "onNewTokenCalled" + s);
    }
}
