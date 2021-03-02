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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fba;
    private FirebaseUser user;
    public static final String CLAVE_MAIL = "MAIL";
    EditText etEmail;
    EditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);


        if (user != null) {

            etEmail.setText(user.getEmail());
        }

    }

    public void iniciarSesion(View view) {

        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()){

            Toast.makeText(this, R.string.toast_et_vacios, Toast.LENGTH_LONG).show();

        } else {

            fba.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        user = fba.getCurrentUser();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(i);
                        finish();

                    } else {

                        Toast.makeText(LoginActivity.this,
                                R.string.toast_msg_no_usuario,
                                Toast.LENGTH_LONG).show();

                    }

                }
            });

        }

    }

    public void registro(View view) {

        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {

            Intent i = new Intent(this, SignInActivity.class);

            startActivity(i);

        } else {

            Intent i = new Intent(this, SignInActivity.class);

            i.putExtra(CLAVE_MAIL ,email);

            startActivity(i);

        }


    }

}