package com.example.iottest10_28_19_11.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iottest10_28_19_11.model.AuthResponse;
import com.example.iottest10_28_19_11.model.Device;
import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.model.FollowRequest;
import com.example.iottest10_28_19_11.model.SessionManager;
import com.example.iottest10_28_19_11.retrofit.ApiService;
import com.example.iottest10_28_19_11.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingDeviceAdapter extends RecyclerView.Adapter<FollowingDeviceAdapter.DeviceViewHolder> {
    private List<Device> deviceList;
    private Context context; // Cần context để hiển thị AlertDialog

    public FollowingDeviceAdapter(Context context, List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = deviceList.get(position);
        holder.textViewDeviceName.setText("Device: " + device.getDeviceName());
        holder.textViewUserName.setText("User: " + device.getUserName());
        // Sự kiện click trên item
        holder.itemView.setOnClickListener(v -> showPopup(device));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    // Hiển thị AlertDialog
    private void showPopup(Device device) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Device Options")
                .setMessage("Follow to " + device.getDeviceName())
                .setPositiveButton("NO", (dialog, which) -> {
                    // Xử lý lựa chọn Option 1
                })
                .setNegativeButton("YES", (dialog, which) -> {
                    // Xử lý lựa chọn Option 2

                    followDevice(device);
                })
                .setNeutralButton("Cancel", (dialog, which) -> {
                    // Đóng dialog
                    dialog.dismiss();
                })
                .create()
                .show();
    }
    private void followDevice(Device device) {
        // Get the token (you need to retrieve the token from shared preferences or other storage)
        SessionManager sessionManager = new SessionManager(context.getApplicationContext());
        String token = "Bearer " + sessionManager.getToken(); // Lấy token từ SharedPreferences

        // Create an instance of ApiService
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class); // Assuming you have RetrofitClient setup
        FollowRequest followRequest = new FollowRequest(device.getDeviceId());
        // Make the API call to follow the device
        apiService.followDevice(token, followRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Show success message or update the UI
                    Toast.makeText(context, "Device followed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the error (show error message)
                    Toast.makeText(context, "Failed to follow device", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure (network error, etc.)
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDeviceName, textViewUserName;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDeviceName = itemView.findViewById(R.id.textViewDeviceName);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
        }
    }
}
