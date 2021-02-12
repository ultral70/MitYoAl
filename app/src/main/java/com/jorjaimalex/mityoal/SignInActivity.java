package com.jorjaimalex.mityoal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void registrarse(View view) {
        Intent i = new Intent(this, LoginActivity.class);

        startActivity(i);
    }

    public void inicio(View view) {
        Intent i = new Intent(this, LoginActivity.class);

        startActivity(i);
    }

}