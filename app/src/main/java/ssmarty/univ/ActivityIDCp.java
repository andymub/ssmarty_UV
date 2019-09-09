package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ActivityIDCp extends AppCompatActivity {
    EditText txtCode,nomEtPrenomCp;
    ProgressBar progressBar;
    ImageButton imageButton;
    String code,univ,fac,promo, nom;
    String []dataTosend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcp);
         txtCode=findViewById(R.id.codeDialog);
         nomEtPrenomCp = findViewById(R.id.EdtxtUserNmeVerificationDialogue);
         progressBar = findViewById(R.id.progressBar2);
         imageButton = findViewById(R.id.imgbtnIDCp);
         progressBar.setVisibility(View.INVISIBLE);
//        studentIntent.putExtra("pass",pass1);
//        studentIntent.putExtra("facCP",facDep);
//        studentIntent.putExtra("promoCP",promo);
         code = getIntent().getStringExtra("pass");
         fac = getIntent().getStringExtra("facCP");
         univ = getIntent().getStringExtra("univ");
         promo = getIntent().getStringExtra("promoCP");

         imageButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 progressBar.setVisibility(View.VISIBLE);

                 if (isEmptyFields()){}
                 else{
                 nom ="C.P "+ nomEtPrenomCp.getText();
                 if (code.equals(txtCode.getText().toString().trim())){
                     Intent studentIntent;
                     dataTosend= new String[]{univ,nom};
                     studentIntent=new Intent(ActivityIDCp.this, MainActivity_Prof.class);
                     studentIntent.putExtra("data",dataTosend);
                     //studentIntent.putExtra("univ",univ);
                     //studentIntent.putExtra("pass",pass1);
                     studentIntent.putExtra("facCP",fac);
                     studentIntent.putExtra("promoCP",promo);
                     //studentIntent.putExtra("userName",dataTosend)
                     progressBar.setVisibility(View.INVISIBLE);
                     startActivity(studentIntent);
                 }
                 else Toast.makeText(getApplicationContext(),"Passe incorecte",Toast.LENGTH_LONG).show();
                 }

             }
         });


    }
    public boolean isEmptyFields(){
        Boolean chk =true;
        if ((nomEtPrenomCp.getText().length()<3) || (txtCode.getText().length()<3)){
            progressBar.setVisibility(View.INVISIBLE);
        }
        else chk=false;

        return chk;
    }
}
