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

    private FirebaseUser fuser;
    private FirebaseAuth fba;
    EditText etEmail;
    EditText etPass;
    EditText etUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        fba = FirebaseAuth.getInstance();
        fuser = fba.getCurrentUser();

        etEmail = findViewById(R.id.etEmailReg);
        etPass = findViewById(R.id.etPassReg);
        etUser = findViewById(R.id.etUserReg);

    }



    public void registrarse(View view) {

        String user = etUser.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty() || user.isEmpty()) {

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

                                fuser = fba.getCurrentUser();

                                fuser.getDisplayName();

                                Intent i = new Intent(SignInActivity.this, MainActivity.class);


                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(i);

                                finish();

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


}