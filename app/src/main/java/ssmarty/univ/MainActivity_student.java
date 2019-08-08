package ssmarty.univ;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity_student extends AppCompatActivity {

    private ImageButton btnHoraire, btnExam, btnComFac, btnComUniv;
    Intent switchActiv;
    TextView displayUnivName,displayStudentName;
    String facDep,promo,annee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        btnComFac=findViewById(R.id.imgbtn_my_listes);
        btnComUniv=findViewById(R.id.imgbtn_com_univ);
        btnExam=findViewById(R.id.imgbtn_communi_prof);
        btnHoraire=findViewById(R.id.imgbtn_liste);
        displayUnivName=findViewById(R.id.studen_txtNom_univ); displayStudentName=findViewById(R.id.nom_etudiant);
        //todo get student's name and uniV n
        String []getDataFromCard = getIntent().getStringArrayExtra("data");
        displayUnivName.setText(getDataFromCard[0]+"");
        displayStudentName.setText(getDataFromCard[1]+" - ");
        String promoYear = getDataFromCard[2];
        String[] facPromoYear = promoYear.split("_");
        facDep= facPromoYear[0];
        promo=facPromoYear[1];
        annee=facPromoYear[2];


        //boutton Horaire
        btnHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, HoraireActivity.class);
                switchActiv.putExtra("promo",facDep+" "+promo+"."+annee);
                startActivity(switchActiv);

            }
        });
        //bouton exam
        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, ExamActivity.class);
                switchActiv.putExtra("promo",facDep+" "+promo+"."+annee);
                startActivity(switchActiv);

            }
        });

        //bouton communication fac
        btnComFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, CommuFacActivity.class);
                switchActiv.putExtra("promo",facDep+" "+promo+"."+annee);
                startActivity(switchActiv);

            }
        });

        //btn com univ

        btnComUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, CommuUnivActivity.class);
                startActivity(switchActiv);
            }
        });
    }
}
