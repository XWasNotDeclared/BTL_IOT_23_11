package com.example.iottest10_28_19_11.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iottest10_28_19_11.model.Device;
import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.view.DeviceUserActivity;

import java.util.List;

public class MyDevicesAdapter extends RecyclerView.Adapter<MyDevicesAdapter.DeviceViewHolder> {
    private List<Device> deviceList;
    private Context context; // Cần context để hiển thị AlertDialog

    public MyDevicesAdapter(Context context, List<Device> deviceList) {
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
        builder.setTitle("Options")
                .setMessage("Xem danh sách người dùng " + device.getDeviceName())
                .setPositiveButton("Đang theo dõi", (dialog, which) -> {
                    // Xử lý lựa chọn Option 1
                    Intent intent = new Intent(context, DeviceUserActivity.class);
                    intent.putExtra("DEVICE_ID", device.getDeviceId());  // Chuyển tên thiết bị
                    context.startActivity(intent);
                })
                .setNegativeButton("Chưa chấp nhận theo dõi", (dialog, which) -> {
                    // Xử lý lựa chọn Option 2

                })
                .setNeutralButton("Cancel", (dialog, which) -> {
                    // Đóng dialog
                    dialog.dismiss();
                })
                .create()
                .show();
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
