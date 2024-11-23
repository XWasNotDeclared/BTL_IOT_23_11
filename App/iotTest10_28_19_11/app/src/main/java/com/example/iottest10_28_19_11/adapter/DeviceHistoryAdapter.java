//package com.example.iottest10_28_19_11.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.iottest10_28_19_11.R;
//import com.example.iottest10_28_19_11.model.DeviceHistory;
//
//import java.util.List;
//
//public class DeviceHistoryAdapter extends RecyclerView.Adapter<DeviceHistoryAdapter.HistoryViewHolder> {
//
//    private List<DeviceHistory> historyList;
//
//    public DeviceHistoryAdapter(List<DeviceHistory> historyList) {
//        this.historyList = historyList;
//    }
//
//    @NonNull
//    @Override
//    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_device_history, parent, false);
//        return new HistoryViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
//        DeviceHistory history = historyList.get(position);
//        holder.tvDate.setText(history.getDate());
//        holder.tvLocation.setText("Lat: " + history.getLatitude() + ", Long: " + history.getLongitude());
//        holder.tvMoreInfo.setText(history.getMoreInfo());
//    }
//
//    @Override
//    public int getItemCount() {
//        return historyList.size();
//    }
//
//    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
//        TextView tvDate, tvLocation, tvMoreInfo;
//
//        public HistoryViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvDate = itemView.findViewById(R.id.tv_date);
//            tvLocation = itemView.findViewById(R.id.tv_location);
//            tvMoreInfo = itemView.findViewById(R.id.tv_more_info);
//        }
//    }
//}
//it's work but i want moreeeeeeeeeeeeeeeeeeeeeeeeeeee
//
package com.example.iottest10_28_19_11.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.model.DeviceHistory;

import java.util.List;

public class DeviceHistoryAdapter extends RecyclerView.Adapter<DeviceHistoryAdapter.HistoryViewHolder> {

    private List<DeviceHistory> historyList;

    public DeviceHistoryAdapter(List<DeviceHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        DeviceHistory history = historyList.get(position);
        holder.tvDeviceName.setText(history.getDeviceName());
        holder.tvOwnerDeivce.setText(history.getDeviceOwnerUsername());
        holder.tvDate.setText(history.getDate());
        holder.tvLocation.setText("Lat: " + history.getLatitude() + ", Long: " + history.getLongitude());
        holder.tvMoreInfo.setText("Test this is more_infor holder");
        holder.tvMoreInfo.setText(history.getMoreInfo());

        // Set OnClickListener on item to open Google Maps
        holder.itemView.setOnClickListener(v -> {
            double latitude = Double.parseDouble(history.getLatitude());
            double longitude = Double.parseDouble(history.getLongitude());

            // Create the Google Maps URL
            String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");

            // Check if Google Maps is installed
            if (intent.resolveActivity(v.getContext().getPackageManager()) != null) {
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvLocation, tvMoreInfo, tvDeviceName, tvOwnerDeivce;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvMoreInfo = itemView.findViewById(R.id.tv_more_info);
            tvDeviceName = itemView.findViewById(R.id.tv_device_name);
            tvOwnerDeivce = itemView.findViewById(R.id.tv_owner_device);
        }
    }
}
