package ssmarty.univ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.HashMap;
import java.util.Map;

import io.opencensus.stats.Aggregation;
import ssmarty.univ.database.model.MessageUniv;
import ssmarty.univ.time.getCurrentDate;

public class TabUnivCommuni extends AppCompatActivity {
    private EditText objectMessage, messageTextUniv;
    private ImageButton btnSendFacMessag;
    private TextView txtSenderAndDate,txtNomUniv;
    private String nameOfSender;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG="TAG";
    ProgressBar progeProgressBar;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_univ_communi);
        objectMessage=findViewById(R.id.objetMessage_Univ);
        messageTextUniv =findViewById(R.id.editxtxMessage_Univ);
        btnSendFacMessag=findViewById(R.id.imgbtnMessage_Univ);
        txtSenderAndDate=findViewById(R.id.txtSenderDateMessage_Univ);
        txtNomUniv=findViewById(R.id.txtNomUniv_ContactUniv);
        progeProgressBar=findViewById(R.id.progressBarMessageUniv);
        progeProgressBar.setVisibility(View.INVISIBLE);

        nameOfSender= getIntent().getStringExtra("data_nom_user");
        final String getUnivName = getIntent().getStringExtra("data_nom_univ");
        //set sender name and date
        getCurrentDate getCurrentDate = new getCurrentDate();
        txtSenderAndDate.setText(nameOfSender+" - Date :"+getCurrentDate.getCurrentDate());
        txtNomUniv.setText(getUnivName);


        //on TextChange
        objectMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSendFacMessag.setImageResource(R.drawable.ic_send_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        messageTextUniv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSendFacMessag.setImageResource(R.drawable.ic_send_black_24dp);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //btn send Data
        btnSendFacMessag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfMessageIsEmpty()) btnSendFacMessag.setImageResource(R.drawable.ic_send_red_24dp);
                else{

                    // Build an AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(TabUnivCommuni.this);

                    // Set a title for alert dialog
                    builder.setTitle("Confirmation");

                    // Ask the final question
                    builder.setMessage("Envoyer ce message ?");

                    // Set the alert dialog yes button click listener
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when user clicked the Yes button
                            // Set the TextView visibility GONE
                            //tv.setVisibility(View.GONE);

                            btnSendFacMessag.setClickable(false);
                            MessageUniv messageUniv = new MessageUniv();
                            messageUniv.setTitre(objectMessage.getText().toString());
                            messageUniv.setEditeur(txtSenderAndDate.getText().toString());
                            messageUniv.setMessage(messageTextUniv.getText().toString());
                            final Map<String, Object> messageToMap = new HashMap<>();
                            messageToMap.put("Titre",messageUniv.getTitre());
                            messageToMap.put("Editeur",messageUniv.getEditeur());
                            messageToMap.put("Message",messageUniv.getMessage());
                            progeProgressBar.setVisibility(View.VISIBLE);

                            db.collection(getUnivName+" message")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot document : task.getResult()) {
                                                    count++;
                                                }
                                                db.collection(getUnivName+" message").document("Message"+count)
                                                        .set(messageToMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                btnSendFacMessag.setClickable(true);
                                                                progeProgressBar.setVisibility(View.INVISIBLE);
                                                                btnSendFacMessag.setImageResource(R.drawable.ic_send_bleu_24dp);
                                                                Toast.makeText(getApplicationContext(),"Message envoyé",Toast.LENGTH_SHORT).show();
                                                                count=0;
                                                                clearFiel();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error writing document", e);
                                                                btnSendFacMessag.setClickable(true);
                                                                progeProgressBar.setVisibility(View.INVISIBLE);
                                                                btnSendFacMessag.setImageResource(R.drawable.ic_send_red_24dp);
                                                                count=0;
                                                                Toast.makeText(getApplicationContext(),"Message Non envoyé",Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    });


                            //btnSendFacMessag.setImageResource(R.drawable.ic_send_bleu_24dp);
                            // Toast.makeText(getApplicationContext(),"Message envoyé",Toast.LENGTH_SHORT).show();
                            //TODO CHEK IF CONNECTION
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when No button clicked
                            Toast.makeText(getApplicationContext(),
                                    "Non",Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();

                    //fin
                }
            }
        });
    }
    private Boolean checkIfMessageIsEmpty(){
        Boolean chk=false;
        if ((objectMessage.length()==0)){

                chk=true;
            }
        else if (messageTextUniv.length()==0){
             Toast.makeText(getApplicationContext(),"Message vide...",Toast.LENGTH_LONG).show();
             chk=true;
        }else if (messageTextUniv.length()<6){
             Toast.makeText(getApplicationContext(),"Message court...",Toast.LENGTH_LONG).show();
             chk=true;
        }else if (objectMessage.length()<5){
             Toast.makeText(getApplicationContext(),"Objet du message est court...",Toast.LENGTH_LONG).show();
             chk=true;
        }
        else
        {

        }
        return chk;
    }
    public void clearFiel(){
        objectMessage.setText("");
        messageTextUniv.setText("");
        btnSendFacMessag.setImageResource(R.drawable.ic_send_black_24dp);
    }
}
