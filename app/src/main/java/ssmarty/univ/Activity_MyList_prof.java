package ssmarty.univ;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ssmarty.univ.database.adapter.ListeEnumerationAdapter;
import ssmarty.univ.database.model.ListeEnumerationModel;
import ssmarty.univ.database.model.ListsModel;
import ssmarty.univ.helper.DatabaseHelper;

import static ssmarty.univ.Activity_liste_presence.MIME_TEXT_PLAIN;

public class Activity_MyList_prof extends AppCompatActivity {
    //a List of type ListeEnumerationModel for holding list items
    List<ListeEnumerationModel> listpresence;
    private DatabaseHelper db;
    //the listview
    ListView MyListePresListView;
    public String getIntentUserName;
    public  String getIntentNomUniv ;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my_list_prof);
        listpresence= new ArrayList<>();
        MyListePresListView = (ListView) findViewById(R.id.listViewMyListePres);

        //final String getIntentNomUniv ;//= getIntent().getStringExtra("nom_univ");
        //final String[] getIntentUserName;
        try {
            getIntentUserName = getIntent().getStringExtra("data");
            getIntentNomUniv = getIntent().getStringExtra("data_nom_univ");

            if ((getIntentNomUniv=="") || (getIntentNomUniv==null))
            {
                finish();
                //Intent intentGoTOPresenceListe =new Intent(this, MainActivity_Prof.class);
                Toast toast= Toast.makeText(getApplicationContext(),"Aucune liste enregistrée",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.END, 0, 0);
                LinearLayout toastContentView = (LinearLayout) toast.getView();
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.ic_empty_list_box);
                toastContentView.addView(imageView, 0);
                toast.show();
            }

        }
        catch (Exception ex)
        {
           // startActivity(intentGoTOPresenceListe);
        }

        //todo Get data from sqlite Datbase et display
        db = new DatabaseHelper(this);
        List<ListsModel> arraylistModel = new ArrayList<>();
      //  try {

            arraylistModel= db.getAllList(getIntentNomUniv);
//        }
//        catch (Exception ex)
//        {
//            super.onBackPressed();
//            Toast.makeText(getApplicationContext(),"Aucune liste enregistrée",Toast.LENGTH_LONG).show();
//        }
        if (arraylistModel.isEmpty()) {
            //adding some values to our list
            String [] Myliste =new String[]{"Etudiant(e) 0","Etudiant(e) 1","Etudiant(e) 2","Etudiant(e) 3","Etudiant(e)"};
            listpresence.add(new ListeEnumerationModel("Aucune Liste", " ", " ", Myliste, "non"));
          //  listpresence.add(new ListeEnumerationModel("prof M - Date : 06 / 07 / 2019 , 07:28", "En cours/INTRO GEO/L1", "95 Présents", Myliste, "oui"));

        }
        else{
            for (ListsModel listsModel: arraylistModel){
                String nomDate,intitule,nombre,etat;
                String [] Myliste;
                nomDate =listsModel.getNom_Date();
                intitule=listsModel.getType();
                Myliste=listsModel.getListe().split(",");
                etat=listsModel.getEtat();
                nombre= String.valueOf(Myliste.length);
                listpresence.add(new ListeEnumerationModel(nomDate,intitule,nombre,Myliste,etat));

            }
        }
       //creating the adapter
        ListeEnumerationAdapter listeEnumerationAdapter  = new ListeEnumerationAdapter(this, R.layout.my_custom_list_prof, listpresence);

        //attaching adapter to the listview
        MyListePresListView.setAdapter(listeEnumerationAdapter);



    }


}


