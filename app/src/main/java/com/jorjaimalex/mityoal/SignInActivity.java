package com.jorjaimalex.mityoal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {


    FirebaseAuth fba;
    FirebaseAuth.AuthStateListener fasl;
    EditText etEmail;
    EditText etPass;
    EditText etName;
    Spinner spProf;
    Button btRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        fba = FirebaseAuth.getInstance();
        fasl = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etName = findViewById(R.id.etUserReg);

        String mail = getIntent().getStringExtra(LoginActivity.CLAVE_MAIL);
        spProf = findViewById(R.id.SP);

        ArrayList<String> opciones = new ArrayList<String>();
        opciones.add("Perro Policia");
        opciones.add("Abanicador");
        opciones.add("Sexador");
        opciones.add("Piloto");
        opciones.add("Malabarista");

        ArrayAdapter adp = new ArrayAdapter(SignInActivity.this, android.R.layout.simple_spinner_dropdown_item, opciones);

        spProf.setAdapter(adp);

        spProf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcion = (String) spProf.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sp = spProf.getSelectedItem().toString();

                if(sp == null){
                    return;
                }

                final String email = etEmail.getText().toString();
                final String password = etPass.getText().toString();
                final String name = etName.getText().toString();

                fba.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }else{
                            String userId = fba.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("prof", sp);
                            userInfo.put("profileImageUrl", "default");
                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
            }
        });
    }





    public void inicio() {
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