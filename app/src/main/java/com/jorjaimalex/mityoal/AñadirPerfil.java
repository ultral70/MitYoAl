package com.jorjaimalex.mityoal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AñadirPerfil extends AppCompatActivity {

    Uri selectedUri;

    FirebaseAuth fab;
    DatabaseReference dbRef;

    ImageView ivFotoD;
    EditText etUsuario;
    EditText etProfesion;
    EditText etDescripcion;
    Button guardar;
    Button atras;
    String idUser;
    String usuario;
    String profesion;
    String descripcion;
    String urlImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        fab = FirebaseAuth.getInstance();

        ivFotoD = findViewById(R.id.ivFotoD);
        etUsuario = findViewById(R.id.etUsuarioP);
        etProfesion = findViewById(R.id.etProfesionP);
        etDescripcion = findViewById(R.id.etDescP);
        atras = findViewById(R.id.materialButtonVolverP);
        guardar = findViewById(R.id.materialButtonGuardarP);

        idUser = fab.getCurrentUser().getUid();

        dbRef = FirebaseDatabase.getInstance().getReference().child("User").child(idUser);


        getUserInfo();

        ivFotoD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos(view);
            }
        });
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });

    }

    private void getUserInfo() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        usuario = map.get("name").toString();
                        etUsuario.setText(usuario);
                    }
                    if (map.get("prof") != null) {
                        profesion = map.get("prof").toString();
                        etProfesion.setText(profesion);
                    }

                    if (map.get("desc") != null) {
                        descripcion = map.get("desc").toString();
                        etDescripcion.setText(descripcion);
                    }

                    Glide.with(ivFotoD.getContext())
                            .load(selectedUri)
                            .into(ivFotoD);
                    if (map.get("imageUrl") != null) {
                        urlImg = map.get("imageUrl").toString();
                        switch (urlImg) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.gente).into(ivFotoD);
                                break;
                            default:
                                Glide.with(getApplication()).load(urlImg).into(ivFotoD);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            selectedUri = imageUri;
            ivFotoD.setImageURI(selectedUri);
        }
    }

    public void guardarDatos(View view) {
        usuario = etUsuario.getText().toString();
        descripcion = etDescripcion.getText().toString();
        profesion = etProfesion.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("name", usuario);
        userInfo.put("prof", profesion);
        userInfo.put("desc", descripcion);
        dbRef.updateChildren(userInfo);
        if (selectedUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(idUser);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), selectedUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

        } else {
            finish();
        }
    }
}