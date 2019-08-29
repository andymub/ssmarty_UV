package ssmarty.univ;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ssmarty.univ.database.file.ListePresenceToFile;
import ssmarty.univ.helper.DatabaseHelper;


public class Activity_liste_presence extends AppCompatActivity {
    ListView myListview;
    Button Addbutton;
    String GetValue;
    ScrollView scrollListePresence;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    int value=0;
    ToneGenerator toneGen1;
    private RelativeLayout relaLayout_presence;
    String[] ListElements=new String[] {
    };

    private DatabaseHelper db;
    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter=0;
    private TextView txtExpediteurDate,messageEvolution;
    Spinner spinnerTypePresence,spinnerListFacDep,spinnerPromo;
    ImageButton btnStartList,btnSendToCloud;
    private NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    public String getListFacDetIntent;
    int i=1, stateBtnStartList =0;
   // public String DATABASE_NAME1 =  getResources().getString(R.string.database_name_sqlite);
    String receieveOk,nomDuProf, getIntentNomUniv;
    SQLiteDatabase mDatabase;

    public static final String TABLE_NAME = "Listes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_N0M_DATE = "Nom_Date";
    public static final String COLUMN_TYPE_INTITULE = "Type";
    public static final String COLUMN_LISTE = "Liste";
    public static final String COLUMN_ETAT = "Etat";
    private EditText edittxtAutreRainson,editxtIntutuleListe;
    private Intent intent;
    public  List<String> ListElementsArrayList;
    public ArrayAdapter<String> adapter;
    FileOutputStream fstream;
    public String nomFichier;
    public String downloadUrl;
    private StorageReference mStorageRef;
    //



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_presence);
        relaLayout_presence=findViewById(R.id.relaLayout_presence);
        txtExpediteurDate =findViewById(R.id.txtdate);
        spinnerTypePresence=findViewById(R.id.spinnerType);
        spinnerListFacDep=findViewById(R.id.spinnerfac);
        spinnerPromo=findViewById(R.id.spinnerpromo);
        edittxtAutreRainson=findViewById(R.id.edtxtAutrerainson);
        editxtIntutuleListe=findViewById(R.id.editxtObjet);
        btnStartList=findViewById(R.id.imgbtnStarList);
        btnSendToCloud=findViewById(R.id.ImgSendcloud);
        messageEvolution=findViewById(R.id.txtmesageEtatList);
        Addbutton = (Button) findViewById(R.id.addBtn);
        messageEvolution.setText("...");
        myListview = (ListView) findViewById(R.id.listView_presence);
        myListview.setBackgroundResource(R.color.WHITE_nfc);
        scrollListePresence = findViewById(R.id.scroll_liste_presence);
        //TODO  Get data from nf  card to feel in list


        //firebase



        // Assign FirebaseStorage instance to storageReference.
       // storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
