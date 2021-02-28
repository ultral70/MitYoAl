package com.jorjaimalex.mityoal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth fba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Configure Google Sign In

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

    }



    public void registrarse(View view) {

        fba.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            user = fba.getCurrentUser();

                            accederApp();

                        } else {

                            Toast.makeText(LoginActivity.this,
                                    R.string.msj_no_accede
                                            + "\n" + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }

                    }
                });

        Intent i = new Intent(this, LoginActivity.class);

        startActivity(i);
    }

    public void inicio(View view) {
        Intent i = new Intent(this, LoginActivity.class);

        startActivity(i);
    }

    private void accederApp() {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        /*
        Aquí finalizamos para que no se mantenga latente la bbdd
         */
        finish();

    }

}