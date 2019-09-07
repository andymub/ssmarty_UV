package ssmarty.univ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListHoraireEtudiantAdapter;
import ssmarty.univ.model.HoraireCourOuExam;

public class HoraireActivity extends AppCompatActivity {
    //List of Horaire model
    List<HoraireCourOuExam> horaireCourList;

    ListView listViewHoraire;
    TextView txtPromo;
    String univName,facDep,promo;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG="TAG";
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horaire);
        listViewHoraire=findViewById(R.id.listViewHoraireEtudiant);
        txtPromo= findViewById(R.id.txtHorairePromo);
        horaireCourList= new ArrayList<>();
        String getPromo =getIntent().getStringExtra("promo");
        String[] gtpromo1= getPromo.split("_");
        txtPromo.setText(gtpromo1[0]+" "+gtpromo1[1]);
        univName =getIntent().getStringExtra("univ_name");
        facDep  =getIntent().getStringExtra("fac");
        promo =getIntent().getStringExtra("promotion");

        progressBar= findViewById(R.id.progressHorareStudent);
//DESACTIVATE NFC
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            adapter.enableReaderMode(this, null, NfcAdapter.STATE_OFF, null);
        }
//        li

        //TODO RECUPERER LES ELEMENTS DANS LA DB et -> dans la liste

        horaireCourList.add(new HoraireCourOuExam(
                "Introduction à la géologie"
                ,"Bat 4, St Peter",
                "Prof Matungulu P.",
                "12/05/2019",
                "Ass Jean Yves"));
        horaireCourList.add(new HoraireCourOuExam(
                 "Géologie 1"
                ,"Bat 4, Salle B",
                "Prof Matungulu P.",
                "4/06/2019",
                "Ass Paul Yves et Ass Grég J."));
        horaireCourList.add(new HoraireCourOuExam(
                "Mécanique de Sol"
                ,"Bat 7, Salle Pr Febuz",
                "Prof Zimmerman M.",
                "4/07/2019 -12/07/2019",
                "Le prof réviendra pour une séance après son éxam"));
        horaireCourList.add(new HoraireCourOuExam(
                "Géographie "
                ,"Bat 7, Salle D",
                "Prof Alexis K.",
                "4/07/2019 -12/07/2019",
                "Examen après 3 semaines de cours"));
        horaireCourList.add(new HoraireCourOuExam(
                "Géologie 2"
                ,"Bat 1, Salle 1",
                "Prof Matungulu P.",
                "4/08/2019",
                "Ass Paul Yves et Ass Grég J."));
        horaireCourList.add(new HoraireCourOuExam(
                "Géologie 3"
                ,"Bat 2, Salle A",
                "Prof Matungulu P.",
                "25/08/2019",
                "Ass Paul Yves et Ass Grég J."));

        db.collection(univName).document(facDep)
                .collection(promo).document("Horaire")
                .collection("Cours")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                horaireCourList= new ArrayList<>();
                HoraireCourOuExam courModel=new HoraireCourOuExam();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String kkkh=document.getString("Cours");
                        courModel.setNomCourExam(document.getString("Cours"));
                        courModel.setDate(document.getString("Date"));
                        courModel.setTitulaire(document.getString("Titulaire"));
                        courModel.setLieu(document.getString("Lieu"));
                        courModel.setAutres(document.getString("Détails"));
                        horaireCourList.add(new HoraireCourOuExam(courModel));

                    }
                    if (horaireCourList.isEmpty()){
                        finish();
                        Toast.makeText(getApplicationContext(),"Pas d'Horaire en ligne",Toast.LENGTH_LONG).show();
                    }
                    ListHoraireEtudiantAdapter HoraireAdapter =
                            new ListHoraireEtudiantAdapter(getApplicationContext(),R.layout.my_custom_list_horaire_exam,
                                    horaireCourList);
                    listViewHoraire.setAdapter(HoraireAdapter);
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Aucun horaire disponible",Toast.LENGTH_SHORT).show();
                finish();


            }
        });

    }



}
