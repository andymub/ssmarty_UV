package ssmarty.univ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private StorageReference mStorageRef;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG="TAG";
    ProgressBar progressBar;
    int count = 0;
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

        progressBar.setVisibility(View.INVISIBLE);
        final String getUnivName= getIntent().getStringExtra("data_nom_univ");
        String getListFacDetIntent= getIntent().getStringExtra("list_fac");
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
        spinnerFac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String tutorialsName = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
                facDep=spinnerFac.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                Toast.makeText(parent.getContext(), "Choisir une Faculté" ,          LENGTH_LONG).show();
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
                else if (spinnerFac.getSelectedItemPosition()==0)
                {

                    Toast.makeText(getApplicationContext(), "Choisir une Faculté" ,LENGTH_LONG).show();
                }
                else if (spinnerPromo.getSelectedItemPosition()==0)
                {

                    Toast.makeText(getApplicationContext(), "Choisir une Promotion" ,LENGTH_LONG).show();
                }
                else {
                    mStorageRef = FirebaseStorage.getInstance().getReference();
                    final Map<String, Object> horaireDataMap = new HashMap<>();
                    horaireDataMap.put("cours",nomCoursHoraire.getText().toString());
                    horaireDataMap.put("lieu",lieuHoraire.getText().toString());
                    horaireDataMap.put("date",dateHoraire.getText().toString());
                    horaireDataMap.put("titulaire",titulaireHoraire.getText().toString());
                    horaireDataMap.put("details",detailsHoraire.getText().toString());
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
                                                    }
                                                });
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
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
