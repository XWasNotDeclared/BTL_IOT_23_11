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
import com.example.iottest10_28_19_11.model.Device;
import com.example.iottest10_28_19_11.model.DeviceResponse;
import com.example.iottest10_28_19_11.model.SessionManager;
import com.example.iottest10_28_19_11.retrofit.ApiService;
import com.example.iottest10_28_19_11.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class PendingFragment extends Fragment {
    private RecyclerView recyclerViewDevices;
    private DeviceAdapter deviceAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);

        recyclerViewDevices = view.findViewById(R.id.recyclerViewDevices);
        recyclerViewDevices.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchPendingDevices();

        return view;
    }

    private void fetchPendingDevices() {
        SessionManager sessionManager = new SessionManager(requireContext());
        String token = "Bearer " + sessionManager.getToken(); // Lấy token từ SharedPreferences

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.pendingDevices(token).enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceResponse> call, @NonNull Response<DeviceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Device> devices = response.body().getDevices();
                    if (devices != null && !devices.isEmpty()) {
                        deviceAdapter = new DeviceAdapter(getContext(),devices);
                        recyclerViewDevices.setAdapter(deviceAdapter);
                    } else {
                        Toast.makeText(getContext(), "No devices found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch devices!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
