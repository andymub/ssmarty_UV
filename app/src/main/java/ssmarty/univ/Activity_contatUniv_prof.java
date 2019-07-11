package ssmarty.univ;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_contatUniv_prof extends AppCompatActivity {
    private TextView numUnivOne, numUnivTwo, emailUniv,nomUniv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contat_univ_prof);
        numUnivOne = findViewById(R.id.txtxNumUniv1);
        numUnivTwo = findViewById(R.id.txtxNumUniv2);
        emailUniv = findViewById(R.id.txtEmailUniv);
        nomUniv=findViewById(R.id.txtContactNomUniv);

        //todo fetch univ'name into shared dada
        nomUniv.setText("Université dupont");

        numUnivTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(numUnivTwo);
            }
        });
        numUnivOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(numUnivOne);
            }
        });
        emailUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailUniv.getText().toString().trim();
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
                sendIntent.setData(Uri.parse(email));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "-");
               // sendIntent.setType("plain/text");
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "Insert text");
                startActivity(sendIntent);


            }
        });
    }

    public void call(TextView num) {
        Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:"+num.getText().toString().trim()));
        if(ActivityCompat.checkSelfPermission(Activity_contatUniv_prof.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        startActivity(call);
    }
}
