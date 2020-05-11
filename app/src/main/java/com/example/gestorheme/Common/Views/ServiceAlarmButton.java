package com.example.gestorheme.Common.Views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.R;

public class ServiceAlarmButton extends ConstraintLayout {

    public ServiceAlarmButton(Context context) {
        super(context);
        View.inflate(context, R.layout.service_alarm_button, this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        lp.setMargins(0, CommonFunctions.convertToPx(15, context), CommonFunctions.convertToPx(10, context), CommonFunctions.convertToPx(15, context));
        setLayoutParams(lp);
    }
}
