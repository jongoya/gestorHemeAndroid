package com.example.gestorheme.Common.Views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.R;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AddServiceButton extends ConstraintLayout {

    public AddServiceButton(Context context) {
        super(context);
        View.inflate(context, R.layout.add_service_button, this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        lp.setMargins(0, CommonFunctions.convertToPx(25, context), 0, CommonFunctions.convertToPx(100, context));
        setLayoutParams(lp);
    }
}
