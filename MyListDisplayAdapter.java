package com.example.andre.tfg_securitycams;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class MyListDisplayAdapter  extends ArrayAdapter<ALTACAM> {

    private List<ALTACAM> listDisplayCam;

    public MyListDisplayAdapter(@NonNull Context context, List<ALTACAM> listaDisplay) {
        super(context, R.layout.displaylistitem, listaDisplay);
        this.listDisplayCam = listaDisplay;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.displaylistitem, parent, false);
        TextView tvNom = (TextView) view.findViewById(R.id.textView);

        tvNom.setText(getItem(position).getNom());

        return view;
    }
}