//        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        //nfc adapter
        btnSendToCloud.setVisibility(View.INVISIBLE);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //NFC DISABLE



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "Ce support ne possède pas de techno  NFC.", Toast.LENGTH_LONG).show();
            finish();
            return; }
        if (!nfcAdapter.isEnabled()) {
            //Nfc not enabled
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        } else {
            //enabled
        }
        //handleIntent(getIntent());
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent
                (this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);

        getIntentNomUniv = getIntent().getStringExtra("data_nom_univ");



        //intent get userName
        nomDuProf= getIntent().getStringExtra("data_nom_user");
        //spinnerListeFac
        List<String> listFac;

        try {
            getListFacDetIntent= getIntent().getStringExtra("list_fac");
            getListFacDetIntent=getListFacDetIntent.replace("|",",");
            getListFacDetIntent= getListFacDetIntent.replaceFirst("","Choisir la Fac/Dép");
            String [] tabl=getListFacDetIntent.split(",");
            listFac=Arrays.asList(tabl);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listFac);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerListFacDep.setAdapter(dataAdapter);

            //SQLITE
            //opening the database
            mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
            db = new DatabaseHelper(this);
            createListeTable(getIntentNomUniv);
        }
        catch (Exception ex){
            Intent intent =new Intent(this,MainActivity.class);
            startActivity(intent);
        }

       // getListFacDetIntent= getListFacDetIntent.substring(getListFacDetIntent.indexOf(","),getListFacDetIntent.indexOf(","));


        //Todo nom prof from sqlite


        //Storing data in DB
        // get Instance of Database Adapter


        //Set Inivible scrollView
        scrollListePresence.setVisibility(View.INVISIBLE);

        //nOTICATION BEEP
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));
        adapter = new ArrayAdapter<String>
                (Activity_liste_presence.this, android.R.layout.simple_list_item_1, ListElementsArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                final TextView tv = view.findViewById(android.R.id.text1);
                if(position == ListElementsArrayList.size()-1) {
                    // Set the text color of TextView (ListView Item)
                    relaLayout_presence.setBackgroundResource(R.drawable.listview_border);
                    tv.setTextColor(getResources().getColor(R.color.bipNfc_etudiant_added));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            relaLayout_presence.setBackgroundResource(R.color.WHITE_nfc);
                        }
                    }, 250);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv.setTextColor(Color.BLACK);
                        }
                    }, 500);
                }
                // Generate ListView Item using TextView
                return view;
            }
        };

        myListview.setAdapter(adapter);

        //get date and hour
        getCurrentDate(nomDuProf);
        //spinner ste onclick
        spinnerTypePresence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==3){
                    edittxtAutreRainson.setVisibility(View.VISIBLE);
                }
                else{
                    edittxtAutreRainson.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnStartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (stateBtnStartList==0) {
                    // nfcAdapter.disableForegroundDispatch(Activity_liste_presence.this);  //TODO
                    if (checkIfEditEmpty()) {
                        if (spinnerListFacDep.getSelectedItemPosition() + 1 == 1) {
                            Toast.makeText(getApplicationContext(), "Choisir la Faculté", Toast.LENGTH_LONG).show();
                        } else if (spinnerPromo.getSelectedItemPosition() + 1 == 1) {
                            Toast.makeText(getApplicationContext(), "Choisir la promotion", Toast.LENGTH_LONG).show();
                        } else {
                            edittxtAutreRainson.setEnabled(true);
                            editxtIntutuleListe.setEnabled(true);
                            spinnerTypePresence.setClickable(true);
                            spinnerPromo.setClickable(true);
                            btnStartList.setImageResource(R.drawable.ic_done_red_24dp);
                            //set visible Scrollliste
                            scrollListePresence.setVisibility(View.INVISIBLE);
                        }
                    } else {


                        if (spinnerListFacDep.getSelectedItemPosition() + 1 == 1) {
                            Toast.makeText(getApplicationContext(), "Choisir une Faculté", Toast.LENGTH_LONG).show();
                        } else if (spinnerPromo.getSelectedItemPosition() + 1 == 1) {
                            Toast.makeText(getApplicationContext(), "Choisir la promotion", Toast.LENGTH_LONG).show();

                            //set visible Scrollliste
                            scrollListePresence.setVisibility(View.INVISIBLE);
                        } else {
                            btnSendToCloud.setVisibility(View.VISIBLE);
                            spinnerPromo.setEnabled(false);
                            spinnerListFacDep.setEnabled(false);
                            spinnerTypePresence.setEnabled(false);
                            btnStartList.setImageResource(R.drawable.ic_done_bleu_24dp);
                            messageEvolution.setText("Pour completer la liste, bipé une carte");

                            //set visible Scrollliste
                            scrollListePresence.setVisibility(View.VISIBLE);
                            edittxtAutreRainson.setEnabled(false);
                            editxtIntutuleListe.setEnabled(false);
                            spinnerTypePresence.setClickable(false);
                            spinnerPromo.setClickable(false);
                            spinnerTypePresence.setEnabled(false);
                            ncfCompatible ();
                            if (edittxtAutreRainson.getVisibility() == View.VISIBLE) {
                                edittxtAutreRainson.setEnabled(false);
                            }
                            stateBtnStartList++;

                        }
                    }

                }
                else if(stateBtnStartList==1)
                {
                    btnSendToCloud.setVisibility(View.INVISIBLE);
                    stateBtnStartList--;
                    spinnerPromo.setEnabled(true);
                    spinnerListFacDep.setEnabled(true);
                    spinnerTypePresence.setEnabled(true);
                    btnStartList.setImageResource(R.drawable.ic_done_one_24dp);
                    messageEvolution.setText("Modification en cours...");

                    //set visible Scrollliste
                    scrollListePresence.setVisibility(View.INVISIBLE);
                    edittxtAutreRainson.setEnabled(true);
                    editxtIntutuleListe.setEnabled(true);
                    spinnerTypePresence.setClickable(true);
                    spinnerPromo.setClickable(true);
                    spinnerTypePresence.setEnabled(true);
                    if (edittxtAutreRainson.getVisibility() == View.VISIBLE) {
                        edittxtAutreRainson.setEnabled(true);
                    }
                }
            }
        });

        btnSendToCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeEtINtitule;
                if (spinnerTypePresence.getSelectedItem().toString().equals("Autre"))
                {
                    typeEtINtitule=edittxtAutreRainson.getText().toString()+"/"+editxtIntutuleListe.getText().toString();
                }else{
                    typeEtINtitule=spinnerTypePresence.getSelectedItem().toString()+"/"+editxtIntutuleListe.getText().toString();;

                }
                if (i==1){

                    if (ListElementsArrayList.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"Aucune présence enregistrée",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if (!chekInternet()){
                        finish();
                    }

                    else {
                    createInternalFile();
                    btnSendToCloud.setImageResource(R.drawable.ic_cloud_done_black_24dp);
                   // messageEvolution.setText("Liste envoyéé");
                    setListMyDB(getIntentNomUniv+TABLE_NAME,txtExpediteurDate.getText().toString(),
                            spinnerListFacDep.getSelectedItem().toString()+"-"+spinnerPromo.getSelectedItem().toString()+
                                    "|"+typeEtINtitule,convertListToString(ListElementsArrayList),"oui");
                    btnSendToCloud.setEnabled(false);
                    i++;  }
                   // f=f+"";
                }
                else {
                    btnSendToCloud.setImageResource(R.drawable.ic_cloud_off_black_24dp);
                    messageEvolution.setText("Liste non envoyée sur serveur, stockéé dans mes Listes");
                    setListMyDB(getIntentNomUniv+TABLE_NAME,txtExpediteurDate.getText().toString(),
                            spinnerListFacDep.getSelectedItem().toString()+"-"+spinnerPromo.getSelectedItem().toString()+
                                    "|"+typeEtINtitule,convertListToString(ListElementsArrayList),"non");
                    btnSendToCloud.setEnabled(false);
                }


            }

        });

        btnSendToCloud.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String typeEtINtitule;
                if (spinnerTypePresence.getSelectedItem().toString().equals("Autre"))
                {
                    typeEtINtitule=edittxtAutreRainson.getText().toString()+"/"+editxtIntutuleListe.getText().toString();
                }else{
                    typeEtINtitule=spinnerTypePresence.getSelectedItem().toString()+"/"+editxtIntutuleListe.getText().toString();

                }
                btnSendToCloud.setImageResource(R.drawable.ic_cloud_off_black_24dp);
                messageEvolution.setText("Liste non envoyée sur serveur, stockéé dans mes Listes");
                setListMyDB(getIntentNomUniv+TABLE_NAME,txtExpediteurDate.getText().toString(),
                        spinnerListFacDep.getSelectedItem().toString()+"-"+spinnerPromo.getSelectedItem().toString()+
                                "|"+typeEtINtitule,convertListToString(ListElementsArrayList),"non");
                btnSendToCloud.setEnabled(false);
                return false;
            }
        });

        //LISTEvIEW add //todo to be replace with NFC LISTENER
        Addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {

                         toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        //toneGen1.release();
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (toneGen1 != null) {
                                    toneGen1.release();
                                    toneGen1 = null;
                                }
                            }

                        }, 100);

                       // r.play();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    GetValue= value+++".";
                    ListElementsArrayList.add(GetValue);
                    adapter.notifyDataSetChanged();
            }
        });

    }






    //create file and save into
    public boolean createInternalFile(){
        boolean state =false;
        final int[] i = {0};

        if (convertListToString(ListElementsArrayList)==null){
            finish();
        }
        String[] editeurDate =txtExpediteurDate.getText().toString().split("-");
        nomFichier ="Liste présence,"+editeurDate[0]+" Cours:"+
                editxtIntutuleListe.getText().toString()+".text";
        final String fac =spinnerListFacDep.getSelectedItem().toString();
        final String prom=spinnerPromo.getSelectedItem().toString();
        ListePresenceToFile listePresenceToFile =new ListePresenceToFile();
        listePresenceToFile.setType(spinnerTypePresence.getSelectedItem().toString());
        listePresenceToFile.setDate(editeurDate[1]);
        listePresenceToFile.setFac(spinnerListFacDep.getSelectedItem().toString());
        listePresenceToFile.setPromo(spinnerPromo.getSelectedItem().toString());
        listePresenceToFile.setCours(editxtIntutuleListe.getText().toString());
        listePresenceToFile.setNomeEditeur(editeurDate[0]);
        listePresenceToFile.setList(convertListToString(ListElementsArrayList));

        try {
            fstream = openFileOutput("Liste présence,"+editeurDate[0]+" Cours:"+
                    editxtIntutuleListe.getText().toString()+".text", Context.MODE_PRIVATE);
            fstream.write((listePresenceToFile.getType()+"--").getBytes());
            fstream.write((listePresenceToFile.getNomeEditeur()+"--").getBytes());
            fstream.write((listePresenceToFile.getDate()+"--").getBytes());
            fstream.write((listePresenceToFile.getCours()+"--").getBytes());
            fstream.write((listePresenceToFile.getFac()+"--").getBytes());
            fstream.write((listePresenceToFile.getPromo()+"--").getBytes());
            fstream.write((listePresenceToFile.getList()+"--").getBytes());
            fstream.close();
            Toast.makeText(getApplicationContext(), "Fichier crée",Toast.LENGTH_SHORT).show();
            //intent = new Intent(Activity_liste_presence.this,MainActivity_Prof.class);

            //upload file to serveur
            // Create a storage reference from our app

// Create a storage reference from our app
            mStorageRef = FirebaseStorage.getInstance().getReference();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy");
            String strDate = mdformat.format(calendar.getTime());

            //File files= new File()
            //File directory = this.getFilesDir();
//            File fileN = new File(directory, nomFichier);
            String path =getApplicationContext().getFilesDir().getPath();
//            Uri file = Uri.fromFile(new File("/data/data/ssmarty.univ/files/"+nomFichier));
            Uri file = Uri.fromFile(new File(path+"/"+nomFichier));
            StorageReference riversRef = mStorageRef.child(getIntentNomUniv+"/"+strDate+"/"+fac+"/"+prom+"/"+nomFichier);
            //StorageReference riversRef = mStorageRef.child("images/12/");

            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //downloadUrl = taskSnapshot.getUploadSessionUri();
                           i[0] =1;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            int o = 1;
                            Savelocal();
                            // ...
                        }
                    });
            downloadUrl=riversRef.getPath();

            if (i[0]==0){
                // Access a Cloud Firestore instance from your Activity
                String collection = getIntentNomUniv+"/"+fac+"/"+prom;
                String document ="Listes de présence";
                Map<String, Object> uploadedFile = new HashMap<>();
                uploadedFile.put(""+nomFichier,downloadUrl);
                //db.collection("12").document(document)
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // Hierarchical Data with Subcollection-Document in a Document
                //DocumentReference androidTutRef =
                db.collection(getIntentNomUniv).document(fac)
                        .collection(prom).document(document)
                        .set(uploadedFile,SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });



            }

            onBackPressed();
            //startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Exception "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return state;
    }




    //@Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Toast.makeText(this,"Result "+result,Toast.LENGTH_LONG ).show();
        }
    }


    private static final String LIST_SEPARATOR = ",";

    public static String convertListToString(List<String> stringList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : stringList) {
            stringBuilder.append(str).append(LIST_SEPARATOR);
        }

        // Remove last separator
        stringBuilder.setLength(stringBuilder.length() - LIST_SEPARATOR.length());

        return stringBuilder.toString();
    }

    public static List<String> convertStringToList(String str) {
        return Arrays.asList(str.split(LIST_SEPARATOR));
    }

    public String setListMyDB (String tableName,String nomEtDateEditeur, String typeEtIntutile, String listeOfStudents, String etatDeList){
       String rec= "Not save";
        try {
            addStudentList(tableName,nomEtDateEditeur,typeEtIntutile,listeOfStudents,etatDeList);
            rec= "saved";
        }catch (Exception ex)

        {
            Log.e("Er","Message "+ex.getMessage());
         Toast.makeText(getApplicationContext(),"Message "+ex.getMessage(),Toast.LENGTH_LONG).show();
        }

//      Long rec= db.insert(nomEtDateEditeur,typeEtIntutile,listeOfStudents,etatDeList);

        return rec.toString();
    }
    public void getCurrentDate(String nomDuProf) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy , HH:mm");
        String strDate = nomDuProf+" - Date : " + mdformat.format(calendar.getTime());
        txtExpediteurDate.setText(strDate );
    }

    //this method will validate the fields
    //dept does not need validation as it is a spinner and it cannot be empty
    public Boolean checkIfEditEmpty(){
        Boolean chk=true;
        if (editxtIntutuleListe.length()==0){
            //////////////
            editxtIntutuleListe.setHintTextColor(getResources().getColor(R.color.RED_nfc));
            chk=true;
            if(edittxtAutreRainson.getVisibility() == View.VISIBLE)
            {
                // Toast.makeText(getApplicationContext(),"c est bon2",Toast.LENGTH_LONG).show();
                //////////////

                if (edittxtAutreRainson.length()!=0){ chk=true;}
                else {chk=true;edittxtAutreRainson.setHintTextColor(getResources().getColor(R.color.RED_nfc));}

            }

        }
        else if (editxtIntutuleListe.length()!=0){
            //Toast.makeText(getApplicationContext(),"c est bon"+editxtIntutuleListe.getTextSize(),Toast.LENGTH_LONG).show();
            if(edittxtAutreRainson.getVisibility() == View.VISIBLE)
            {
               // Toast.makeText(getApplicationContext(),"c est bon2",Toast.LENGTH_LONG).show();
                    //////////////

                if (edittxtAutreRainson.length()!=0){ chk=false;}
                    else {chk=true;edittxtAutreRainson.setHintTextColor(getResources().getColor(R.color.RED_nfc));}

            }
            else{
               // Toast.makeText(getApplicationContext(),"c est bon3",Toast.LENGTH_LONG).show();
                chk=false;
            }
        }
        else{
           // Toast.makeText(getApplicationContext(),"c est pas bon",Toast.LENGTH_LONG).show();
            chk=false;
        }

        return chk;
    }
    private static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath("ssmarty_univ");
        return dbFile.exists();
    }

    //In this method we will do the create operation
    private void addStudentList(String tableName,String nomEtDateEditeur, String typeEtIntutile, String listeOfStudents, String etatDeList) {

        // DatabaseHelper DatabaseHelper=new DatabaseHelper(getApplicationContext());
        //DatabaseHelper.onCreate();
        String insertSQL = "INSERT INTO \n" +tableName+
                "(Nom_Date, Type, Liste, Etat)\n" +
                "VALUES \n" +
                "(?, ?, ?, ?);";
        //using the same method execsql for inserting values
        //this time it has two parameters
        //first is the sql string and second is the parameters that is to be binded with the query
        mDatabase.execSQL(insertSQL, new String[]{nomEtDateEditeur, typeEtIntutile, listeOfStudents, etatDeList});

        Toast.makeText(this, "sauvegarde réussie", Toast.LENGTH_SHORT).show();
    }
    private void createListeTable(String nomUniv) {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS "+ nomUniv+TABLE_NAME + "( "
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_N0M_DATE + " TEXT,"
                        + COLUMN_TYPE_INTITULE + " TEXT,"
                        + COLUMN_LISTE + " TEXT,"
                        + COLUMN_ETAT + " TEXT"+" )"
        );
        //mDatabase.close();
        //Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }


    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        //tvNFCContent.setText("NFC Content: " + text);
    }
    @Override
    protected void onNewIntent(Intent intent){
        getTagInfo(intent);
        String ez = "p";
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = action + "\n\n" + tag.toString();

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord [] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {
                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

//                            s += ("\n\nNdefMessage[" + i + "], NdefRecord[" + j + "]:\n\"" +
//                                    new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1, textEncoding) + "\"");
//
                            ez=new String(payload, langCodeLen + 1, payload.length - langCodeLen - 1,
                                    textEncoding);
                            splitPayloadChekAuth(ez);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }
        }

    }
    private void getTagInfo(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    }

    public String [] splitPayloadChekAuth(String myPayload)
    {
        String [] payloadSplited=null;
        payloadSplited =myPayload.split("/");
//                0-univ/
//               1-fac1_departement1/
//               2-G1/
//               3-2019/
//               4-1(code 1étu ..0 prof)/
//                5.nom Jean Yves Mélanie
        Boolean checkIfStudent =checkIfIDStudent(payloadSplited);
        if (checkIfStudent)
        {

            addToList(payloadSplited[5]);
        }
        else
            {

            }
        return payloadSplited;
    }

    public boolean checkIfIDStudent (String [] id){
        boolean chck=false;
        if (id[4].equals("1"))
        {
            if (getIntentNomUniv.trim().equals(id[0].trim()))
            { //unic checked
                if (spinnerListFacDep.getSelectedItem().toString().trim().equals(id[1].trim()))
                { //fac dep checked
                    if (id[2].trim().equals(spinnerPromo.getSelectedItem().toString().trim()))
                    {

                        chck=true;
                    }

                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Cette carte ne correspond pas",Toast.LENGTH_LONG).show();
        }
        return chck;
    }

    public void addToList (String Student)
    {
        try {

            if (Build.VERSION.SDK_INT <Build.VERSION_CODES.KITKAT) {


                    toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    //toneGen1.release();
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (toneGen1 != null) {
                                toneGen1.release();
                                toneGen1 = null;
                            }
                        }

                    }, 100);
            }

            // r.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        GetValue= value+++"."+Student;
        ListElementsArrayList.add(GetValue);
        adapter.notifyDataSetChanged();
        myListview.setVisibility(View.VISIBLE);
        myListview.setAdapter(adapter);
    }

    public void Savelocal(){
        String typeEtINtitule;
        if (spinnerTypePresence.getSelectedItem().toString().equals("Autre"))
        {
            typeEtINtitule=edittxtAutreRainson.getText().toString()+"/"+editxtIntutuleListe.getText().toString();
        }else{
            typeEtINtitule=spinnerTypePresence.getSelectedItem().toString()+"/"+editxtIntutuleListe.getText().toString();

        }
        btnSendToCloud.setImageResource(R.drawable.ic_cloud_off_black_24dp);
        messageEvolution.setText("Liste non envoyée sur serveur, stockéé dans mes Listes");
        setListMyDB(getIntentNomUniv+TABLE_NAME,txtExpediteurDate.getText().toString(),
                spinnerListFacDep.getSelectedItem().toString()+"-"+spinnerPromo.getSelectedItem().toString()+
                        "|"+typeEtINtitule,convertListToString(ListElementsArrayList),"non");
        btnSendToCloud.setEnabled(false);
    }

    public boolean chekInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
        {
            connected = false;
            Toast.makeText(getApplicationContext(),"Liste enregistrée",Toast.LENGTH_LONG).show();
            Savelocal();
        }
        return connected;
    }

    public void ncfCompatible (){
        NfcManager nfcManager = (NfcManager) getApplication().getSystemService(Context.NFC_SERVICE);
        NfcAdapter nfcAdapter = nfcManager.getDefaultAdapter();
        if (nfcAdapter == null) {
            // Device not compatible for NFC support
            Toast.makeText(getApplicationContext(),"Votre dispositif ne supporte pas de NFC",Toast.LENGTH_LONG).show();
        }
        else{
            if (nfcAdapter.isNdefPushEnabled()){

            }
            else if (nfcAdapter.isEnabled()){

            }
            else {
                Toast.makeText(getApplicationContext(), R.string.veuill_activE_Nfc,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            }

        }

    }
}
