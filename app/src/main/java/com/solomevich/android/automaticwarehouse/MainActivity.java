package com.solomevich.android.automaticwarehouse;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton b1, b2, b3, b4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        b1 = (RadioButton) findViewById(R.id.radio_2);
        b2 = (RadioButton) findViewById(R.id.radio_3);
        b3 = (RadioButton) findViewById(R.id.radio_4);
        b4 = (RadioButton) findViewById(R.id.radio_5);

    }

    public void onRadioButtonClicked(View view) {
        Intent intent = new Intent(MainActivity.this, Main.class);

        if (b1.isChecked()) {
            intent.putExtra("count", "2");

        }
        if (b2.isChecked()) {
            intent.putExtra("count", "3");
        }
        if (b3.isChecked()) {

            intent.putExtra("count", "4");
        }
        if (b4.isChecked()) {
            intent.putExtra("count", "5");
        }

               startActivity(intent);
    }

}
