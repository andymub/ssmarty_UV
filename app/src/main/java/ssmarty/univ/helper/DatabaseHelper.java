package ssmarty.univ.helper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.database.model.listViewModel;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static String DATABASE_NAME = "ssmarty_univ";
    // Database Version
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(listViewModel.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + listViewModel.TABLE_NAME);

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
            id = db.insert(listViewModel.TABLE_NAME, null, newValues);
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

    public List<listViewModel> getAllNotes() {
        List<listViewModel> ArraylistViewModel = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + listViewModel.TABLE_NAME + " ORDER BY " +
                listViewModel.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                listViewModel listViewModel = new listViewModel();
                listViewModel.setId(cursor.getInt(cursor.getColumnIndex(listViewModel.COLUMN_ID)));
                listViewModel.setNom_Date(cursor.getString(cursor.getColumnIndex(listViewModel.COLUMN_N0M_DATE)));
                listViewModel.setType(cursor.getString(cursor.getColumnIndex(listViewModel.COLUMN_TYPE_INTITULE)));
                listViewModel.setListe(cursor.getString(cursor.getColumnIndex(listViewModel.COLUMN_LISTE)));
                listViewModel.setEtat(cursor.getString(cursor.getColumnIndex(listViewModel.COLUMN_ETAT)));

                ArraylistViewModel.add(listViewModel);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return ArraylistViewModel;
    }
}