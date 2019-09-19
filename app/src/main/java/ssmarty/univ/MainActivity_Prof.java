package ssmarty.univ;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListCommUnivAdapter;
import ssmarty.univ.database.model.InfoPresistance;
import ssmarty.univ.database.model.MessageUniv;
import ssmarty.univ.helper.DatabaseHelper;
import ssmarty.univ.network.InternetConnectionStatus;
import ssmarty.univ.notification.NotifactionDataAdded;


public class MainActivity_Prof extends AppCompatActivity {
    private ImageView btnBuildList, btnCOmmuni, btnMyList, btnHoraire;
    Intent switch_prof_acti;
    TextView displayUnivName,displayProfName,txtMsgTitre1;
//            txtMsgTitre1,txtMsgEditeur1,txtMsg1,
//            txtMsgTitre2,txtMsgEditeur2,txtMsg2,
//            txtMsgTitre3,txtMsgEditeur3,txtMsg3;
    ListView listViewMesssageUNIV;
    ProgressBar simpleProgressBar;
    SQLiteDatabase mDatabase;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<String[]> listOfMessage;
    public List<MessageUniv> listeOfMessageUniv;
    public List<String> listeOfFacDep;
    public MessageUniv myMessageUniv;
    SwipeRefreshLayout mSwipeRefreshLayout;
    InternetConnectionStatus internetConnectionStatus;
    public String nomUnivDisplayed;
//    DatabaseHelper databaseHelper =new DatabaseHelper(this);
//    //databaseHelper.insertIntoTableInfo(listFacDep);
//    String fac=databaseHelper.getAllFacDepFromLocal();
    String TAG="TAG",facCP, promoCP="";
    Boolean isACp=false;
    int count=0;
    ImageView unvLogo;
    String []getDataFromCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prof);
        btnBuildList=findViewById(R.id.imgbtn_liste);
        btnCOmmuni=findViewById(R.id.imgbtn_communi_prof);
        btnHoraire =findViewById(R.id.imgbtn_contact_univ);
        btnMyList=findViewById(R.id.imgbtn_my_listes);
        displayUnivName=findViewById(R.id.prof_txtNom_univ);
         mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        listViewMesssageUNIV= findViewById(R.id.listeViewessage_mainProf);
        //message txt
        txtMsgTitre1=findViewById(R.id.txtmsgtitre1);
//        txtMsgEditeur1=findViewById(R.id.txtmsgediteur1);
//        txtMsg1=findViewById(R.id.txtmsg1);
//        txtMsgTitre2=findViewById(R.id.txtmsgtitre2);
//        txtMsgEditeur2=findViewById(R.id.txtmsgediteur2);
//        txtMsg2=findViewById(R.id.txtmsg2);
//        txtMsgTitre3=findViewById(R.id.txtmsgtitre3);
//        txtMsgEditeur3=findViewById(R.id.txtmsgediteur3);
//        txtMsg3=findViewById(R.id.txtmsg3);
        simpleProgressBar=findViewById(R.id.simpleProgressBar);
        displayProfName=findViewById(R.id.nom_Prof);
        unvLogo=findViewById(R.id.imageViewLogoUnivProf);
        clearfield ();
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));


