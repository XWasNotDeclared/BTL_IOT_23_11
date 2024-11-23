package com.example.iottest10_28_19_11.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String SHARED_PREF_NAME = "user_session";
    private static final String KEY_TOKEN = "auth_token"; // Key for storing token

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Constructor
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Lưu token vào SharedPreferences
    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token); // Lưu token vào SharedPreferences
        editor.apply();
    }

    // Lấy token từ SharedPreferences
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null); // Trả về token nếu có, nếu không thì trả về null
    }

    // Kiểm tra xem token có tồn tại không
    public boolean isLoggedIn() {
        return getToken() != null;
    }

    // Xóa token khi người dùng đăng xuất
    public void logout() {
        editor.remove(KEY_TOKEN); // Xóa token
        editor.apply();
    }
}
