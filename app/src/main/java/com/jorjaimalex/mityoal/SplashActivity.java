package com.jorjaimalex.mityoal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img = (ImageView) findViewById(R.id.imgLetras);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        img.startAnimation(anim);

        Intent i = new Intent(this, LoginActivity.class);

        startActivity(i);
    }
}