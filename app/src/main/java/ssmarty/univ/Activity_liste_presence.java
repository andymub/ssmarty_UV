package ssmarty.univ;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Activity_liste_presence extends AppCompatActivity {

    private TextView txtdate;
    Spinner spinnerTypePresence;
    private EditText edittxtAutreRainson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_presence);
        txtdate=findViewById(R.id.txtdate);
        spinnerTypePresence=findViewById(R.id.spinnerType);
        edittxtAutreRainson=findViewById(R.id.edtxtAutrerainson);
        getCurrentDate();
        //spinner ste onclick
        spinnerTypePresence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==3){
                    edittxtAutreRainson.setVisibility(View.VISIBLE);
                }
                else{
                    edittxtAutreRainson.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd , HH:mm");
        String strDate = "Date et Heure : " + mdformat.format(calendar.getTime());
        txtdate.setText(strDate );
    }
}
