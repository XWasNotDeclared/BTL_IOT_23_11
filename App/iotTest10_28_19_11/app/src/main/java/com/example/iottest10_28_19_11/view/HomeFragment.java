package com.example.iottest10_28_19_11.view;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.adapter.DeviceAdapter;
import com.example.iottest10_28_19_11.adapter.DeviceHistoryAdapter;
import com.example.iottest10_28_19_11.model.Device;
import com.example.iottest10_28_19_11.model.DeviceHistory;
import com.example.iottest10_28_19_11.model.DeviceHistoryResponse;
import com.example.iottest10_28_19_11.model.DeviceResponse;
import com.example.iottest10_28_19_11.model.SessionManager;
import com.example.iottest10_28_19_11.retrofit.ApiService;
import com.example.iottest10_28_19_11.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewDeviceHistories;
    private DeviceHistoryAdapter deviceHistoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewDeviceHistories = view.findViewById(R.id.recyclerViewDevicesHistory);
        recyclerViewDeviceHistories.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchDevicesHistory();

        return view;
    }

    private void fetchDevicesHistory() {
        SessionManager sessionManager = new SessionManager(requireContext());
        String token = "Bearer " + sessionManager.getToken(); // Lấy token từ SharedPreferences

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.getAllDevicesHistory(token).enqueue(new Callback<DeviceHistoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceHistoryResponse> call, @NonNull Response<DeviceHistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DeviceHistory> deviceHistories = response.body().getDeviceHistories();
                    deviceHistories.sort((d1, d2) -> d2.getDate().compareTo(d1.getDate()));
                    if (deviceHistories != null && !deviceHistories.isEmpty()) {
                        deviceHistoryAdapter = new DeviceHistoryAdapter(deviceHistories);
                        recyclerViewDeviceHistories.setAdapter(deviceHistoryAdapter);
                    } else {
                        Toast.makeText(getContext(), "No devices history found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch devices history!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceHistoryResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
