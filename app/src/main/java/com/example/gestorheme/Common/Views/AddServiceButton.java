package com.example.gestorheme.Common.Views;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gestorheme.Common.AppStyle;
import com.example.gestorheme.Common.CommonFunctions;
import com.example.gestorheme.R;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AddServiceButton extends ConstraintLayout {
    private ConstraintLayout buttonContainer;
    private ImageView plusImage;
    private TextView plusText;

    public AddServiceButton(Context context) {
        super(context);
        View.inflate(context, R.layout.add_service_button, this);
        getFields();
        customizeButton(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        lp.setMargins(0, CommonFunctions.convertToPx(25, context), 0, CommonFunctions.convertToPx(100, context));
        setLayoutParams(lp);
    }

    private void getFields() {
        buttonContainer = findViewById(R.id.buttonContainer);
        plusImage = findViewById(R.id.plusButton);
        plusText = findViewById(R.id.plusText);
    }

    private void customizeButton(Context context) {
        CommonFunctions.customizeViewWithImage(context, buttonContainer, plusImage, AppStyle.getPrimaryColor(), AppStyle.getPrimaryColor());
        plusText.setTextColor(AppStyle.getPrimaryColor());
    }
}
