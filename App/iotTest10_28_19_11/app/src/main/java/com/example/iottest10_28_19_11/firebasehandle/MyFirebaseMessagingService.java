package com.example.iottest10_28_19_11.firebasehandle;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.iottest10_28_19_11.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // Kiểm tra nếu có dữ liệu trong tin nhắn
//        if (remoteMessage.getData().size() > 0) {
//            // Xử lý dữ liệu
//            String data = remoteMessage.getData().toString();
//            // Có thể hiển thị thông báo hoặc cập nhật UI ở đây
//        }
//
//        // Nếu có thông báo (notification)
//        if (remoteMessage.getNotification() != null) {
//            String message = remoteMessage.getNotification().getBody();
//            // Hiển thị thông báo hoặc xử lý
//        }
//    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if there is any data in the message
        if (remoteMessage.getData().size() > 0) {
            // Extract latitude and longitude from the data
            String latitudeStr = remoteMessage.getData().get("latitude");
            String longitudeStr = remoteMessage.getData().get("longitude");

            if (latitudeStr != null && longitudeStr != null) {
                double latitude = Double.parseDouble(latitudeStr);
                double longitude = Double.parseDouble(longitudeStr);

                // Create the Google Maps URL using the latitude and longitude
                String googleMapsUrl = "https://www.google.com/maps?q=" + latitude + "," + longitude;

                // Intent to open Google Maps
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsUrl));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        // Handle the notification body (optional, for showing custom notifications)
        if (remoteMessage.getNotification() != null) {
            String message = remoteMessage.getNotification().getBody();
            // You can also show a custom notification or handle it as needed
        }
    }



    @Override
    public void onNewToken(String token) {
        // Lưu token mới khi FCM thay đổi
        // Token có thể được gửi đến server để sử dụng cho việc gửi tin nhắn
        super.onNewToken(token);
    }
}
