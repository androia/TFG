package com.example.andre.tfg_securitycams;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button entrar, login;
    private EditText email, password;
    //FireBase
    private FirebaseAuth firebaseAuth;
    //Proceso a la espera
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Detección movimiento");
        alertDialog.setMessage("Se ha detectado movimiento en su 'Camara 1'.");
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();*/

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        entrar = (Button) findViewById(R.id.btnEntrar);
        login = (Button) findViewById(R.id.btnRegistrar);
        email = (EditText) findViewById(R.id.lbEmail);
        password = (EditText) findViewById(R.id.lbPassword);
        email.setText("andreaarubio@gmail.com");
        password.setText("1234567890");

        //Botón entrar
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String correo = email.getText().toString();
                final String contraseña = password.getText().toString();

                if(TextUtils.isEmpty(correo)){
                    Toast.makeText(LoginActivity.this,"Introducir un correo.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(contraseña)){
                    Toast.makeText(LoginActivity.this,"Introducir una contraseña.",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Comprobando usuario...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,VistaCamaraActivity.class);
                            startActivity(intent);
                        } else{
                            Toast.makeText(LoginActivity.this,"Error al entrar.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //Botón registrar
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String correo = email.getText().toString();
                final String contraseña = password.getText().toString();

                if(TextUtils.isEmpty(correo)){
                    Toast.makeText(LoginActivity.this,"Introducir un correo.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(contraseña)){
                    Toast.makeText(LoginActivity.this,"Introducir una contraseña.",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Registrando usuario ...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            firebaseAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(LoginActivity.this,"Usuario registrado correctamente.",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this,VistaCamaraActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else{
                            Toast.makeText(LoginActivity.this,"Error al entrar.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
