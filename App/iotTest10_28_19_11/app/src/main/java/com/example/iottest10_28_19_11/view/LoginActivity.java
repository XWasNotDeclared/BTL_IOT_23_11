package com.example.iottest10_28_19_11.view;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iottest10_28_19_11.firebasehandle.FCMHelper;
import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.model.AuthResponse;
import com.example.iottest10_28_19_11.model.SessionManager;
import com.example.iottest10_28_19_11.model.User;
import com.example.iottest10_28_19_11.retrofit.ApiService;
import com.example.iottest10_28_19_11.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(view -> loginUser());
        btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy FCM Token
        FCMHelper.getFCMToken(new FCMHelper.TokenCallback() {
            @Override
            public void onSuccess(String token) {
                Log.d(TAG, "FCM Token: " + token);

                // Tạo user object với token
                User user = new User(username, password, token);

                // Gọi API login
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                apiService.login(user).enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Lưu token vào SharedPreferences
                            String token = response.body().getToken(); // Giả sử token được trả về từ API
//                            getSharedPreferences("app_prefs", MODE_PRIVATE)
//                                    .edit()
//                                    .putString("auth_token", token)
//                                    .apply();
                            SessionManager sessionManager = new SessionManager(LoginActivity.this);
                            sessionManager.saveToken(token);

                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e(TAG, "Failed to get FCM Token", exception);
                Toast.makeText(LoginActivity.this, "Failed to get FCM Token!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
