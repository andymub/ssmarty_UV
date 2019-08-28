package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class Activity_ajouts_horaire_prof extends AppCompatActivity {

    ImageButton btnAjout;
    Spinner fac, promo,type;
    EditText nomCoursHoraire,lieuHoraire,titulaireHoraire,dateHoraire,detailsHoraire;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouts_horaire_prof);

        btnAjout=findViewById(R.id.imgbtnAjoutCours);
        fac=findViewById(R.id.spinnerfaDepAjoutCours);
        promo= findViewById(R.id.spinnerPromoAjoutCours);
        type=findViewById(R.id.spinnerTypeAjoutCours);

        nomCoursHoraire =findViewById(R.id.editxtNomDuCoursAjoutCours);
        lieuHoraire =findViewById(R.id.editxtLieuCoursAjoutCours);
        titulaireHoraire = findViewById(R.id.edittxtTitulaireAjoutCours);
        dateHoraire= findViewById(R.id.editxtDateCoursAjoutCours);
        detailsHoraire= findViewById(R.id.detailAjoutCours);

    }


}
