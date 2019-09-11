package ssmarty.univ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class Activity_ajouts_horaire_prof extends AppCompatActivity {

    ImageButton btnAjout;
    Spinner spinnerFac, spinnerPromo,spinnerType;
    EditText nomCoursHoraire,lieuHoraire,titulaireHoraire,dateHoraire,detailsHoraire;
    List<String> listFac;
    String facDep="",promo="",typeHoraire="";
    FileOutputStream fstream;
    String getUnivName,getUser;
    private StorageReference mStorageRef;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG="TAG";
    String sss;
    ProgressBar progressBar;
    int count = 0;
    public String getListFacDetIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouts_horaire_prof);

        btnAjout=findViewById(R.id.imgbtnAjoutCours);
        spinnerFac=findViewById(R.id.spinnerfaDepAjoutCours);
        spinnerPromo= findViewById(R.id.spinnerPromoAjoutCours);
        spinnerType=findViewById(R.id.spinnerTypeAjoutCours);
        progressBar=findViewById(R.id.progressBar);
        nomCoursHoraire =findViewById(R.id.editxtNomDuCoursAjoutCours);
        lieuHoraire =findViewById(R.id.editxtLieuCoursAjoutCours);
        titulaireHoraire = findViewById(R.id.edittxtTitulaireAjoutCours);
        dateHoraire= findViewById(R.id.editxtDateCoursAjoutCours);
        detailsHoraire= findViewById(R.id.detailAjoutCours);
        //DESACTIVATE NFC
        NfcAdapter adapterNfc = NfcAdapter.getDefaultAdapter(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            adapterNfc.enableReaderMode(this, null, NfcAdapter.STATE_OFF, null);
        }
