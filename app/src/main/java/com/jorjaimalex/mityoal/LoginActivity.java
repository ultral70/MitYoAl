package com.jorjaimalex.mityoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fba;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener fasl;
    EditText etEmail;
    EditText etPass;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fba = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPass.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            R.string.toast_et_vacios,
                            Toast.LENGTH_SHORT).show();
                } else {
                    fba.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                            LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,
                                        R.string.toast_error_inicio_sesion,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                user = fba.getCurrentUser();

                                accederApp();

                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    private void accederApp() {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        /*
        Aquí finalizamos para que no se mantenga latente la bbdd
         */
        finish();

    }

    public void registro(View view) {

        Intent i = new Intent(LoginActivity.this, SignInActivity.class);

        startActivity(i);


    }

}