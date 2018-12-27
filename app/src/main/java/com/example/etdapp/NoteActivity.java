package com.example.etdapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {

    public static final String EXTRAMATIERE = "matiere";
    public static final String EXTRANOTE = "note" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
    }

    public void addMatierListner(View view) {
        EditText cMat = findViewById(R.id.id_mat);
        EditText cNote = findViewById(R.id.id_note);
        Intent i = new Intent();
        i.putExtra(EXTRAMATIERE,cMat.getText().toString());
        i.putExtra(EXTRANOTE,Double.parseDouble(cNote.getText().toString()));
        this.setResult(RESULT_OK,i);
        this.finish();
    }
}