//
        sss=getIntent().getStringExtra("promoCP");
        getUnivName= getIntent().getStringExtra("data_nom_univ");
        getUser= getIntent().getStringExtra("data_nom_user");
        String getListFacDetIntent= getIntent().getStringExtra("list_fac");
        try {
            progressBar.setVisibility(View.INVISIBLE);
            if ((getIntent().getStringExtra("promoCP").equals("01"))){
                getListFacDetIntent = getIntent().getStringExtra("list_fac");
//                getListFacDetIntent = getListFacDetIntent.replace("|", ",");
//                getListFacDetIntent = getListFacDetIntent.replaceFirst("", "Choisir la Fac/Dép");
                String[] tabl = getListFacDetIntent.split(",");
                listFac = Arrays.asList(tabl);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, listFac);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFac.setAdapter(dataAdapter);
                spinnerFac.setSelection(0);
                spinnerFac.setEnabled(false);
                if (getIntent().getStringExtra("promoCP1").toUpperCase().trim().equals("PREPA")){
                    spinnerPromo.setSelection(1);
                }
                else if (getIntent().getStringExtra("promoCP1").toUpperCase().trim().equals("G1")){
                    spinnerPromo.setSelection(2);
                }else if (getIntent().getStringExtra("promoCP1").toUpperCase().trim().equals("G2")){
                    spinnerPromo.setSelection(3);
                }else if (getIntent().getStringExtra("promoCP1").toUpperCase().trim().equals("G3")){
                    spinnerPromo.setSelection(4);
                }else if (getIntent().getStringExtra("promoCP1").toUpperCase().trim().equals("L1")){
                    spinnerPromo.setSelection(5);
                }else if (getIntent().getStringExtra("promoCP1").toUpperCase().trim().equals("L2")){
                    spinnerPromo.setSelection(6);
                }else if (getIntent().getStringExtra("promoCP1").trim().equals("Msc1")){
                    spinnerPromo.setSelection(7);
                }else if (getIntent().getStringExtra("promoCP1").trim().equals("Msc2")){
                    spinnerPromo.setSelection(8);
                }
                spinnerPromo.setEnabled(false);

            }
            else if(sss.equals("1"))  {
                progressBar.setVisibility(View.INVISIBLE);

                getListFacDetIntent=getListFacDetIntent.replace("|",",");
                getListFacDetIntent= getListFacDetIntent.replaceFirst("","Choisir la Fac/Dép");
                // getListFacDetIntent= getListFacDetIntent.substring(getListFacDetIntent.indexOf(","),getListFacDetIntent.indexOf(","));
                final String [] tabl=getListFacDetIntent.split(",");
                listFac= Arrays.asList(tabl);
                //Fill my spinner
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, listFac);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFac.setAdapter(arrayAdapter);
            }

        }
        catch (Exception ex){



        }







        spinnerFac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String tutorialsName = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
                facDep=spinnerFac.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
               // Toast.makeText(parent.getContext(), "Choisir une Faculté" ,          LENGTH_LONG).show();
            }
        });
        spinnerPromo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                promo=spinnerPromo.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typeHoraire=spinnerType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nomCoursHoraire.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Nom du cours", LENGTH_LONG).show();

                }
                else if (lieuHoraire.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"le lieu", LENGTH_LONG).show();

                }
                else if (dateHoraire.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"la date", LENGTH_LONG).show();

                }else if (titulaireHoraire.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Nom du titulaire", LENGTH_LONG).show();

                }
                else if (typeHoraire.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Choisir le type d'horaire", LENGTH_LONG).show();

                }
               else if (promo.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Choisir une promotion", LENGTH_LONG).show();
                } else if (nomCoursHoraire.getText().toString()=="" || lieuHoraire.getText().toString()==""
                        || dateHoraire.getText().toString()=="" || titulaireHoraire.getText().toString()==""){
                    Toast.makeText(getApplicationContext(),"Remplir l'horaire", LENGTH_LONG).show();
                }
                else if ((spinnerFac.getSelectedItemPosition()==0) && !(sss.equals("01")))
                {

                    Toast.makeText(getApplicationContext(), "Choisir une Faculté" ,LENGTH_LONG).show();
                }
                else if (spinnerPromo.getSelectedItemPosition()==0)
                {

                    Toast.makeText(getApplicationContext(), "Choisir une Promotion" ,LENGTH_LONG).show();
                }
                else {
                    //mStorageRef = FirebaseStorage.getInstance().getReference();
                    final Map<String, Object> horaireDataMap = new HashMap<>();
                    horaireDataMap.put("Cours",nomCoursHoraire.getText().toString());
                    horaireDataMap.put("Lieu",lieuHoraire.getText().toString());
                    horaireDataMap.put("Date",dateHoraire.getText().toString());
                    horaireDataMap.put("Titulaire",titulaireHoraire.getText().toString());
                    horaireDataMap.put("Détails",detailsHoraire.getText().toString()+" \n Ecrit par : "+getUser);
                    progressBar.setVisibility(View.VISIBLE);
                    // Hierarchical Data with Subcollection-Document in a Document
                    db.collection(getUnivName).document(facDep)
                            .collection(promo).document("Horaire")
                            .collection(typeHoraire)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            count++;
                                        }
                                        db
                                                .collection(getUnivName).document(facDep)
                                                .collection(promo).document("Horaire")
                                                .collection(typeHoraire).document(typeHoraire+""+count)
                                                .set(horaireDataMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        Log.d(TAG, "Ajout horaire "+typeHoraire);
                                                        btnAjout.setImageResource(R.drawable.ic_done_bleu_24dp);
                                                        resetField();
                                                        Toast.makeText(getApplicationContext(),"Ajout "+typeHoraire+" réussi",LENGTH_LONG).show();
                                                        count=0;
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error writing document", e);
                                                        btnAjout.setImageResource(R.drawable.ic_done_red_24dp);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        resetField();
                                                    }
                                                });
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                        resetField();
                                    }
                                }
                            });





                }
            }
        });




    }
    public void resetField()
    {
        nomCoursHoraire.setText("");
        lieuHoraire.setText("");
        dateHoraire.setText("");
        titulaireHoraire.setText("");
        detailsHoraire.setText("");
        spinnerFac.setSelection(0);
        spinnerPromo.setSelection(0);

    }


}
