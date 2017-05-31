package com.example.andre.tfg_securitycams;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AltaCamActivity extends AppCompatActivity{

    private Button alta;
    private EditText nom, ip, port, user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altacam);

        final FirebaseUser actual_user = FirebaseAuth.getInstance().getCurrentUser();

        alta = (Button) findViewById(R.id.btnDarAlta);
        nom = (EditText) findViewById(R.id.nomCam);
        ip = (EditText) findViewById(R.id.ipCam);
        port = (EditText) findViewById(R.id.portCam);
        user = (EditText) findViewById(R.id.usuariCam);
        pass = (EditText) findViewById(R.id.contraCam);

        alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ALTACAM cam = new ALTACAM(nom.getText().toString(),ip.getText().toString(),port.getText().toString(),user.getText().toString(),pass.getText().toString());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USUARIOS");
                reference.child(actual_user.getUid()).child(nom.getText().toString()).setValue(cam);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                String ipSinPuntos = ip.getText().toString().replace('.', '_');
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("CAMARAS").child(ipSinPuntos).child(actual_user.getUid());
                reference2.setValue(cam);

                Toast.makeText(AltaCamActivity.this,"Cámara dada de alta correctamente.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
