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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;


public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth fba;
    private DatabaseReference user;
    private FirebaseAuth.AuthStateListener fasl;
    EditText etEmail;
    EditText etPass;
    EditText etName;
    Button btRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        etEmail = findViewById(R.id.etEmailReg);
        etPass = findViewById(R.id.etPassReg);
        etName = findViewById(R.id.etUserReg);
        btRegistrar = findViewById(R.id.btnRegistrar);

        fba = FirebaseAuth.getInstance();
        fasl = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(SignInActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                String name = etName.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(SignInActivity.this,
                            R.string.toast_et_vacios, Toast.LENGTH_SHORT).show();
                } else {
                    if (email.length() < 25 && name.length() < 15) {

                        fba.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SignInActivity.this,
                                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(SignInActivity.this,
                                            "sign up error", Toast.LENGTH_SHORT).show();
                                }else{
                                    String userId = fba.getCurrentUser().getUid();
                                    user = FirebaseDatabase.getInstance().getReference()
                                            .child("User").child(userId);
                                    Map userInfo = new HashMap<>();
                                    userInfo.put("name", name);
                                    userInfo.put("email", email);
                                    userInfo.put("prof","paro");
                                    userInfo.put("profB","paroB");
                                    userInfo.put("desc","Cosas");
                                    userInfo.put("imageUrl", "default");
                                    user.updateChildren(userInfo);
                                    accederApp();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(SignInActivity.this,
                                R.string.toast_et_muy_grandes,
                                Toast.LENGTH_SHORT).show();
                    }
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

    public void inicio(View view) {
        Intent i = new Intent(SignInActivity.this,
                LoginActivity.class);
        startActivity(i);
        finish();
    }
}