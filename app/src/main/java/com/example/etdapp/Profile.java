package com.example.etdapp;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.*;

public class Profile extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    private static final String DEBUGTAG="PROFILE";
    private static final int ACTIVITY_NOTE_rea_code = 105 ;
    private  Etudiant etd = null;

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(DEBUGTAG,"onResume");
        ConnectivityManager cm =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() == null || !cm.getActiveNetworkInfo().isConnected()){
            Toast.makeText(this,"No connection",Toast.LENGTH_LONG).show();
        }else
        {
            Toast.makeText(this," connection",Toast.LENGTH_LONG).show();
        }
        //Wtask wstask = new Wtask();
        //wstask.execute("http://belatar.name/tests/","profile.php?login=test&passwd=test");
        //RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new  JsonObjectRequest(Request.Method.GET,
                "http://belatar.name/tests/profile.php?login=test&passwd=test&notes=true",
                null,this,this);

        MySingleton.getInstance(this).getRequestQueue().add(jsonObjectRequest);



    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(DEBUGTAG,error.getMessage());

    }

    @Override
    public void onResponse(JSONObject response) {

        Log.e(DEBUGTAG,response.toString());
        try{
            if(response.has("ERROR")){
                Log.e(DEBUGTAG,response.getString("ERROR"));
                //
            }
            else {
                Log.e(DEBUGTAG,"connect");
                etd=new Etudiant(response.getInt("id"),response.getString("nom"),
                        response.getString("prenom"),response.getString("phone"),response.getString("classe"));

                EditText nom =  findViewById(R.id.EtName);
                EditText prenom = findViewById(R.id.Etprenom);
                EditText classe= findViewById(R.id.Etemail);
                EditText remarks= findViewById(R.id.etremarks);

                nom.setText(etd.getNom());
                prenom.setText(etd.getPrenom());
                classe.setText(etd.getClasse());
                //remarks.setText(etd.getTele());


                final ImageView photo = findViewById(R.id.img);

                MySingleton.getInstance(this).getImageLoader()
                        .get("http://belatar.name/tests/"+response.getString("photo"),
                        new ImageLoader.ImageListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }

                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                photo.setImageBitmap(response.getBitmap());
                            }
                        }
                );
                JSONArray ary= response.getJSONArray("notes");
                int y=0;
                for(int i=0;i<ary.length();i++){
                    JSONObject j = ary.getJSONObject(i);
                    Log.e(DEBUGTAG,ary.length()+" obj :"+j.toString());
                    etd.addNote(new Notes(j.getString("label"),j.getDouble("score")));
                    y=i;
                }
                Log.e(DEBUGTAG,etd.getNotes().toString());
                ListView ls = findViewById(R.id.txtlistnotes);
                customAdapter notesArrayAdapterAdapter = new customAdapter(this,etd.getNotes());
                //Log.e(DEBUGTAG,"y="+y);
                ls.setAdapter(notesArrayAdapterAdapter);
                Log.e(DEBUGTAG,"y="+y);

                remarks.setText(ProfileDbHelper.get_instance(getApplicationContext()).getRemarq(etd.getStdId()));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void callListner(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+etd.getTele().toString()));
        startActivity(intent);
    }

    public void matListner(View view) {
        Intent intent = new Intent(this,NoteActivity.class);
        startActivityForResult(intent,ACTIVITY_NOTE_rea_code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ACTIVITY_NOTE_rea_code && resultCode==RESULT_OK)
            Log.e(DEBUGTAG,data.getStringExtra(NoteActivity.EXTRAMATIERE));
    }

    class Wtask extends AsyncTask<String,Void,Etudiant>{

        @Override
        protected Etudiant doInBackground(String... strings) {
            Etudiant etd = null;
            try {

                URL url = new URL(strings[0]+strings[1]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                InputStream in = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result="",ligne="";
                while ((ligne=reader.readLine())!=null){
                    result+= ligne;
                }
                con.disconnect();
                JSONObject json = new JSONObject(result);
                if(json.has("ERROR")){
                    Log.e(DEBUGTAG,json.getString("ERROR"));
                    //
                }
                else {
                    etd=new Etudiant(json.getInt("id"),json.getString("nom"),json.getString("prenom"),json.getString("phone"),json.getString("classe"));
                    url = new  URL(strings[0]+json.getString("photo"));
                    con = (HttpURLConnection) url.openConnection();
                    in = con.getInputStream();
                    etd.setImgae(BitmapFactory.decodeStream(in));
                    con.disconnect();
                }
                Log.d(DEBUGTAG,result);
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return etd;
        }

        @Override
        protected void onPostExecute(Etudiant etudiant) {
            super.onPostExecute(etudiant);
            if(etudiant == null){

            }
            else {
                EditText nom =  findViewById(R.id.EtName);
                EditText prenom = findViewById(R.id.Etprenom);
                EditText classe= findViewById(R.id.Etemail);
                EditText remarks= findViewById(R.id.etremarks);
                ImageView photo = findViewById(R.id.img);
                nom.setText(etudiant.getNom());
                prenom.setText(etudiant.getPrenom());
                classe.setText(etudiant.getClasse());
                remarks.setText(etudiant.getTele());
                photo.setImageBitmap(etudiant.getImgae());
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void enregistrer(View view) {
        Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show();
        EditText remarks= findViewById(R.id.etremarks);
        ProfileDbHelper.get_instance(getApplicationContext()).addRemarque(etd,remarks.getText().toString());

    }
}