//        listViewMesssageUNIV.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                int topRowVerticalPosition = (listViewMesssageUNIV == null || listViewMesssageUNIV.getChildCount() == 0) ? 0 : listViewMesssageUNIV.getChildAt(0).getTop();
//                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
//            }
//        });



        //SQLITE
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);


        //todo get prof's name and uniV n
        getDataFromCard = getIntent().getStringArrayExtra("data");
        displayUnivName.setText(getDataFromCard[0]);
        nomUnivDisplayed=getDataFromCard[0];
        if (getIntent().getStringExtra("facCP")!=null){
            facCP=getIntent().getStringExtra("facCP");
            promoCP=getIntent().getStringExtra("promoCP");
            displayProfName.setText(getDataFromCard[1]);
            isACp=true;

        }
        else
        {
            String getUserName= getDataFromCard[1]; //todo send username to activities
            displayProfName.setText(getUserName);
        }


        //getMessageFromServer
        fetch3LastMessages(getDataFromCard[0]);
        displayUnivName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_contatUniv_prof.class);
                switch_prof_acti.putExtra("data_nom_univ",getDataFromCard[0]);
                startActivity(switch_prof_acti);
            }
        });

        File file = new File(getApplicationContext().getFilesDir(),getDataFromCard[0]+".png");
        if(file.exists()){
            //Do something
            //Toast.makeText(this,"file"+univName+" existe",Toast.LENGTH_LONG).show();
            //imageSaver.load();
            //loadImageFromStorage(getApplicationContext().getFilesDir().getPath(),univName);

        }
        else{
            //Nothing
            //Toast.makeText(this,"file"+univName+" No ",Toast.LENGTH_LONG).show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://ssmartyuniv.appspot.com/"+getDataFromCard[0]).child(getDataFromCard[0]+".png");

            try {
                final File localFile = File.createTempFile(getDataFromCard[0], "png");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        unvLogo.setImageBitmap(bitmap);
                        //imageSaver.save(bitmap);
                        //saveToInternalStorage(bitmap,univName);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            } catch (IOException e ) {}
        }
        unvLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_contatUniv_prof.class);
                switch_prof_acti.putExtra("data_nom_univ",getDataFromCard[0]);
                startActivity(switch_prof_acti);
            }
        });


        btnBuildList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper =new DatabaseHelper(getApplicationContext());
                //databaseHelper.insertIntoTableInfo(listFacDep);
                if (!isACp){
                String fac=databaseHelper.getAllFacDepFromLocal();
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_liste_presence.class);
                switch_prof_acti.putExtra("data_nom_user",getDataFromCard[1]);
                switch_prof_acti.putExtra("data_nom_univ",getDataFromCard[0]);
                switch_prof_acti.putExtra("list_fac",fac);
                    switch_prof_acti.putExtra("promoCP","1");
                startActivity(switch_prof_acti);}
                else if (isACp) {
                    //String fac=databaseHelper.getAllFacDepFromLocal();
                    switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_liste_presence.class);
                    switch_prof_acti.putExtra("data_nom_user",getDataFromCard[1]);
                    switch_prof_acti.putExtra("data_nom_univ",getDataFromCard[0]);
                    switch_prof_acti.putExtra("list_fac",facCP);
                    switch_prof_acti.putExtra("promoCP","01");
                    switch_prof_acti.putExtra("promoCP1",promoCP);
                    startActivity(switch_prof_acti);

                }

            }
        });
        btnCOmmuni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper =new DatabaseHelper(getApplicationContext());
                //databaseHelper.insertIntoTableInfo(listFacDep);
                if (!isACp) {
                    String fac = databaseHelper.getAllFacDepFromLocal();
                    switch_prof_acti = new Intent(MainActivity_Prof.this, Activity_Communi_Prof.class);
                    switch_prof_acti.putExtra("data_nom_user", getDataFromCard[1]);
                    switch_prof_acti.putExtra("data_nom_univ", getDataFromCard[0]);
                    switch_prof_acti.putExtra("list_fac", fac);
                    switch_prof_acti.putExtra("promoCP","1");
                    startActivity(switch_prof_acti);
                }
                else if (isACp){
                    switch_prof_acti = new Intent(MainActivity_Prof.this, Activity_Communi_Prof.class);
                    switch_prof_acti.putExtra("data_nom_user", getDataFromCard[1]);
                    switch_prof_acti.putExtra("data_nom_univ", getDataFromCard[0]);
                    switch_prof_acti.putExtra("list_fac", facCP);
                    switch_prof_acti.putExtra("facCP", facCP);
                    switch_prof_acti.putExtra("promoCP","01");
                    switch_prof_acti.putExtra("promoCP1",promoCP);
                    startActivity(switch_prof_acti);

                }

            }
        });
        btnMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_MyList_prof.class);
                switch_prof_acti.putExtra("data",getDataFromCard[1]);
                switch_prof_acti.putExtra("data_nom_univ",getDataFromCard[0]);
                startActivity(switch_prof_acti);

            }
        });
        btnHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isACp) {
                    DatabaseHelper databaseHelper =new DatabaseHelper(getApplicationContext());
                    String fac=databaseHelper.getAllFacDepFromLocal();
                    switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_ajouts_horaire_prof.class);
                    switch_prof_acti.putExtra("data_nom_user",getDataFromCard[1]);
                    switch_prof_acti.putExtra("data_nom_univ",getDataFromCard[0]);
                    switch_prof_acti.putExtra("list_fac",fac);
                    switch_prof_acti.putExtra("promoCP","1");
                    startActivity(switch_prof_acti);
                }
                else  if (isACp){

                    switch_prof_acti = new Intent(MainActivity_Prof.this, Activity_ajouts_horaire_prof.class);
                    switch_prof_acti.putExtra("data_nom_userdata_nom_user", getDataFromCard[1]);
                    switch_prof_acti.putExtra("data_nom_univ", getDataFromCard[0]);
                    switch_prof_acti.putExtra("list_fac", facCP);
                    switch_prof_acti.putExtra("facCP", facCP);
                    switch_prof_acti.putExtra("promoCP","01");
                    switch_prof_acti.putExtra("promoCP1",promoCP);
                    startActivity(switch_prof_acti);
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // shuffle();
                //getAllMessage(getDataFromCard[0].toLowerCase()+" message");
                fetch3LastMessages(getDataFromCard[0]);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //fetch3LastMessages(getDataFromCard[0].toLowerCase()+" message");
        //getAllMessage(getDataFromCard[0]+" message");
        int i=0;


        // Run on background task todo save liste of all fac_dep and all message in table
        Thread t = new Thread(new Runnable() {
            public void run() {
                /*
                 * Do something
                 */
                getAllFacDepart (getDataFromCard[0]);
//                getAllFacDepart (getDataFromCard[0].toLowerCase());
            }
        });

        t.start();
    }


    public void fetch3LastMessages (String nomUniv){
       // CollectionReference univMessage = db.collection(nomUniv+" message");

        listeOfMessageUniv=new ArrayList<>();
        simpleProgressBar.setVisibility(View.VISIBLE);
        final MessageUniv messageUniv=new MessageUniv();
        ListCommUnivAdapter listCommUnivAdapter;

        db.collection(nomUniv+" message")
                .orderBy(FieldPath.documentId(), Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                messageUniv.setMessage(document.getString("Message"));
                                messageUniv.setEditeur(document.getString("Editeur"));
                                messageUniv.setTitre(document.getString("Titre"));
                                listeOfMessageUniv.add(new MessageUniv(document.getString("Titre"),
                                        document.getString("Message"),
                                        document.getString("Editeur")));
                            }

                            if (listeOfMessageUniv.isEmpty()){
                                finish();
                                Toast.makeText(getApplicationContext(),"Aucun Message",Toast.LENGTH_LONG).show();
                            }
                            ListCommUnivAdapter listCommUnivAdapter = new ListCommUnivAdapter(getApplicationContext(),
                                    R.layout.my_custum_list_communication_univ,
                                    listeOfMessageUniv);
                            listViewMesssageUNIV.setAdapter(listCommUnivAdapter);
                            simpleProgressBar.setVisibility(View.INVISIBLE);


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            simpleProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                simpleProgressBar.setVisibility(View.INVISIBLE);

            }
        });
