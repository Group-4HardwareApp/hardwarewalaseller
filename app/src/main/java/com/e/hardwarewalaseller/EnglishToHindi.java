package com.e.hardwarewalalseller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

public class EnglishToHindi extends AppCompatActivity {


    EditText et;
    Button btn;
    TextView tv;
    private static final String TAG = "EnglishToHindi";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.englishtohindi);
        btn=findViewById(R.id.btn);
        et = findViewById(R.id.etinput);
        tv= findViewById(R.id.tvoutput);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String arg=et.getText().toString();
                TranslateAPI translateAPI = new TranslateAPI(
                        Language.AUTO_DETECT,
                        Language.HINDI,
                        arg);

                translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                    @Override
                    public void onSuccess(String translatedText) {
                        Log.d(TAG, "onSuccess: "+translatedText);
                        tv.setText(translatedText);
                    }

                    @Override
                    public void onFailure(String ErrorText) {
                        Log.d(TAG, "onFailure: "+ErrorText);
                    }
                });

            }
        });




    }
}
