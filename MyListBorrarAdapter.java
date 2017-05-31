package com.example.andre.tfg_securitycams;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by andre on 10/03/2017.
 */

public class MyListBorrarAdapter extends ArrayAdapter<ALTACAM>{

    private List<ALTACAM> listBorrarCam;

    public MyListBorrarAdapter(@NonNull Context context, List<ALTACAM> listaBorrar) {
        super(context, R.layout.borrarlistitem, listaBorrar);
        this.listBorrarCam = listaBorrar;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.borrarlistitem, parent, false);
        final TextView tvNom = (TextView) view.findViewById(R.id.tvNomCam);
        Button borrar = (Button) view.findViewById(R.id.Borrar);

        tvNom.setText(getItem(position).getNom());
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Borramos de FireBase
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final DatabaseReference camarareference = FirebaseDatabase.getInstance().getReference(user.getUid()).child(getItem(position).getNom());
                camarareference.removeValue();

                //Borramos de la lista
                listBorrarCam.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
