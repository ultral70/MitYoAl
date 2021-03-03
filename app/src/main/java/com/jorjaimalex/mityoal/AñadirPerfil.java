package com.jorjaimalex.mityoal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jorjaimalex.mityoal.model.Perfil;

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

    String idUser;
    String usuario;
    String urlImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        fab = FirebaseAuth.getInstance();

        ivFotoD = findViewById(R.id.ivFotoD);
        etUsuario = findViewById(R.id.etUsuarioP);

        dbRef = FirebaseDatabase.getInstance()
                .getReference("datos/Perfiles");
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
                    Glide.with(ivFotoD.getContext())
                            .load(selectedUri)
                            .into(ivFotoD);
                    if (map.get("profileImageUrl") != null) {
                        urlImg = map.get("profileImageUrl").toString();
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

        Map userInfo = new HashMap();
        userInfo.put("name", usuario);
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
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map userInfo = new HashMap();
                    userInfo.put("profileImageUrl", downloadUrl.toString());
                    dbRef.updateChildren(userInfo);

                    finish();
                    return;
                }
            });
        } else {
            finish();
        }
    }
}