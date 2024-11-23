package com.example.iottest10_28_19_11.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.iottest10_28_19_11.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Lấy token từ SharedPreferences
        String token = getSharedPreferences("app_prefs", MODE_PRIVATE).getString("auth_token", null);

        // Thiết lập Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_devices) {
                selectedFragment = new DevicesFragment();
            } else if (item.getItemId() == R.id.nav_following) {
                selectedFragment = new FollowingFragment();
            } else if (item.getItemId() == R.id.nav_pending) {
                selectedFragment = new PendingFragment();
            }
            else if (item.getItemId() == R.id.nav_my_devices) {
                selectedFragment = new MyDevicesFragment();
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }
            return true;
        });


        // Set default fragment
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }
}
