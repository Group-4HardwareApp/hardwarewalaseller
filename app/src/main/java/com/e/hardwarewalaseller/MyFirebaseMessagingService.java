package com.e.hardwarewalaseller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "001";
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name,importance);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(mChannel);

            Notification.Builder builder = new Notification.Builder(MyFirebaseMessagingService.this,CHANNEL_ID);
                    builder.setContentTitle(title)
                    .setContentText(message)
                            .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.bell)
                    .setChannelId(CHANNEL_ID)
                    .build();

            NotificationManagerCompat notificationManagerCompat= NotificationManagerCompat.from(this);
//            manager.notify((int) System.currentTimeMillis(), notification);
            notificationManagerCompat.notify(001,builder.build());

        }



    else
        {
            if (remoteMessage.getNotification() != null) {
                title = remoteMessage.getNotification().getTitle();
                message = remoteMessage.getNotification().getBody();
            }
            Notification notification = new NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.drawable.bell).setContentTitle(title).setContentText(message).build();


            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
           manager.notify((int) System.currentTimeMillis(), notification);
        }



    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("s", "onNewTokenCalled" + s);
    }
}
