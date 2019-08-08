package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListHoraireEtudiantAdapter;
import ssmarty.univ.model.HoraireCourOuExam;

public class HoraireActivity extends AppCompatActivity {
    //List of Horaire model
    List<HoraireCourOuExam> horaireCourList;

    ListView listViewHoraire;
    TextView txtPromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horaire);
        listViewHoraire=findViewById(R.id.listViewHoraireEtudiant);
        txtPromo= findViewById(R.id.txtHorairePromo);
        horaireCourList= new ArrayList<>();
        String getPromo =getIntent().getStringExtra("promo");
        txtPromo.setText(getPromo);

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
        ListHoraireEtudiantAdapter HoraireAdapter =
                new ListHoraireEtudiantAdapter(this,R.layout.my_custom_list_horaire_exam,
                        horaireCourList);
        listViewHoraire.setAdapter(HoraireAdapter);
    }



}
