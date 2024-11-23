package com.example.iottest10_28_19_11.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.iottest10_28_19_11.R;
import com.example.iottest10_28_19_11.model.DeviceFollowDetail;
import java.util.List;

public class DeviceFollowAdapter extends BaseAdapter {
    private Context context;
    private List<DeviceFollowDetail> userList;

    public DeviceFollowAdapter(Context context, List<DeviceFollowDetail> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_device_user, parent, false);
        }

        DeviceFollowDetail user = userList.get(position);

        TextView username = convertView.findViewById(R.id.textViewUserName);
        TextView phone = convertView.findViewById(R.id.textViewPhone);
        TextView status = convertView.findViewById(R.id.textViewStatus);

        username.setText(user.getUsername());
        phone.setText(user.getPhone());
        status.setText(user.getStatus());

        return convertView;
    }
}
