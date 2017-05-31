package com.example.andre.tfg_securitycams;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BorrarCamActivity extends AppCompatActivity {

    private ListView lvBorrarCams;
    private MyListBorrarAdapter myListBorrarAdapter;
    private List<ALTACAM> camaras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrarcam);

        camaras = new ArrayList<ALTACAM>();
        lvBorrarCams = (ListView) findViewById(R.id.lvBorrarCam);
        final FirebaseUser actual_user = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference camarareference = FirebaseDatabase.getInstance().getReference(actual_user.getUid());
        camarareference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                camaras.clear();
                for (final DataSnapshot listSnapshot: dataSnapshot.getChildren()){
                    camaras.add(listSnapshot.getValue(ALTACAM.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myListBorrarAdapter = new MyListBorrarAdapter(this, camaras);
        lvBorrarCams.setAdapter(myListBorrarAdapter);
    }
}
