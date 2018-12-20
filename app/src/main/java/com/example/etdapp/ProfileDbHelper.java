package com.example.etdapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class ProfileDbHelper extends SQLiteOpenHelper {

    private static final int BD_VERSION = 1;
    private static final String DB_NAME = "profile.db";
    private static final String TABLE_REQ="Remarques";
    private static final String COL_ID="id";
    private static final String COL_REQ="remarque";
    private static ProfileDbHelper _instance;

    private ProfileDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, BD_VERSION);
    }

    public static ProfileDbHelper get_instance(Context c){
        if( _instance == null) _instance= new ProfileDbHelper(c);

        return _instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createReq = "create TABLE "+TABLE_REQ+"("+COL_ID+" INTEGER PRIMARY KEY,"+COL_REQ+" TEXT);";
        db.execSQL(createReq);
    }

    public void  addRemarque(Etudiant e,String text){
        ContentValues save = new ContentValues();
        save.put(COL_ID,e.getStdId());
        save.put(COL_REQ,text);
        getWritableDatabase().insertWithOnConflict(TABLE_REQ,null,save,SQLiteDatabase.CONFLICT_REPLACE);
    }
    public String getRemarq(int stdid){
        Cursor c= getReadableDatabase().query(TABLE_REQ,new String[]{COL_REQ},COL_ID+"= ?",
                new String[] {stdid+""},null,null,null);
        if(c.getCount()> 0){
            c.moveToFirst();
            return c.getString(c.getColumnIndex(COL_REQ));
        }
        return "";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.delete(TABLE_REQ,null,null);
        onCreate(db);
    }
}
