package com.example.andre.tfg_securitycams;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by andre on 08/03/2017.
 */

public class MySpinnerAdapter implements SpinnerAdapter {

    private List<ALTACAM> camaras;
    private Context context;

    public MySpinnerAdapter(List<ALTACAM> camaras, Context context) {
        this.camaras = camaras;
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.displaylistitem, viewGroup, false);

        TextView textView = (TextView) view2.findViewById(R.id.textView);
        textView.setText(camaras.get(i).getNom());

        return view2;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        return getView(i, view, viewGroup);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return camaras.size();
    }

    @Override
    public Object getItem(int i) {
        return camaras.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return camaras.size() == 0;
    }
}
