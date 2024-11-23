//package com.example.iottest10_28_19_11;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.List;
//
//public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
//    private List<Device> deviceList;
//
//    public DeviceAdapter(List<Device> deviceList) {
//        this.deviceList = deviceList;
//    }
//
//    @NonNull
//    @Override
//    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_device, parent, false);
//        return new DeviceViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
//        Device device = deviceList.get(position);
//        holder.textViewDeviceName.setText("Device: " + device.getDeviceName());
//        holder.textViewUserName.setText("User: " + device.getUserName());
//    }
//
//    @Override
//    public int getItemCount() {
//        return deviceList.size();
//    }
//
//    static class DeviceViewHolder extends RecyclerView.ViewHolder {
//        TextView textViewDeviceName, textViewUserName;
//
//        public DeviceViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textViewDeviceName = itemView.findViewById(R.id.textViewDeviceName);
//            textViewUserName = itemView.findViewById(R.id.textViewUserName);
//        }
//    }
//}
//
package com.example.iottest10_28_19_11.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iottest10_28_19_11.model.Device;
import com.example.iottest10_28_19_11.R;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> deviceList;
    private Context context; // Cần context để hiển thị AlertDialog

    public DeviceAdapter(Context context, List<Device> deviceList) {
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
                .setMessage("Choose an action for " + device.getDeviceName())
                .setPositiveButton("Option 1", (dialog, which) -> {
                    // Xử lý lựa chọn Option 1
                })
                .setNegativeButton("Option 2", (dialog, which) -> {
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
