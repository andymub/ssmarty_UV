package ssmarty.univ;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity_Prof extends AppCompatActivity {
    private ImageButton btnBuildList, btnCOmmuni, btnMyList, btnContactUniv;
    Intent switch_prof_acti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__prof);
        btnBuildList=findViewById(R.id.imgbtn_liste);
        btnCOmmuni=findViewById(R.id.imgbtn_communi_prof);
        btnContactUniv=findViewById(R.id.imgbtn_contact_univ);
        btnMyList=findViewById(R.id.imgbtn_my_listes);

        btnBuildList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_liste_presence.class);
                startActivity(switch_prof_acti);

            }
        });
        btnCOmmuni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_Communi_Prof.class);
                startActivity(switch_prof_acti);

            }
        });
        btnMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_MyList_prof.class);
                startActivity(switch_prof_acti);

            }
        });
        btnContactUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch_prof_acti=new Intent(MainActivity_Prof.this,Activity_contatUniv_prof.class);
                startActivity(switch_prof_acti);
            }
        });
    }
}
