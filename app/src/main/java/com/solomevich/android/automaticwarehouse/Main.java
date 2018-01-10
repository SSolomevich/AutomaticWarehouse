package com.solomevich.android.automaticwarehouse;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

/**
 * Created by 15 on 30.10.2017.
 */

public class Main extends Activity {
    public String countRobot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        requestWindowFeature(Window.FEATURE_NO_TITLE);

        countRobot = getIntent().getExtras().getString("count");
        setContentView(new GameView(this, countRobot));
    }


}
