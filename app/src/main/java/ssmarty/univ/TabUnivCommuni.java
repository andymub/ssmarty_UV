package ssmarty.univ;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ssmarty.univ.time.getCurrentDate;

public class TabUnivCommuni extends AppCompatActivity {
    private EditText objectMessage, messageTextFac;
    private ImageButton btnSendFacMessag;
    private TextView txtSenderAndDate;
    private String nameOfSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_univ_communi);
        objectMessage=findViewById(R.id.objetMessage_Univ);
        messageTextFac=findViewById(R.id.editxtxMessage_Univ);
        btnSendFacMessag=findViewById(R.id.imgbtnMessage_Univ);
        txtSenderAndDate=findViewById(R.id.txtSenderDateMessage_Univ);

        //todo get name of sender
        nameOfSender="Prof Matungulu";
        //set sender name and date
        getCurrentDate getCurrentDate = new getCurrentDate();
        txtSenderAndDate.setText(nameOfSender+" - Date :"+getCurrentDate.getCurrentDate());

        //btn send Data
        btnSendFacMessag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfMessageIsEmpty()) btnSendFacMessag.setImageResource(R.drawable.ic_send_red_24dp);
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

                chk=false;
            }
        else Toast.makeText(getApplicationContext(),"Remplir l'objet ou message",Toast.LENGTH_LONG).show();
        return chk;
    }
}
