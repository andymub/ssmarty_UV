package ssmarty.univ;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Activity_contatUniv_prof extends AppCompatActivity {
    private TextView numUnivOne, numUnivTwo, emailUniv,nomUniv;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG ="TAG",nomContacUniv,emailContacUniv,num1ContacUniv,num2ContacUniv,detailsContacUniv;
    TextView detail;
    ProgressBar progressBarContact;
    int clicableButton=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contat_univ_prof);
        numUnivOne = findViewById(R.id.txtxNumUniv1);
        numUnivTwo = findViewById(R.id.txtxNumUniv2);
        emailUniv = findViewById(R.id.txtEmailUniv);
        nomUniv=findViewById(R.id.txtContactNomUniv);
        detail=findViewById(R.id.txtDetailContactUniv);
        progressBarContact=findViewById(R.id.progressContactUniv);
        disanbClickable();
        detail.setMovementMethod(LinkMovementMethod.getInstance());

        //todo fetch univ'name into shared dada       DONE

        String getListUnivName = getIntent().getStringExtra("data_nom_univ");
        db.collection(getListUnivName+" contact")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            nomContacUniv= document.getString("Nom");
                            emailContacUniv= document.getString("Email");
                            num1ContacUniv= document.getString("Num1");
                            num2ContacUniv= document.getString("Num2");
                            detailsContacUniv= document.getString("Détails");
                            nomUniv.setText(nomContacUniv);
                            numUnivOne.setText(num1ContacUniv);
                            numUnivTwo.setText(num2ContacUniv);
                            emailUniv.setText(emailContacUniv);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                detail.setText(Html.fromHtml(detailsContacUniv, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                detail.setText(Html.fromHtml(detailsContacUniv));
                            }
                            //detail.setText(detailsContacUniv);
                            clicableButton=1;

                            clickable();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        disanbClickable();
                        clicableButton=0;

            }
        });

        numUnivTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicableButton==1)
                call(numUnivTwo);
                else {

                }
            }
        });
        numUnivOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clicableButton==1) call(numUnivOne);
                else
                {

                }
            }
        });
        emailUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicableButton==1){
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
                else {

                }


            }
        });


    }
    public void clickable(){

        progressBarContact.setVisibility(View.INVISIBLE);
        emailUniv.setLinksClickable(true);
        nomUniv.setClickable(true);
        numUnivOne.setClickable(true);
        numUnivTwo.setLinksClickable(true);
    }
    public void disanbClickable(){

        progressBarContact.setVisibility(View.VISIBLE);
        emailUniv.setLinksClickable(false);
        emailUniv.setText("Email ...");
        nomUniv.setClickable(false);
        nomUniv.setText("Univ  ...");
        numUnivOne.setClickable(false);
        numUnivTwo.setText("Num  ...");
        numUnivOne.setText("     ...");
        numUnivTwo.setLinksClickable(false);
        detail.setText("En cours ...");
    }

    public void call(TextView num) {
        //Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:"+num.getText().toString().trim()));
        if(ActivityCompat.checkSelfPermission(Activity_contatUniv_prof.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        startActivity(call);
    }
}
