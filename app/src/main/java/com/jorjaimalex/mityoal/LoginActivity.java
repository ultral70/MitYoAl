package com.jorjaimalex.mityoal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void iniciarSesion(View view) {
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }

    public void registro(View view) {
        Intent i = new Intent(this, SignInActivity.class);

        startActivity(i);
    }
}