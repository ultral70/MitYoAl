package com.jorjaimalex.mityoal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth fba;
    EditText etEmail;
    EditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);

        String mail = getIntent().getStringExtra(LoginActivity.CLAVE_MAIL);

        if (!mail.isEmpty()) {

            etEmail.setText(mail);

        }

    }



    public void registrarse(View view) {

        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {

            Toast.makeText(this, R.string.toast_et_vacios, Toast.LENGTH_LONG).show();

        } else {

            /*
            Métodos que se crean siempre, para comprobar los datos de la bbdd y añadirlos
             */

            fba.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                user = fba.getCurrentUser();

                                accederApp();

                            } else {

                                Toast.makeText(SignInActivity.this,
                                        R.string.toast_msg_no_usuario
                                                + "\n" + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();

                            }

                        }
                    });

        }
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