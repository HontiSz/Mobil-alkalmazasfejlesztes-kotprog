package hu.mobilalk.shop;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.collection.LLRBNode;

public class NotificationHandler {
    private NotificationManager manager;
    private Context context;

    private static final String chanId = "channel_id";
    private static final int notId = 0;

    public NotificationHandler(Context context) {
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel(chanId, "WebShop Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.BLUE);
        channel.setDescription("Hello én egy Notification vagyok (o′┏▽┓｀o) ");
        this.manager.createNotificationChannel(channel);
    }

    public void sendNotification(String message) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, chanId)
                .setContentTitle("Web Shop")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_add_shopping_cart)
                .setContentIntent(pendingIntent);

        this.manager.notify(notId, builder.build());
    }

    public void cancelNotification() {
        this.manager.cancel(notId);
    }
}
