package com.example.andre.tfg_securitycams;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VistaCamaraActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, SurfaceHolder.Callback{

    static String IP = "";
    static String PORT = "";
    static String USERNAME = "";
    static String PASSWORD = "";
    static String RTSP_URL = "";
    //static String USERNAME = "admin";
    //static String PASSWORD = "jvc";
    //final static String USERNAME = "root";
    //final static String PASSWORD = "axis";
    //static String RTSP_URL = "rtsp://" + USERNAME + ":" + PASSWORD + "@88.2.40.161:25554/ONVIF/Streaming/channels/0";
    //final static String RTSP_URL = "rtsp://" + USERNAME + ":" + PASSWORD + "@88.2.40.161:25554/onvif-media/media.amp";

    private MediaPlayer _mediaPlayer;
    private SurfaceHolder _surfaceHolder;

    private List<ALTACAM> camaras;

    private ListView listDisplay;
    private MyListDisplayAdapter myListDisplayAdapter;
    //private Spinner spinner;
    //private MySpinnerAdapter mySpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vistacamara);

        // Configure the view that renders live video.
        SurfaceView surfaceView =
                (SurfaceView) findViewById(R.id.surfaceView);
        _surfaceHolder = surfaceView.getHolder();
        _surfaceHolder.addCallback(this);
        //_surfaceHolder.setFixedSize(320, 240);

        listDisplay = (ListView) findViewById(R.id.LDisplay);
        //spinner = (Spinner) findViewById(R.id.spinner);

        camaras = new ArrayList<ALTACAM>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference camarareference = FirebaseDatabase.getInstance().getReference("USUARIOS").child(user.getUid());
        camarareference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                camaras.clear();
                Long value = dataSnapshot.getChildrenCount();
                for(DataSnapshot camaraSnapshot : dataSnapshot.getChildren()){
                    ALTACAM camera = camaraSnapshot.getValue(ALTACAM.class);
                    camaras.add(camera);
                    //Toast.makeText(VistaCamaraActivity.this,camera.toString(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(VistaCamaraActivity.this,"meeeeec" + camera.getNom(),Toast.LENGTH_SHORT).show();
                }
                MyListDisplayAdapter adapter = new MyListDisplayAdapter(VistaCamaraActivity.this, camaras);
                listDisplay.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //MySpinnerAdapter adapter = new MySpinnerAdapter(camaras, this);
        //spinner.setAdapter(adapter);

       listDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               IP = camaras.get(i).getIp();
               PORT = camaras.get(i).getPort();
               USERNAME = camaras.get(i).getUser();
               PASSWORD = camaras.get(i).getPass();
               RTSP_URL = "rtsp://" + USERNAME + ":" + PASSWORD + "@" + IP + ":" + PORT + "/ONVIF/Streaming/channels/0";
               //Toast.makeText(VistaCamaraActivity.this,RTSP_URL,Toast.LENGTH_SHORT).show();

               //Cerramos la visualización y la volvemos a cargar con el nuevo RTSP.
               _mediaPlayer = new MediaPlayer();
               _mediaPlayer.setDisplay(_surfaceHolder);
               Context context = getApplicationContext();
               Uri source = Uri.parse(RTSP_URL);

               try {
                   // Specify the IP camera's URL and auth headers.
                   _mediaPlayer.setDataSource(context, source);

                   // Begin the process of setting up a video stream.
                   _mediaPlayer.setOnPreparedListener(VistaCamaraActivity.this);
                   _mediaPlayer.prepareAsync();
               }
               catch (Exception e) {}

               _mediaPlayer.start();
               Toast.makeText(VistaCamaraActivity.this,camaras.get(i).getNom() + " CONECTADA",Toast.LENGTH_SHORT).show();
           }
       });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ajustes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.altaCam:
                Intent alta = new Intent(VistaCamaraActivity.this, AltaCamActivity.class);
                startActivity(alta);
                return true;
            case R.id.borrarCam:
                Intent borrar = new Intent(VistaCamaraActivity.this, BorrarCamActivity.class);
                startActivity(borrar);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public List<ALTACAM> getCamaras(){
        return this.camaras;
    }


    /* ---- SurfaceHolder.Callback ---- */

    @Override
    public void surfaceCreated(SurfaceHolder sh) {
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.setDisplay(_surfaceHolder);

        Context context = getApplicationContext();
        Uri source = Uri.parse(RTSP_URL);

        try {
            // Specify the IP camera's URL and auth headers.
            _mediaPlayer.setDataSource(context, source);

            // Begin the process of setting up a video stream.
            _mediaPlayer.setOnPreparedListener(this);
            _mediaPlayer.prepareAsync();
        }
        catch (Exception e) {}
    }

    @Override
    public void surfaceChanged(SurfaceHolder sh, int f, int w, int h) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder sh) {
        _mediaPlayer.release();
    }

    /* MediaPlayer.OnPreparedListener */
    @Override
    public void onPrepared(MediaPlayer mp) {
        _mediaPlayer.start();
    }
}
