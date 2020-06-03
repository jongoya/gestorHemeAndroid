package com.example.gestorheme.Activities.TextInputField;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestorheme.R;

public class TextInputFieldActivity extends AppCompatActivity {
    private TextView textInput;
    private ImageView microImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_input_field_layout);
        getFields();
        setOnClickListeners();
        getInputIntent();
        customizeEditText();
    }

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        i.putExtra("TEXTO", textInput.getText().toString());
        setResult(RESULT_OK, i);
        super.onBackPressed();
    }

    private void getFields() {
        textInput = findViewById(R.id.text_input);
        microImage = findViewById(R.id.micro_image);
    }

    private void getInputIntent() {
        textInput.setText(getIntent().getStringExtra("contenido"));
        textInput.setInputType(getIntent().getIntExtra("keyboard", InputType.TYPE_CLASS_TEXT));
    }

    private void customizeEditText() {
        textInput.requestFocus();
        showKeyboard();
        textInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    onBackPressed();
                }

                return false;
            }
        });
    }

    private void setOnClickListeners() {
        findViewById(R.id.microphone_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO reconocimiento por voz
            }
        });
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textInput.getWindowToken(), 0);
    }
}
