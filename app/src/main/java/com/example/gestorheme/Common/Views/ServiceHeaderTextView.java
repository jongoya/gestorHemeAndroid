package com.example.gestorheme.Common.Views;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;

import com.example.gestorheme.Common.CommonFunctions;

public class ServiceHeaderTextView extends androidx.appcompat.widget.AppCompatTextView {

    public ServiceHeaderTextView(Context context, String headerTitle) {
        super(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(CommonFunctions.convertToPx(10, context), CommonFunctions.convertToPx(15, context), CommonFunctions.convertToPx(10, context), CommonFunctions.convertToPx(15, context));
        setLayoutParams(lp);
        setText(headerTitle);
        setTextColor(Color.BLACK);
    }
}
