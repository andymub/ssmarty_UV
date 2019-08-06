package ssmarty.univ;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.opencensus.stats.Aggregation;
import ssmarty.univ.database.model.InfoPresistance;
import ssmarty.univ.database.model.messageUniv;
import ssmarty.univ.helper.DatabaseHelper;
import ssmarty.univ.network.InternetConnectionStatus;


public class MainActivity_Prof extends AppCompatActivity {
    private ImageButton btnBuildList, btnCOmmuni, btnMyList, btnContactUniv;
    Intent switch_prof_acti;
    TextView displayUnivName,displayProfName,txtMsgTitre1,txtMsgEditeur1,txtMsg1,
            txtMsgTitre2,txtMsgEditeur2,txtMsg2,
            txtMsgTitre3,txtMsgEditeur3,txtMsg3;
    ProgressBar simpleProgressBar;
    SQLiteDatabase mDatabase;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<String[]> listOfMessage;
    public List<messageUniv> listeOfMessageUniv;
    public List<String> listeOfFacDep;
    public messageUniv myMessageUniv;
    SwipeRefreshLayout mSwipeRefreshLayout;
    InternetConnectionStatus internetConnectionStatus;
    public String nomUnivDisplayed;
//    DatabaseHelper databaseHelper =new DatabaseHelper(this);
//    //databaseHelper.insertIntoTableInfo(listFacDep);
//    String fac=databaseHelper.getAllFacDepFromLocal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__prof);
        btnBuildList=findViewById(R.id.imgbtn_liste);
        btnCOmmuni=findViewById(R.id.imgbtn_communi_prof);
        btnContactUniv=findViewById(R.id.imgbtn_contact_univ);
        btnMyList=findViewById(R.id.imgbtn_my_listes);
        displayUnivName=findViewById(R.id.prof_txtNom_univ);
         mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        //message txt
        txtMsgTitre1=findViewById(R.id.txtmsgtitre1);
        txtMsgEditeur1=findViewById(R.id.txtmsgediteur1);
        txtMsg1=findViewById(R.id.txtmsg1);
        txtMsgTitre2=findViewById(R.id.txtmsgtitre2);
        txtMsgEditeur2=findViewById(R.id.txtmsgediteur2);
        txtMsg2=findViewById(R.id.txtmsg2);
        txtMsgTitre3=findViewById(R.id.txtmsgtitre3);
        txtMsgEditeur3=findViewById(R.id.txtmsgediteur3);
        txtMsg3=findViewById(R.id.txtmsg3);
        simpleProgressBar=findViewById(R.id.simpleProgressBar);
        displayProfName=findViewById(R.id.nom_Prof);
        clearfield ();


