package com.example.iottest10_28_19_11.view;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.model.AuthResponse;
import com.example.iottest10_28_19_11.model.User;
import com.example.iottest10_28_19_11.retrofit.ApiService;
import com.example.iottest10_28_19_11.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword, edtPhone, edtName;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhoneNumber);
        edtName = findViewById(R.id.edtName);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String phoneNum = edtPhone.getText().toString();
        String FCM = "testFCM";
        String name = edtName.getText().toString();
        int age = 20;
        User userRequest = new User(username, password,phoneNum,FCM,name,age);
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.register(userRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
