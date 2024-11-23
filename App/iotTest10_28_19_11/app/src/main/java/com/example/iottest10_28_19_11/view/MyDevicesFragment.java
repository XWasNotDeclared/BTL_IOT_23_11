package com.example.iottest10_28_19_11.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.adapter.DeviceAdapter;
import com.example.iottest10_28_19_11.adapter.MyDevicesAdapter;
import com.example.iottest10_28_19_11.model.Device;
import com.example.iottest10_28_19_11.model.DeviceResponse;
import com.example.iottest10_28_19_11.model.SessionManager;
import com.example.iottest10_28_19_11.retrofit.ApiService;
import com.example.iottest10_28_19_11.retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class MyDevicesFragment extends Fragment {
    private RecyclerView recyclerViewDevices;
    private MyDevicesAdapter myDevicesAdapter;
    private Button btnAdd;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_devices, container, false);

        recyclerViewDevices = view.findViewById(R.id.recyclerViewDevices);
        recyclerViewDevices.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> showAddDeviceDialog());
        fetchDevices();

        return view;
    }
    private void showAddDeviceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_device, null);
        builder.setView(dialogView);

        EditText etDeviceName = dialogView.findViewById(R.id.etDeviceName);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        AlertDialog dialog = builder.create();

        btnSubmit.setOnClickListener(v -> {
            String deviceName = etDeviceName.getText().toString().trim();
            if (!deviceName.isEmpty()) {
                addDevice(deviceName);
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Device name cannot be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    private void addDevice(String deviceName) {
        //do somthing
        SessionManager sessionManager = new SessionManager(requireContext());
        String token = "Bearer " + sessionManager.getToken();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        Device newDevice = new Device(deviceName); // Assuming Device model has a constructor for name

        apiService.addDevice(token, newDevice).enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceResponse> call, @NonNull Response<DeviceResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Device added successfully!", Toast.LENGTH_SHORT).show();
                    fetchDevices();
                } else {
                    Toast.makeText(getContext(), "Failed to add device!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeviceResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDevices() {
        SessionManager sessionManager = new SessionManager(requireContext());
        String token = "Bearer " + sessionManager.getToken(); // Lấy token từ SharedPreferences

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.getMyDevices(token).enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeviceResponse> call, @NonNull Response<DeviceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Device> devices = response.body().getDevices();
                    if (devices != null && !devices.isEmpty()) {
                        myDevicesAdapter = new MyDevicesAdapter(getContext(),devices);
                        recyclerViewDevices.setAdapter(myDevicesAdapter);
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

