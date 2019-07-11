package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ssmarty.univ.time.getCurrentDate;

public class TabFacCommuni extends AppCompatActivity {
    private Spinner spinnerSelecteFac;
    private EditText objectMessage, messageTextFac;
    private ImageButton btnSendFacMessag;
    private TextView txtSenderAndDate;
    private String nameOfSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1_fac_communi);spinnerSelecteFac = findViewById(R.id.spinner_fac_message_Univ);
        objectMessage=findViewById(R.id.objetMessage_fac);
        messageTextFac=findViewById(R.id.editxtxMessage_fac);
        btnSendFacMessag=findViewById(R.id.btnSendFacMessage);
        txtSenderAndDate=findViewById(R.id.txtSenderDateMessage_Fac);

        //todo get name of sender
        nameOfSender="Prof Matungulu";
        //set sender name and date
        //todo get name of sender
        nameOfSender="Prof Matungulu";
        //set sender name and date
        getCurrentDate getCurrentDate = new getCurrentDate();
        txtSenderAndDate.setText(nameOfSender+" - Date :"+getCurrentDate.getCurrentDate());

        ArrayList<String> arrayList = new ArrayList<>();
        //todo get Item(fac) FROM STOREDDATA --
        arrayList.add("- - -");
        arrayList.add("Medecine G2");
        arrayList.add("Philo G3");
        arrayList.add("Economie G1");
        arrayList.add("INFO L2");
        arrayList.add("Génie G1");
        //Fill my spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,                         android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelecteFac.setAdapter(arrayAdapter);
        spinnerSelecteFac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String tutorialsName = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                Toast.makeText(parent.getContext(), "Choiisir une destination du message" ,          Toast.LENGTH_LONG).show();
            }


        });

        //btn send Data
        btnSendFacMessag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfMessageIsEmpty())btnSendFacMessag.setImageResource(R.drawable.ic_send_red_24dp);
                else{
                    btnSendFacMessag.setImageResource(R.drawable.ic_send_bleu_24dp);
                    Toast.makeText(getApplicationContext(),"Message envoyé",Toast.LENGTH_SHORT).show();
                    //TODO CHEK IF CONNECTION
                }
            }
        });
    }

    private Boolean checkIfMessageIsEmpty(){
        Boolean chk=true;
        if ((objectMessage.length()!=0)&&(messageTextFac.length()!=0)){
            if(spinnerSelecteFac != null &&!(spinnerSelecteFac.getSelectedItem().toString().equals("- - -") )) {
                //name = (String)spinnerName.getSelectedItem();
                chk=false;
            } else   {chk=true;Toast.makeText(getApplicationContext(),"Choisir un destinateur",Toast.LENGTH_LONG).show();}
        }
        else Toast.makeText(getApplicationContext(),"Remplir l'objet ou message",Toast.LENGTH_LONG).show();
        return chk;
    }

}
