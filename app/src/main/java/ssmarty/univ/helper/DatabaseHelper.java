package ssmarty.univ.helper;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.Activity_liste_presence;
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
            db.execSQL(ListsModel.CREATE_TABLE);
        }
        else{
//            Toast.makeText(context,"Aucune liste enregistrée",Toast.LENGTH_LONG ).show();
//            Intent intent=new Intent(context.getApplicationContext(), Activity_liste_presence.class);
//            context.startActivity(intent);
        }
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
            System.out.print(id);
            //  Toast.makeText(getApplicationContaxt, "Liste enregistrée", Toast.LENGTH_LONG).show();
            Log.e("Liste", "One row entered");
        }catch(Exception ex) {
            System.out.println("Exceptions " +ex);
            Log.e("Liste", "Erreur "+ex.getMessage());

        }


        // close db connection
        db.close();
        //return "ok";

        // return newly inserted row id
        return id;
    }

    public List<ListsModel> getAllList() {
        List<ListsModel> arraylistModel = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ListsModel.TABLE_NAME + " ORDER BY " +
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