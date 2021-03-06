package com.jorjaimalex.mityoal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jorjaimalex.mityoal.Cards.arrayAdapter;
import com.jorjaimalex.mityoal.Cards.tarjetas;
import com.jorjaimalex.mityoal.contactos.ContactoActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;

    private String currentUId;

    private String userProf;
    private String otroUserProf;

    private DatabaseReference usersDb;


    private ListView listView;
    private List<tarjetas> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference().child("User");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        compProf();

        rowItems = new ArrayList<tarjetas>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", String.valueOf(R.string.msg_objeto_borrado));
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                tarjetas obj = (tarjetas) dataObject;
                String userId = obj.getuId();
                usersDb.child(userId).child("connections").child("nope")
                        .child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, R.string.izq,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                tarjetas obj = (tarjetas) dataObject;
                String userId = obj.getuId();
                usersDb.child(userId).child("connections").child("yeps")
                        .child(currentUId).setValue(true);
                isConnectionMatch(userId);
                Toast.makeText(MainActivity.this, R.string.der,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });



        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this,
                        R.string.toast_tuto, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId)
                .child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "new Connection",
                            Toast.LENGTH_LONG).show();

                    String key = FirebaseDatabase.getInstance()
                            .getReference().child("Chat").push().getKey();

                    usersDb.child(dataSnapshot.getKey()).child("connections")
                            .child("matches").child(currentUId).child("ChatId").setValue(key);
                    usersDb.child(currentUId).child("connections").child("matches")
                            .child(dataSnapshot.getKey()).child("ChatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void compProf(){


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            //Hay que cambiarlo por profesiones
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    if (dataSnapshot.child("profB").getValue() != null
                            && dataSnapshot.child("prof").getValue() != null){

                        userProf = dataSnapshot.child("prof").getValue().toString();
                        otroUserProf = dataSnapshot.child("profB").getValue().toString();

                        compOtrasProf();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void compOtrasProf(){
        usersDb.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if (dataSnapshot.exists() && !dataSnapshot.child("connections")
                            .child("nope").hasChild(currentUId) && !dataSnapshot
                            .child("connections").child("yeps")
                            .hasChild(currentUId) && dataSnapshot
                            .child("prof").getValue().toString().equals(otroUserProf)) {

                        String profileImageUrl = "default";

                        if (!dataSnapshot.child("imageUrl").getValue().equals("default")) {

                            profileImageUrl = dataSnapshot.child("imageUrl")
                                    .getValue().toString();
                        }

                        tarjetas item = new tarjetas(dataSnapshot.getKey(),
                                dataSnapshot.child("name").getValue().toString(),
                                profileImageUrl, dataSnapshot.child("desc")
                                .getValue().toString());

                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }

                }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent i;
        if (id == R.id.bottom_app_bar_menu_salir) {
            mAuth.signOut();
            i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.bottom_app_bar_menu_ajustes) {
            i = new Intent(MainActivity.this, AñadirPerfil.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.bottom_app_bar_menu_contactos) {
            i = new Intent(MainActivity.this, ContactoActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}