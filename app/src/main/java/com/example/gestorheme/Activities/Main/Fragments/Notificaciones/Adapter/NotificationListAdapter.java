package com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.gestorheme.Models.Notification.NotificationModel;

import java.util.ArrayList;

public class NotificationListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NotificationModel> notifications;

    public NotificationListAdapter(Context context, ArrayList<NotificationModel> notifications) {
        this.notifications = notifications;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int i) {
        return notifications.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
