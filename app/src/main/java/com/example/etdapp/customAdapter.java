package com.example.etdapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class customAdapter extends ArrayAdapter<Notes> {
    public customAdapter(Context context, ArrayList<Notes> items) {
        super(context, 0, items);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        Notes note= getItem(position);
        /*if (convertView == null) {*/
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.ligne, parent, false);
        //}

        TextView libelle= convertView.findViewById(R.id.id_txtScore);
        TextView score= convertView.findViewById(R.id.id_txtmat);

        libelle.setText(note.getLabel());
        score.setText(note.getScore().toString());
        /*if(note.getScore()<10){
            //imglike.setImageRessource....

        }*/



        return convertView;
    }
}

