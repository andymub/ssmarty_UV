package ssmarty.univ;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Activity_liste_presence extends AppCompatActivity {
    ListView myListview;
    Button Addbutton;
    String GetValue;
    ScrollView scrollListePresence;
    int value=0;
    ToneGenerator toneGen1;
    private RelativeLayout relaLayout_presence;
    String[] ListElements = new String[] {
            "-   -   -",
    };


    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter=0;

    private TextView txtdate,messageEvolution;
    Spinner spinnerTypePresence;
    ImageButton btnStartList,sendToCloud;
    int i=1;
    private EditText edittxtAutreRainson,editxtIntutuleListe;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_presence);
        relaLayout_presence=findViewById(R.id.relaLayout_presence);
        txtdate=findViewById(R.id.txtdate);
        spinnerTypePresence=findViewById(R.id.spinnerType);
        edittxtAutreRainson=findViewById(R.id.edtxtAutrerainson);
        editxtIntutuleListe=findViewById(R.id.editxtObjet);
        btnStartList=findViewById(R.id.imgbtnStarList);
        sendToCloud=findViewById(R.id.ImgSendcloud);
        messageEvolution=findViewById(R.id.txtmesageEtatList);
        Addbutton = (Button) findViewById(R.id.addBtn);
        messageEvolution.setText("...");
        myListview = (ListView) findViewById(R.id.listView_presence);
        myListview.setBackgroundResource(R.color.WHITE_nfc);
        scrollListePresence = findViewById(R.id.scroll_liste_presence);

        //Set Inivible scrollView
        scrollListePresence.setVisibility(View.INVISIBLE);

        //nOTICATION BEEP
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        final List<String> ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (Activity_liste_presence.this, android.R.layout.simple_list_item_1, ListElementsArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                final TextView tv = view.findViewById(android.R.id.text1);
                if(position == ListElementsArrayList.size()-1) {
                    // Set the text color of TextView (ListView Item)
                    relaLayout_presence.setBackgroundResource(R.drawable.listview_border);
                    tv.setTextColor(getResources().getColor(R.color.bipNfc_etudiant_added));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            relaLayout_presence.setBackgroundResource(R.color.WHITE_nfc);
                        }
                    }, 250);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv.setTextColor(Color.BLACK);
                        }
                    }, 500);
                }
                // Generate ListView Item using TextView
                return view;
            }
        };

        myListview.setAdapter(adapter);

        //get date and hour
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
        btnStartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIfEditEmpty())
                {
                    edittxtAutreRainson.setEnabled(true);
                    editxtIntutuleListe.setEnabled(true);
                    spinnerTypePresence.setClickable(true);
                    btnStartList.setImageResource(R.drawable.ic_done_red_24dp);
                    //set visible Scrollliste
                    scrollListePresence.setVisibility(View.INVISIBLE);
                }
                else{


                btnStartList.setImageResource(R.drawable.ic_done_bleu_24dp);
                messageEvolution.setText("Pour remplir la liste, bipé vos cartes");

                //set visible Scrollliste
                scrollListePresence.setVisibility(View.VISIBLE);
                edittxtAutreRainson.setEnabled(false);
                editxtIntutuleListe.setEnabled(false);
                spinnerTypePresence.setClickable(false);
                    spinnerTypePresence.setEnabled(false);
                if(edittxtAutreRainson.getVisibility() == View.VISIBLE){
                    edittxtAutreRainson.setEnabled(false);
                }
                }
            }
        });
        sendToCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i==1){

                    sendToCloud.setImageResource(R.drawable.ic_cloud_done_black_24dp);
                    messageEvolution.setText("Liste envoyéé");
                    i++;
                }
                else {
                    sendToCloud.setImageResource(R.drawable.ic_cloud_off_black_24dp);
                    messageEvolution.setText("Liste non envoyéé, stockéé dans Mes Listes");
                }
            }

        });

        //LISTEvIEW add //todo to be replace with NFC LISTENER
        Addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                     toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    //toneGen1.release();
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (toneGen1 != null) {
                                toneGen1.release();
                                toneGen1 = null;
                            }
                        }

                    }, 100);

                   // r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GetValue="Etudiant(e) "+value++;
                ListElementsArrayList.add(GetValue);
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy , HH:mm");
        String strDate = "Date et Heure : " + mdformat.format(calendar.getTime());
        txtdate.setText(strDate );
    }

    public Boolean checkIfEditEmpty(){
        Boolean chk=true;
        if (editxtIntutuleListe.length()==0){
            //////////////
            editxtIntutuleListe.setHintTextColor(getResources().getColor(R.color.RED_nfc));
            chk=true;
            if(edittxtAutreRainson.getVisibility() == View.VISIBLE)
            {
                // Toast.makeText(getApplicationContext(),"c est bon2",Toast.LENGTH_LONG).show();
                //////////////

                if (edittxtAutreRainson.length()!=0){ chk=true;}
                else {chk=true;edittxtAutreRainson.setHintTextColor(getResources().getColor(R.color.RED_nfc));}

            }

        }
        else if (editxtIntutuleListe.length()!=0){
            //Toast.makeText(getApplicationContext(),"c est bon"+editxtIntutuleListe.getTextSize(),Toast.LENGTH_LONG).show();
            if(edittxtAutreRainson.getVisibility() == View.VISIBLE)
            {
               // Toast.makeText(getApplicationContext(),"c est bon2",Toast.LENGTH_LONG).show();
                    //////////////

                if (edittxtAutreRainson.length()!=0){ chk=false;}
                    else {chk=true;edittxtAutreRainson.setHintTextColor(getResources().getColor(R.color.RED_nfc));}

            }
            else{
               // Toast.makeText(getApplicationContext(),"c est bon3",Toast.LENGTH_LONG).show();
                chk=false;
            }
        }
        else{
           // Toast.makeText(getApplicationContext(),"c est pas bon",Toast.LENGTH_LONG).show();
            chk=false;
        }

        return chk;
    }
}
