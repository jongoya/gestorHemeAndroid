package com.example.gestorheme.Activities.Empleados.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.gestorheme.Activities.ColorPicker.ColorPickerActivity;
import com.example.gestorheme.Activities.EmpleadoDetail.EmpleadoDetailActivity;
import com.example.gestorheme.Activities.Empleados.Interfaces.EmpleadosListInterface;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.Common.Constants;
import com.example.gestorheme.Common.OnSwipeTouchListener;
import com.example.gestorheme.Models.Empleados.EmpleadoModel;
import com.example.gestorheme.Models.Service.ServiceModel;
import com.example.gestorheme.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpleadosListAdapter extends BaseAdapter {
    private Context contexto;
    private ArrayList<EmpleadoModel> empleados;
    private boolean showColorLayout;
    private int xDelta = 0;
    private EmpleadosListInterface delegate;

    public EmpleadosListAdapter(Context contexto, ArrayList<EmpleadoModel> empleados, boolean showColorLayout, EmpleadosListInterface delegate) {
        this.contexto = contexto;
        this.empleados = empleados;
        this.showColorLayout = showColorLayout;
        this.delegate = delegate;
    }

    @Override
    public int getCount() {
        return empleados.size();
    }

    @Override
    public Object getItem(int i) {
        return empleados.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();

        LayoutInflater mInflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.empleado_cell_layout, null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        holder.nombre = view.findViewById(R.id.nombre);
        holder.img = view.findViewById(R.id.img);
        holder.colorLayout = view.findViewById(R.id.colorLayout);
        holder.background = view.findViewById(R.id.background);
        holder.crossButton = view.findViewById(R.id.crossButton);

        EmpleadoModel empleado = empleados.get(i);


        GradientDrawable unwrappedDrawable = (GradientDrawable) AppCompatResources.getDrawable(contexto, R.drawable.color_rounded_view);
        if (showColorLayout) {
            int empleadoColor = Color.rgb((int)empleado.getRedColorValue(), (int)empleado.getGreenColorValue(), (int)empleado.getBlueColorValue());
            unwrappedDrawable.setColor(empleadoColor);
        } else {
            unwrappedDrawable.setColor(contexto.getResources().getColor(R.color.white));
        }
        holder.colorLayout.setBackground(unwrappedDrawable);

        holder.nombre.setText(empleado.getNombre() + " " + empleado.getApellidos());

        setTouchListener(holder.background, empleado);

        holder.crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmpleado(empleado);
            }
        });

        return view;
    }

    private static class ViewHolder {
        private TextView nombre;
        private ImageView img;
        private RelativeLayout colorLayout;
        private RelativeLayout background;
        private RelativeLayout crossButton;
    }

    private void setTouchListener(RelativeLayout backgroundLayout, EmpleadoModel empleado) {
        backgroundLayout.setOnTouchListener(new OnSwipeTouchListener(contexto) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if (xDelta == 0 && !showColorLayout) {
                    leftSwipeAnimation(contexto, backgroundLayout);
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                if (xDelta == 1 && !showColorLayout) {
                    rightSwipeAnimation(contexto, backgroundLayout);
                }
            }

            @Override
            public void onClick() {
                super.onClick();
                if (showColorLayout) {
                    Intent intent = new Intent(contexto, ColorPickerActivity.class);
                    intent.putExtra("empleado", empleado);
                    contexto.startActivity(intent);
                } else {
                    Intent intent = new Intent(contexto, EmpleadoDetailActivity.class);
                    intent.putExtra("empleado", empleado);
                    contexto.startActivity(intent);
                }
            }
        });
    }

    private void leftSwipeAnimation(final Context context, RelativeLayout backgroundLayout) {
        Animation slide = new TranslateAnimation(-CommonFunctions.convertToPx(xDelta, context), -CommonFunctions.convertToPx(65, context), 0, 0);
        slide.setDuration(600);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        backgroundLayout.startAnimation(slide);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                backgroundLayout.clearAnimation();
                xDelta = 1;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(backgroundLayout.getWidth(), backgroundLayout.getHeight());
                lp.setMargins(-CommonFunctions.convertToPx(55, context), CommonFunctions.convertToPx(10, context), -CommonFunctions.convertToPx(55, context), 0);
                backgroundLayout.setLayoutParams(lp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void rightSwipeAnimation(final Context context, RelativeLayout backgroundLayout) {
        Animation slide = new TranslateAnimation(0, CommonFunctions.convertToPx(65, context) , 0, 0);
        slide.setDuration(600);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        backgroundLayout.startAnimation(slide);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                backgroundLayout.clearAnimation();
                xDelta = 0;
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(backgroundLayout.getWidth(), backgroundLayout.getHeight());
                lp.setMargins(CommonFunctions.convertToPx(10, context), CommonFunctions.convertToPx(10, context), 0, 0);
                backgroundLayout.setLayoutParams(lp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void deleteEmpleado(EmpleadoModel empleado) {
        if (empleado.isEmpleadoJefe()) {
            delegate.showErrorMessage("El empleado jefe no puede ser borrado");
            return;
        }

        delegate.showLoadingState("Eliminando servicio");
        Call<EmpleadoModel> call = Constants.webServices.deleteEmpleado(empleado);
        call.enqueue(new Callback<EmpleadoModel>() {
            @Override
            public void onResponse(Call<EmpleadoModel> call, Response<EmpleadoModel> response) {
                delegate.hideLoadingState();
                if (response.code() == 200) {
                    EmpleadoModel empleadoJefe = response.body();
                    Constants.databaseManager.empleadosManager.deleteEmpleadoFromDatabase(empleado.getEmpleadoId());
                    ArrayList<ServiceModel> servicios = Constants.databaseManager.servicesManager.getServicesForEmpleadoId(empleado.getEmpleadoId());
                    for (int i = 0; i < servicios.size(); i++) {
                        ServiceModel servicio = servicios.get(i);
                        servicio.setEmpleadoId(empleadoJefe.getEmpleadoId());
                        Constants.databaseManager.servicesManager.updateServiceInDatabase(servicio);
                    }

                    delegate.reloadList();
                } else if (response.code() == Constants.logoutResponseValue) {
                    delegate.logout();
                } else {
                    delegate.showErrorMessage("Error eliminando el empleado");
                }
            }

            @Override
            public void onFailure(Call<EmpleadoModel> call, Throwable t) {
                delegate.hideLoadingState();
                delegate.showErrorMessage("Error eliminando el empleado");
            }
        });

    }


}
