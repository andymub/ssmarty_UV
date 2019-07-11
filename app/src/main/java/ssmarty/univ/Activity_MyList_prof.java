package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.database.adapter.ListeEnumerationAdapter;
import ssmarty.univ.database.model.ListeEnumerationModel;
import ssmarty.univ.database.model.ListsModel;
import ssmarty.univ.helper.DatabaseHelper;

public class Activity_MyList_prof extends AppCompatActivity {
    //a List of type ListeEnumerationModel for holding list items
    List<ListeEnumerationModel> listpresence;
    private DatabaseHelper db;
    //the listview
    ListView MyListePresListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__my_list_prof);
        listpresence= new ArrayList<>();
        MyListePresListView = (ListView) findViewById(R.id.listViewMyListePres);



        //todo Get data from sqlite Datbase et display
        db = new DatabaseHelper(this);
        List<ListsModel> arraylistModel = new ArrayList<>();
        arraylistModel= db.getAllList();
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
