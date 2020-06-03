package com.example.gestorheme.Common.Views;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gestorheme.R;

public class LoadingStateView extends RelativeLayout {
    private TextView texto;
    private ProgressBar progressBar;

    public LoadingStateView(Context context, String description) {
        super(context);
        View.inflate(context, R.layout.loading_state_layout, this);
        getFields();
        setFields(description);
    }

    private void getFields() {
        texto = findViewById(R.id.texto);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setFields(String descripcion) {
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
        texto.setText(descripcion);
    }
}
