package com.example.iottest10_28_19_11.retrofit;
import com.example.iottest10_28_19_11.model.AuthResponse;
import com.example.iottest10_28_19_11.model.Device;
import com.example.iottest10_28_19_11.model.DeviceFollowDetail;
import com.example.iottest10_28_19_11.model.DeviceHistoryResponse;
import com.example.iottest10_28_19_11.model.DeviceResponse;
import com.example.iottest10_28_19_11.model.FollowRequest;
import com.example.iottest10_28_19_11.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/register")
    Call<AuthResponse> register(@Body User user);

    @POST("/login")
    Call<AuthResponse> login(@Body User user);
//    @GET("/allDevices")
//    Call<DeviceResponse> getAllDevices(@Header("Authorization") String token);
    @GET("/unfollowingDevices")
    Call<DeviceResponse> getAllDevices(@Header("Authorization") String token);
    @GET("/followingDevices")
    Call<DeviceResponse> followingDevices(@Header("Authorization") String token);
    @GET("/pendingDevices")
    Call<DeviceResponse> pendingDevices(@Header("Authorization") String token);
    @GET("/deviceFollowingHistory")
    Call<DeviceHistoryResponse> getAllDevicesHistory(@Header("Authorization") String token);
    @GET("/myDevices")
    Call<DeviceResponse> getMyDevices(@Header("Authorization") String token);
    @POST("/followDevice")
    Call<Void> followDevice(@Header("Authorization") String token, @Body FollowRequest followRequest);
    @POST("/addDevice")
    Call<DeviceResponse> addDevice(@Header("Authorization") String token, @Body Device device);
    @GET("/whoFollowThisDevice")
    Call<DeviceFollowDetail> whoFollowThisDevice(@Header("Authorization") String token, @Body Device device);
}
