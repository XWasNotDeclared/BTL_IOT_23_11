//package com.example.iottest10_28_19_11.view;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ListView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.iottest10_28_19_11.R;
//import com.example.iottest10_28_19_11.model.DeviceFollowDetail;
//import com.example.iottest10_28_19_11.adapter.DeviceFollowAdapter;
//import java.util.ArrayList;
//
//public class DeviceUserActivity extends AppCompatActivity {
//    private ListView listView;
//    private ArrayList<DeviceFollowDetail> userList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_device_user);
//
//        listView = findViewById(R.id.listViewUsers);
//        userList = new ArrayList<>();
//
//        // Nhận dữ liệu từ Intent
//        String deviceName = getIntent().getStringExtra("DEVICE_ID");
//
//        // Giả sử bạn có một nguồn dữ liệu người dùng, ở đây tôi sử dụng một danh sách giả
//        ArrayList<DeviceFollowDetail> allUsers = getUserData();
//
//        // Lọc người dùng có trạng thái là active
//        for (DeviceFollowDetail user : allUsers) {
//            if (user.getStatus().equalsIgnoreCase("active")) {
//                userList.add(user);
//            }
//        }
//
//        if (userList.isEmpty()) {
//            Toast.makeText(this, "Không có người dùng active cho thiết bị " + deviceName, Toast.LENGTH_SHORT).show();
//        } else {
//            // Hiển thị người dùng trong ListView
//            DeviceFollowAdapter adapter = new DeviceFollowAdapter(this, userList);
//            listView.setAdapter(adapter);
//        }
//    }
//
//    // Giả lập dữ liệu người dùng
//    private ArrayList<DeviceFollowDetail> getUserData() {
//        ArrayList<DeviceFollowDetail> users = new ArrayList<>();
//        users.add(new DeviceFollowDetail("User1", "123456789", "active"));
//        users.add(new DeviceFollowDetail("User2", "987654321", "inactive"));
//        users.add(new DeviceFollowDetail("User3", "456789123", "active"));
//        return users;
//    }
//}
package com.example.iottest10_28_19_11.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.adapter.DeviceAdapter;
import com.example.iottest10_28_19_11.model.DeviceFollowDetail;
import com.example.iottest10_28_19_11.adapter.DeviceFollowAdapter;
import com.example.iottest10_28_19_11.model.DeviceResponse;
import com.example.iottest10_28_19_11.model.SessionManager;
import com.example.iottest10_28_19_11.retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.iottest10_28_19_11.model.Device;
import com.example.iottest10_28_19_11.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class DeviceUserActivity extends AppCompatActivity {
//    private ListView listView;
//    private ArrayList<DeviceFollowDetail> userList;
//    private ApiService apiService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_device_user);
//
//        listView = findViewById(R.id.listViewUsers);
//        userList = new ArrayList<>();
//
//        // Khởi tạo Retrofit client
////        apiService = ApiClient.getClient().create(ApiService.class);
//
//        // Nhận dữ liệu từ Intent (Lấy deviceId)
//        String deviceId = getIntent().getStringExtra("DEVICE_ID");
//
//        if (deviceId != null) {
//            // Gửi yêu cầu API lấy danh sách người dùng theo deviceId
//            Device device = new Device(deviceId);
//            getDeviceFollowers(device);
//        } else {
//            Toast.makeText(this, "Không có deviceId", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void getDeviceFollowers(Device device) {
//        SessionManager sessionManager = new SessionManager(this);
//        String token = "Bearer " + sessionManager.getToken(); // Lấy token từ SharedPreferences
//
//        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
//
//        apiService.whoFollowThisDevice(token,device).enqueue(new Callback<DeviceResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<DeviceResponse> call, @NonNull Response<DeviceResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Device> devices = response.body().getDevices();
//                    if (devices != null && !devices.isEmpty()) {
//                        deviceAdapter = new DeviceAdapter(getContext(),devices);
//                        recyclerViewDevices.setAdapter(deviceAdapter);
//                    } else {
//                        Toast.makeText(getContext(), "No devices found.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getContext(), "Failed to fetch devices!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<DeviceResponse> call, @NonNull Throwable t) {
//                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}
