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

public class ExamActivity extends AppCompatActivity {
    TextView txtPromo;
    //List of Horaire model
    List<HoraireCourOuExam> ExamCourList;
    String univName, facDep, promo;
    ListView listViewExam;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "TAG";
    public ProgressBar progressBar;
    Boolean allListIsEmpty=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        listViewExam = findViewById(R.id.listViewExamEtudiant);
        txtPromo = findViewById(R.id.txtExamPromo);
        String getPromo = getIntent().getStringExtra("promo");
        txtPromo.setText(getPromo);
        progressBar = findViewById(R.id.progressExamStudent);
        String[] gtpromo1= getPromo.split("_");
        txtPromo.setText(gtpromo1[0]+" "+gtpromo1[1]);
        univName = getIntent().getStringExtra("univ_name");
        facDep = getIntent().getStringExtra("fac");
        promo = getIntent().getStringExtra("promotion");
//DESACTIVATE NFC
        NfcAdapter adapterNfc = NfcAdapter.getDefaultAdapter(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            adapterNfc.enableReaderMode(this, null, NfcAdapter.STATE_OFF, null);
        }
//        li

        ExamCourList = new ArrayList<>();
        //TODO RECUPERER LES ELEMENTS DANS LA DB et -> dans la liste

        ExamCourList.add(new HoraireCourOuExam(
                "Exam Introduction à la géologie"
                , "Bat 1",
                "Prof Matungulu P.",
                "12/06/2019 à 9h 45",
                "Exam durée : 3h "));
        ExamCourList.add(new HoraireCourOuExam(
                "Exam Géologie 1"
                , "Bat 1, Salle c",
                "Prof Matungulu P.",
                "3/07/2019",
                "Durée Exam 2h30 , à partir de 9h 00"));
        ExamCourList.add(new HoraireCourOuExam(
                "Exam Mécanique de Sol"
                , "Bat 7, Salle Pr Febuz",
                "Prof Zimmerman M.",
                "16/08/2019 à 11h00",
                "Le prof ne sera pas dans la ville, voir son Ass"));
        ExamCourList.add(new HoraireCourOuExam(
                "TP EN GROUPE Géographie "
                , "Bat 4, Salle D",
                "Prof Alexis K.",
                "14/07/2019",
                "Tp puis Examen après 3 semaines de cours"));
        ExamCourList.add(new HoraireCourOuExam(
                "Exam Géologie 2"
                , "Bat 1, Salle 1",
                "Prof Matungulu P.",
                "4/10/2019",
                "Faira aussi partie du TFC"));
        ExamCourList.add(new HoraireCourOuExam(
                "Exam Géologie 3"
                , "Bat 1, Salle A",
                "Prof Matungulu P.",
                "2/09/2019",
                "A 11h 00 après Exposition du TP 7h-10h00"));

        db.collection(univName).document(facDep)
                .collection(promo).document("Horaire")
                .collection("Examen")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ExamCourList = new ArrayList<>();
                //HoraireAdapter.clear();
                ExamCourList.clear();
                HoraireCourOuExam examModel = new HoraireCourOuExam();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String kkkh = document.getString("Cours");
                        examModel.setNomCourExam(document.getString("Cours"));
                        examModel.setDate(document.getString("Date"));
                        examModel.setTitulaire(document.getString("Titulaire"));
                        examModel.setLieu(document.getString("Lieu"));
                        examModel.setAutres(document.getString("Détails"));
                        ExamCourList.add(new HoraireCourOuExam(examModel));

                    }
                    if (ExamCourList.isEmpty()){
                        allListIsEmpty=true;
                       // Toast.makeText(getApplicationContext(),"Aucun Examen",Toast.LENGTH_LONG).show();
                    }
                    ExamCourList.add(new HoraireCourOuExam("","","","",""));
                    // listViewExam.setAdapter(HoraireAdapter);
//                    progressBar.setVisibility(View.INVISIBLE);

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
                        //Toast.makeText(getApplicationContext(), "Aucun horaire disponible", Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });

        db.collection(univName).document(facDep)
                .collection(promo).document("Horaire")
                .collection("Interrogation")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                HoraireCourOuExam examModel = new HoraireCourOuExam();
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String kkkh = document.getString("Cours");
                        examModel.setNomCourExam(document.getString("Cours"));
                        examModel.setDate(document.getString("Date"));
                        examModel.setTitulaire(document.getString("Titulaire"));
                        examModel.setLieu(document.getString("Lieu"));
                        examModel.setAutres(document.getString("Détails"));
                        ExamCourList.add(new HoraireCourOuExam(examModel));

                    }
                    if (ExamCourList.isEmpty()){
                        if(allListIsEmpty){

                            finish();
                        }
                        Toast.makeText(getApplicationContext(),"Aucune Interrogation",Toast.LENGTH_LONG).show();
                    }
                    ListHoraireEtudiantAdapter HoraireAdapter =
                            new ListHoraireEtudiantAdapter(getApplicationContext(), R.layout.my_custom_list_horaire_exam,
                                    ExamCourList);
                    listViewExam.setAdapter(HoraireAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                    //ExamCourList.clear();



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
                        Toast.makeText(getApplicationContext(), "Aucun horaire disponible", Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });


    }


}