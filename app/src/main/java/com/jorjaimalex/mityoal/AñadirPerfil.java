package com.jorjaimalex.mityoal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jorjaimalex.mityoal.model.Perfil;

public class AñadirPerfil extends AppCompatActivity {

    public static final int RC_PHOTO_ADJ = 100;
    Uri selectedUri;
    StorageReference mFotoStorageRef;
    FirebaseAuth fab;
    FirebaseUser fuser;
    DatabaseReference dbRef;
    ValueEventListener vel;

    ImageView ivFoto;
    TextView tvUsuario;

    ImageView ivFotoD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        fab = FirebaseAuth.getInstance();
        fuser = fab.getCurrentUser();

        ivFoto = findViewById(R.id.ivFoto);
        ivFotoD = findViewById(R.id.ivFotoD);
        tvUsuario = findViewById(R.id.tvUsuario);

        dbRef = FirebaseDatabase.getInstance()
                .getReference("datos/Perfiles");
        mFotoStorageRef = FirebaseStorage.getInstance().getReference()
                .child("Fotos");
    }

    public void adjuntarFoto(View view) {
        /*abrirá un selector de archivos para ayudarnos a elegir entre cualquier imagen JPEG almacenada localmente en el dispositivo */
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Complete la acción usando"), RC_PHOTO_ADJ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_ADJ && resultCode == RESULT_OK) {
            // cargamos la imagen seleccionada en el ImageView
            selectedUri = data.getData();
            Glide.with(ivFoto.getContext())
                    .load(selectedUri)
                    .into(ivFoto);

        }
    }

    public void guardarDatos(View view) {
        final StorageReference fotoRef = mFotoStorageRef
                .child(selectedUri.getEncodedPath());
        UploadTask ut = fotoRef.putFile(selectedUri);

        Task<Uri> urlTask = ut.continueWithTask(
                new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                            throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return fotoRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String nombre = fuser.getDisplayName();

                    Uri downloadUri = task.getResult();
                    Perfil p = new Perfil(nombre,
                            downloadUri.toString());
                    dbRef.child(nombre).setValue(p);

                    /* añadimos un listener a la referencia donde hemos insertado el perfil*/
                    addDatabaseListener(nombre);
                }
            }
        });
    }

    private void addDatabaseListener(String clave) {
        if (vel == null) {
            vel = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Perfil p = snapshot.getValue(Perfil.class);
                    if (p != null) cargarPerfil(p);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            };
            dbRef.child(clave).addValueEventListener(vel);
        }
    }

    private void cargarPerfil(Perfil p) {
        tvUsuario.setText(p.getNombre());
        Glide.with(ivFotoD.getContext())
                .load(p.getUrlFoto())
                .into(ivFotoD);

        ivFoto.setImageResource(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeDatabaseListener();
    }

    private void removeDatabaseListener() {
        if (vel != null) {
            dbRef.removeEventListener(vel);
            vel = null;
        }
    }
}