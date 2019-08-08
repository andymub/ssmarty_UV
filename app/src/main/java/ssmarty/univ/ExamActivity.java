package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListHoraireEtudiantAdapter;
import ssmarty.univ.model.HoraireCourOuExam;

public class ExamActivity extends AppCompatActivity {
    TextView txtPromo;
    //List of Horaire model
    List<HoraireCourOuExam> ExamCourList;

    ListView listViewExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        listViewExam=findViewById(R.id.listViewExamEtudiant);
        txtPromo=findViewById(R.id.txtExamPromo);
        String getPromo =getIntent().getStringExtra("promo");
        txtPromo.setText(getPromo);

        ExamCourList= new ArrayList<>();
        //TODO RECUPERER LES ELEMENTS DANS LA DB et -> dans la liste

        ExamCourList.add(new HoraireCourOuExam(
                "Exam Introduction à la géologie"
                ,"Bat 1",
                "Prof Matungulu P.",
                "12/06/2019 à 9h 45",
                "Exam durée : 3h "));
        ExamCourList.add(new HoraireCourOuExam(
                "Exam Géologie 1"
                ,"Bat 1, Salle c",
                "Prof Matungulu P.",
                "3/07/2019",
                "Durée Exam 2h30 , à partir de 9h 00"));
        ExamCourList.add(new HoraireCourOuExam(
                "Exam Mécanique de Sol"
                ,"Bat 7, Salle Pr Febuz",
                "Prof Zimmerman M.",
                "16/08/2019 à 11h00",
                "Le prof ne sera pas dans la ville, voir son Ass"));
        ExamCourList.add(new HoraireCourOuExam(
                "TP EN GROUPE Géographie "
                ,"Bat 4, Salle D",
                "Prof Alexis K.",
                "14/07/2019",
                "Tp puis Examen après 3 semaines de cours"));
        ExamCourList.add(new HoraireCourOuExam(
                "Exam Géologie 2"
                ,"Bat 1, Salle 1",
                "Prof Matungulu P.",
                "4/10/2019",
                "Faira aussi partie du TFC"));
        ExamCourList.add(new HoraireCourOuExam(
                "Exam Géologie 3"
                ,"Bat 1, Salle A",
                "Prof Matungulu P.",
                "2/09/2019",
                "A 11h 00 après Exposition du TP 7h-10h00"));
        ListHoraireEtudiantAdapter HoraireAdapter =
                new ListHoraireEtudiantAdapter(this,R.layout.my_custom_list_horaire_exam,
                        ExamCourList);
        listViewExam.setAdapter(HoraireAdapter);
    }
}
