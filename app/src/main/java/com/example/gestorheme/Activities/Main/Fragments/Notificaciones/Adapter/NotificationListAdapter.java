package com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.gestorheme.Activities.Main.Fragments.Notificaciones.Models.NotificationDayModel;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Models.Client.ClientModel;
import com.example.gestorheme.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NotificationDayModel> notifications;

    public NotificationListAdapter(Context context, ArrayList<NotificationDayModel> notifications) {
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
        ViewHolder holder = new ViewHolder();
        String rowType = notifications.get(i).getDayType();

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (rowType) {
            case Constants.notificationCellRowType:
                view = mInflater.inflate(R.layout.notification_cell_layout, null);
                view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                holder.titulo = view.findViewById(R.id.titulo);
                holder.descripcion = view.findViewById(R.id.descripcion);
                holder.imagen = view.findViewById(R.id.cumpleImage);
                holder.background = view.findViewById(R.id.background);
                holder.titulo.setText(getTituloForNotificationType(notifications.get(i).getNotificaciones().get(0).getType(), i));
                holder.descripcion.setText(getDescripcionForNotificationType(notifications.get(i).getNotificaciones().get(0).getType(), i));
                if (!notifications.get(i).getNotificaciones().get(0).isLeido()) {
                    CommonFunctions.selectLayout(context, holder.background, holder.imagen);
                } else {
                    CommonFunctions.unSelectLayout(context, holder.background, holder.imagen);
                }
                break;
            case Constants.notificationCellHeaderType:
                view = mInflater.inflate(R.layout.notification_cell_header, null);
                view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                holder.titulo = view.findViewById(R.id.titulo);
                holder.titulo.setText(notifications.get(i).getTituloHeader());
                break;
        }

        return view;
    }

    private static class ViewHolder {
        private TextView titulo;
        private TextView descripcion;
        private ImageView imagen;
        private RelativeLayout background;
    }

    private String getTituloForNotificationType(String type, int position) {
        switch (type) {
            case Constants.notificationCumpleañosType:
                return "¡Felicitaciones!";
            case Constants.notificationCadenciaType:
                return "¡Cadencia!";
            case Constants.notificationcajaType:
                return createDateStringForNotifcacion(notifications.get(position).getFecha());
            default:
                return createDateStringForNotifcacion(notifications.get(position).getFecha());
        }
    }

    private String getDescripcionForNotificationType(String type, int position) {
        switch (type) {
            case Constants.notificationCumpleañosType:
            if (notifications.get(position).getNotificaciones().size() == 1) {
                return "¡Hoy cumplen años " + String.valueOf(notifications.get(position).getNotificaciones().size()) + " persona, felicitalo!";
            } else {
                return "¡Hoy cumplen años " + String.valueOf(notifications.get(position).getNotificaciones().size()) + " personas, felicitalos!";
            }
            case Constants.notificationCadenciaType:
                if (notifications.get(position).getNotificaciones().size() == 1) {
                    return String.valueOf(notifications.get(position).getNotificaciones().size()) + " cliente lleva tiempo sin venir";
                } else {
                    return String.valueOf(notifications.get(position).getNotificaciones().size()) + " clientes llevan tiempo sin venir";
                }
            case Constants.notificationcajaType:
                return "¡El cierre de caja está pendiente de realizar!";
            default:
                ClientModel cliente = Constants.databaseManager.clientsManager.getClientForClientId(notifications.get(position).getNotificaciones().get(0).getClientId());
                return "Notificacion de " + cliente.getNombre() + " " + cliente.getApellidos();
        }
    }

    private String createDateStringForNotifcacion(long fecha) {
        Date date = new Date(fecha);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int año = calendar.get(Calendar.YEAR);

        return String.valueOf(dia) + " de " + new SimpleDateFormat("MMMM").format(date) + " de " + String.valueOf(año);
    }
}