//        db.collection(nomUniv+" message")
//                .addSnapshotListener(new OnDo)
////        db.collection(nomUniv).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void getAllMessage(final String univ) {
        listeOfMessageUniv=new ArrayList<>();
        internetConnectionStatus= new InternetConnectionStatus();

        //connction ok
        if (internetConnectionStatus.checkConnection(this)){
            simpleProgressBar.setVisibility(View.VISIBLE);
        db.collection(univ+" message")
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
//                                    MessageUniv = new MessageUniv((document.get("titre")., document.get("message").toString(),
//                                        document.get("editeur").toString());
                                    String r= (String) document.get("titre");
                                    String rr= (String)(document.get("message"));
                                    String rrr=(String) document.get("editeur");
                                    myMessageUniv=new MessageUniv(r,rr,rrr);
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                listeOfMessageUniv.add(myMessageUniv);
                                }
                            displayMessageUniv(listeOfMessageUniv);


                            //}
                        } else {
                            Log.d("TAG", "Error getting news for "+univ, task.getException());

                        }
                    }
                });
        }
        //no connection
        else{
            simpleProgressBar.setVisibility(View.INVISIBLE);
            toastResult("Vérifier connection");
            txtMsgTitre1.setText("Pas de connection ...");
            txtMsgTitre1.setTextColor(getResources().getColor(R.color.RED_nfc));
        }


    }


    public void displayMessageUniv(List<MessageUniv> listeMessage){
        simpleProgressBar.setVisibility(View.INVISIBLE);
        int j=listeMessage.size()-1;
        for(int i=0; listeMessage.size()>i;i++){
            if ((i==0)&&(listeMessage.get(j).getTitre()!=null)){
//                txtMsgTitre1.setText(listeMessage.get(j).getTitre());
//                txtMsgEditeur1.setText(listeMessage.get(j).getEditeur());
//                txtMsg1.setText(listeMessage.get(j).getMessage());
            }
            else if ((i==1)&&(listeMessage.get(j).getTitre()!=null)){
//                txtMsgTitre2.setText(listeMessage.get(j).getTitre());
//                txtMsgEditeur2.setText(listeMessage.get(j).getEditeur());
//                txtMsg2.setText(listeMessage.get(j).getMessage());
//
//                txtMsg1.setTextColor(getResources().getColor(R.color.black_nfc));
            }
            else if ((i==2)&&(listeMessage.get(j).getTitre()!=null)){
//                txtMsgTitre3.setText(listeMessage.get(j).getTitre());
//                txtMsgEditeur3.setText(listeMessage.get(j).getEditeur());
//                txtMsg3.setText(listeMessage.get(j).getMessage());
            }
            //decresing j
            j--;
        }

    }
    public  void clearfield (){
        txtMsgTitre1.setText("Communication universitaire");
//        txtMsgEditeur1.setText("");
//        txtMsg1.setText("");
//
//        txtMsgTitre2.setText("");
//        txtMsgEditeur2.setText("");
//        txtMsg2.setText("");
//
//        txtMsgTitre3.setText("");
//        txtMsgEditeur3.setText("");
//        txtMsg3.setText("");
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
                Toast.makeText(getApplicationContext(),"Liste de département téléchargée...",Toast.LENGTH_LONG).show();
                int i =4;
            }

//        mDatabase.execSQL(insertSQL, new String[]{listString});}
            catch (Exception ex){
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Votre Unisersité n'est pas en ligne, voir SSMARTY",Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        // builder.setCancelable(false);
        builder.setTitle("Deconnexion");
        builder.setMessage("Fermer votre compte ?");
        builder.setPositiveButton("OUI",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "Yes i wanna exit", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity_Prof.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("NON",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "i wanna stay on this page", Toast.LENGTH_LONG).show();
                dialog.cancel();

            }
        });
        builder.setNeutralButton("Rate us",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

            }
        });
        AlertDialog alert=builder.create();
        alert.show();
        //super.onBackPressed();
    }

    public void notification ()
    {
//         functions = require('firebase-functions');
////       db.collection(getDataFromCard[0]+" message").document()
//       Object event = functions.firestore.collection(getDataFromCard[0]+" message").document()
//                .onUpdate(event =>{
//                var transactionId = event.params.transactionId
//                console.log(transactionId);  // Moj2HBrxepX5R7FonvrO
//    });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        NotifactionDataAdded notifactionDataAdded = new NotifactionDataAdded();
//        notifactionDataAdded.checkChange(nomUnivDisplayed,this);
    }
}
