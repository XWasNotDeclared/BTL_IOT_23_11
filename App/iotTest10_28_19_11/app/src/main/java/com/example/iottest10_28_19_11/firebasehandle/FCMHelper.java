package com.example.iottest10_28_19_11.firebasehandle;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCMHelper {
    public static void getFCMToken(TokenCallback callback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Nếu lấy token thất bại
                        callback.onFailure(task.getException());
                        return;
                    }

                    // Lấy token thành công
                    String token = task.getResult();
                    callback.onSuccess(token);
                });
    }

    // Callback để xử lý kết quả
    public interface TokenCallback {
        void onSuccess(String token);
        void onFailure(Exception exception);
    }
}
