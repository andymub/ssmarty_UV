package ssmarty.univ.helper;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.database.model.InfoPresistance;
import ssmarty.univ.database.model.ListsModel;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static String DATABASE_NAME = "ssmarty_univ";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }




    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        File dbtest = new File("/data/data/ssmarty.univ/databases/ssmarty.univ");
        if (dbtest.exists()){
            try {db.execSQL(ListsModel.CREATE_TABLE);
            db.execSQL(InfoPresistance.CREATE_TABLE_INFO);}
            catch (Exception ex){

            }
        }
        else{
//            Toast.makeText(context,"Aucune liste enregistrée",Toast.LENGTH_LONG ).show();
//            Intent intent=new Intent(context.getApplicationContext(), Activity_liste_presence.class);
//            context.startActivity(intent);
        }
    }

    //Insert into table INFO
    public void insertIntoTableInfo(List<String> listFacDe){
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        String listFacDep="";
        for (String s : listFacDe)
        {
            listFacDep += s + "|";
        }
        try{
            ContentValues newValues = new ContentValues();
            newValues.put("LISTE_FACDEP", listFacDep);
            db.insert(InfoPresistance.TABLE_NAME_INFO, null, newValues);
            //id=7878878;

        }catch (Exception ex){
            db.close();
        }
        db.close();

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ListsModel.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    public long insert(String nomEtDateEditeur, String typeEtIntutile, String listeOfStudents, String etatDeList) {
        long id = 0;
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        // `id` and `timestamp` will be inserted automatically.
//        // no need to add them
//        values.put(Note.COLUMN_NOTE, note);
        try {
            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("N0M_DATE", nomEtDateEditeur);
            newValues.put("TYPE_INTITULE", typeEtIntutile);
            newValues.put("LISTE", listeOfStudents);
            newValues.put("ETAT", etatDeList);
            // Insert the row into your table
            //db = dbHelper.getWritableDatabase();
            // insert row
            id = db.insert(ListsModel.TABLE_NAME, null, newValues);
            //System.out.print(id);
            //  Toast.makeText(getApplicationContaxt, "Liste enregistrée", Toast.LENGTH_LONG).show();
            Log.e("Liste", "One row entered");
        }catch(Exception ex) {
          //  System.out.println("Exceptions " +ex);
            Log.e("Liste", "Erreur "+ex.getMessage());

        }


        // close db connection
        db.close();
        //return "ok";

        // return newly inserted row id
        return id;
    }
    public String getAllFacDepFromLocal(){
        List<InfoPresistance> arrayListFac=new ArrayList<>();
        Cursor cursor = new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int i) {
                return false;
            }

            @Override
            public boolean moveToPosition(int i) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String s) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int i) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int i) {
                return new byte[0];
            }

            @Override
            public String getString(int i) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {

            }

            @Override
            public short getShort(int i) {
                return 0;
            }

            @Override
            public int getInt(int i) {
                return 0;
            }

            @Override
            public long getLong(int i) {
                return 0;
            }

            @Override
            public float getFloat(int i) {
                return 0;
            }

            @Override
            public double getDouble(int i) {
                return 0;
            }

            @Override
            public int getType(int i) {
                return 0;
            }

            @Override
            public boolean isNull(int i) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver contentObserver) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver contentObserver) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void setNotificationUri(ContentResolver contentResolver, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public void setExtras(Bundle bundle) {

            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle bundle) {
                return null;
            }
        };
        // Select All Query
        String selectQuery = "SELECT  * FROM " + InfoPresistance.TABLE_NAME_INFO;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
             cursor = db.rawQuery(selectQuery, null);

        }catch (Exception ex){
            int i;
        }

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                InfoPresistance infoPresistance = new InfoPresistance();
                infoPresistance.setId(cursor.getInt(cursor.getColumnIndex(InfoPresistance.COLUMN_ID)));
                infoPresistance.setFacEnum(cursor.getString(cursor.getColumnIndex(InfoPresistance.COLUMN_FAC)));
                arrayListFac.add(infoPresistance);
            } while (cursor.moveToNext());
        }

        String d="";//arrayListFac.get(0).getListeFacDep().toString();
        return "ezez|zezzzze";
    }

    public List<ListsModel> getAllList(String nameUniv) {
        List<ListsModel> arraylistModel = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + nameUniv+ListsModel.TABLE_NAME + " ORDER BY " +
                ListsModel.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ListsModel ListsModel = new ListsModel();
                ListsModel.setId(cursor.getInt(cursor.getColumnIndex(ListsModel.COLUMN_ID)));
                ListsModel.setNom_Date(cursor.getString(cursor.getColumnIndex(ListsModel.COLUMN_N0M_DATE)));
                ListsModel.setType(cursor.getString(cursor.getColumnIndex(ListsModel.COLUMN_TYPE_INTITULE)));
                ListsModel.setListe(cursor.getString(cursor.getColumnIndex(ListsModel.COLUMN_LISTE)));
                ListsModel.setEtat(cursor.getString(cursor.getColumnIndex(ListsModel.COLUMN_ETAT)));

                arraylistModel.add(ListsModel);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return arraylistModel;
    }
}