package ssmarty.univ;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity_student extends AppCompatActivity {

    private ImageButton btnHoraire, btnExam, btnComFac, btnComUniv;
    Intent switchActiv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        btnComFac=findViewById(R.id.imgbtn_my_listes);
        btnComUniv=findViewById(R.id.imgbtn_com_univ);
        btnExam=findViewById(R.id.imgbtn_communi_prof);
        btnHoraire=findViewById(R.id.imgbtn_liste);

        //boutton Horaire
        btnHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, HoraireActivity.class);
                startActivity(switchActiv);

            }
        });
        //bouton exam
        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, ExamActivity.class);
                startActivity(switchActiv);

            }
        });

        //bouton communication fac
        btnComFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, CommuFacActivity.class);
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
