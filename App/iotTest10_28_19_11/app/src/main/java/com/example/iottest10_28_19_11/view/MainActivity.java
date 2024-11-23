package com.example.iottest10_28_19_11.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iottest10_28_19_11.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo TextView để hiển thị FCM Token
        tvStatus = findViewById(R.id.tvStatus);

        // Lấy FCM Token khi ứng dụng khởi động
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Lấy FCM Token
                    String token = task.getResult();
                    Log.d(TAG, "FCM Token: " + token);

                    // Hiển thị FCM Token trong UI (hoặc có thể gửi đến server)
                    tvStatus.setText("FCM Token: " + token);
                });
    }
}