        //SQLITE
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);


        //todo get prof's name and uniV n
        final String []getDataFromCard = getIntent().getStringArrayExtra("data");
        displayUnivName.setText(getDataFromCard[0]);
        nomUnivDisplayed=getDataFromCard[0];
        String getUserName= getDataFromCard[1]; //todo send username to activities
        displayProfName.setText(getUserName);

        btnBuildList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper =new DatabaseHelper(getApplicationContext());
                //databaseHelper.insertIntoTableInfo(listFacDep);
                String fac=databaseHelper.getAllFacDepFromLocal();
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_liste_presence.class);
                switch_prof_acti.putExtra("data_nom_user",getDataFromCard[1]);
                switch_prof_acti.putExtra("list_fac",fac);
                startActivity(switch_prof_acti);

            }
        });
        btnCOmmuni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper =new DatabaseHelper(getApplicationContext());
                //databaseHelper.insertIntoTableInfo(listFacDep);
                String fac=databaseHelper.getAllFacDepFromLocal();
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_Communi_Prof.class);
                switch_prof_acti.putExtra("data_nom_user",getDataFromCard[1]);
                switch_prof_acti.putExtra("list_fac",fac);
                startActivity(switch_prof_acti);

            }
        });
        btnMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_MyList_prof.class);
                switch_prof_acti.putExtra("data",getDataFromCard[1]);
                startActivity(switch_prof_acti);

            }
        });
        btnContactUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_contatUniv_prof.class);
                startActivity(switch_prof_acti);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // shuffle();
                getAllMessage(getDataFromCard[0].toLowerCase()+" message");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //fetch3LastMessages(getDataFromCard[0].toLowerCase()+" message");
        getAllMessage(getDataFromCard[0].toLowerCase()+" message");
        int i=0;


        // Run on background task todo save liste of all fac_dep and all message in table
        Thread t = new Thread(new Runnable() {
            public void run() {
                /*
                 * Do something
                 */
                getAllFacDepart (getDataFromCard[0].toLowerCase());
            }
        });

        t.start();
    }

    public void fetch3LastMessages (String nomUniv){
       // CollectionReference univMessage = db.collection(nomUniv+" message");


//        db.collection(nomUniv).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
//                String [] message = new String[3];
//                if (e != null)
//                    toastResult(e.getMessage());
//                //list.clear(); todo clear message fied prof
//                for (DocumentSnapshot doc : documentSnapshots) {
//
//                    message[0]=doc.getString(" editeur");
//                    message[1]=doc.getString("  message");
//                    message[2]=doc.getString("  titre");
//                   // listOfMessage.add(message);
//                }
//            }
//        });


    }

    public void toastResult (String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    private void getAllMessage(String univ) {
        listeOfMessageUniv=new ArrayList<>();
        internetConnectionStatus= new InternetConnectionStatus();

        //connction ok
        if (internetConnectionStatus.checkConnection(this)){
            simpleProgressBar.setVisibility(View.VISIBLE);
        db.collection("univ message")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                if ((String) document.get("titre")==null){
//                                    int d;
//                                }else
//                                {
//                                    messageUniv = new messageUniv((document.get("titre")., document.get("message").toString(),
//                                        document.get("editeur").toString());
                                    String r= (String) document.get("titre");
                                    String rr= (String)(document.get("message"));
                                    String rrr=(String) document.get("editeur");
                                    myMessageUniv=new messageUniv(r,rr,rrr);
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                listeOfMessageUniv.add(myMessageUniv);
                                }
                            displayMessageUniv(listeOfMessageUniv);


                            //}
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());

                        }
                    }
                });
        }
        //no connection
        else{
            simpleProgressBar.setVisibility(View.INVISIBLE);
            toastResult("Vérifier connection");
            txtMsg1.setText("Pas de connection ...");
            txtMsg1.setTextColor(getResources().getColor(R.color.RED_nfc));
        }


    }


    public void displayMessageUniv(List<messageUniv> listeMessage){
        simpleProgressBar.setVisibility(View.INVISIBLE);
        int j=listeMessage.size()-1;
        for(int i=0; listeMessage.size()>i;i++){
            if ((i==0)&&(listeMessage.get(j).getTitre()!=null)){
                txtMsgTitre1.setText(listeMessage.get(j).getTitre());
                txtMsgEditeur1.setText(listeMessage.get(j).getEditeur());
                txtMsg1.setText(listeMessage.get(j).getMessage());
            }
            else if ((i==1)&&(listeMessage.get(j).getTitre()!=null)){
                txtMsgTitre2.setText(listeMessage.get(j).getTitre());
                txtMsgEditeur2.setText(listeMessage.get(j).getEditeur());
                txtMsg2.setText(listeMessage.get(j).getMessage());

                txtMsg1.setTextColor(getResources().getColor(R.color.black_nfc));
            }
            else if ((i==2)&&(listeMessage.get(j).getTitre()!=null)){
                txtMsgTitre3.setText(listeMessage.get(j).getTitre());
                txtMsgEditeur3.setText(listeMessage.get(j).getEditeur());
                txtMsg3.setText(listeMessage.get(j).getMessage());
            }
            //decresing j
            j--;
        }

    }
    public  void clearfield (){
        txtMsgTitre1.setText("");
        txtMsgEditeur1.setText("");
        txtMsg1.setText("");

        txtMsgTitre2.setText("");
        txtMsgEditeur2.setText("");
        txtMsg2.setText("");

        txtMsgTitre3.setText("");
        txtMsgEditeur3.setText("");
        txtMsg3.setText("");
    }

    public void getAllFacDepart (String nonUniv){
        String [] x;
        db.collection(nonUniv).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listeOfFacDep = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        listeOfFacDep.add(document.getId());

                    }
                    Log.d("TAG", listeOfFacDep.toString());
                    createListeTable();
                    saveListFacDepSQLITE(listeOfFacDep);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void saveListFacDepSQLITE (List<String> listFacDep){
        DatabaseHelper databaseHelper =new DatabaseHelper(getApplicationContext());
       // databaseHelper.insertIntoTableInfo(listFacDep);
        String fac=databaseHelper.getAllFacDepFromLocal();
        String listString="";
//        String insertSQL = "INSERT INTO " +InfoPresistance.TABLE_NAME_INFO+ " VALUES " + "(?);";
        //using the same method execsql for inserting values
        //this time it has two parameters
        //first is the sql string and second is the parameters that is to be binded with the query
        for (String s : listFacDep)
        {
            listString += s + "|";
        }
        String insertSQL = "INSERT INTO "+InfoPresistance.TABLE_NAME_INFO
                +"(LISTE_FACDEP)\n"
                +" VALUES " +
                "(?);";
        String [] facDep;
        if ((fac=="") || (fac=="|")){
            try{
                mDatabase.execSQL(insertSQL, new String[]{listString});
                Toast.makeText(getApplicationContext(),"saved LOCAL",Toast.LENGTH_LONG).show();
                int i =4;
            }

//        mDatabase.execSQL(insertSQL, new String[]{listString});}
            catch (Exception ex){
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"TABANII",Toast.LENGTH_LONG).show();
            }
        }
        else {

            int d;
        }

        mDatabase.close();
//        facDep= fac.split("|");
//        int i= facDep.length;
    }












    ///SQLITE

    private void createListeTable() {
        try {

            mDatabase.execSQL(InfoPresistance.CREATE_TABLE_INFO);
        }catch (Exception ex){

        }
    }

    public String getNomUnivDisplayed() {
        return nomUnivDisplayed;
    }
}
