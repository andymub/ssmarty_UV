package ssmarty.univ;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.icu.util.Calendar;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Parcelable;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import ssmarty.univ.QR.QrCodeScannerActivity;
import ssmarty.univ.decrypter.StringToHex;

public class MainActivity extends AppCompatActivity {

    //ETUDIANT EX --univ/fac1_departement1/G1/2019/1/Grég Mélanie
    //PROF EX-univ/fac1_departement1/G1/2019/0/Jean Yves Mélanie
    private static final String TAG ="NFC" ;
    private ImageView imgNfcState;
    private TextView txtStateNfc;
    public  AnimationDrawable frameAnimation;
    public static final String DATABASE_NAME =  "ssmarty_univ";
    public String nomUtilisteur;
    String []dataTosend;
    //sqlite
    SQLiteDatabase mDatabase;
    NfcAdapter nfcAdapter;
    int retour = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context=getApplicationContext();
        imgNfcState= findViewById(R.id.imgNfcState);


       // imgNfcState.setBackground(ResourcesCompat.getDrawable(getResources(), R.mipmap.ssmart_nfc_connect, null));
        txtStateNfc=findViewById(R.id.txtStateNfc);
        ncfCompatible ();

        //NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }
        readFromIntent(getIntent());

        //todo get univ name and user name

        dataTosend= new String[]{"univ", "prof Didier Ortega1","fac1_departement1_G1_2019"};


        imgNfcState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentIntent;
                studentIntent=new Intent(MainActivity.this, MainActivity_student.class);
                studentIntent.putExtra("data",dataTosend);
                //studentIntent.putExtra("userName",dataTosend)
                startActivity(studentIntent);
            }
        });
        imgNfcState.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent studentIntent;
                studentIntent=new Intent(MainActivity.this, MainActivity_Prof.class);
                studentIntent.putExtra("data",dataTosend);
                startActivity(studentIntent);
                return false;
            }
        });

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        txtStateNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentQR =new Intent(MainActivity.this, QrCodeScannerActivity.class);
                startActivity(intentQR);
            }
        });


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       // ncfCompatible ();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ncfCompatible ();

    }


    public void ncfCompatible (){
        NfcManager nfcManager = (NfcManager) getApplication().getSystemService(Context.NFC_SERVICE);
        NfcAdapter nfcAdapter = nfcManager.getDefaultAdapter();
        if (nfcAdapter == null) {
            txtStateNfc.setText("Cliquer pour scanner votre QR code");
            // Device not compatible for NFC support
            Toast.makeText(getApplicationContext(),"Votre dispositif ne supporte pas de NFC",Toast.LENGTH_LONG).show();
            // Build an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            // Set a title for alert dialog
            builder.setTitle("QR code ");

            // Ask the final question
            builder.setMessage("scanner votre code QR ?");

            // Set the alert dialog yes button click listener
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do something when user clicked the Yes button
                    // Set the TextView visibility GONE
                    //tv.setVisibility(View.GONE);
                    Intent intentQR =new Intent(MainActivity.this, QrCodeScannerActivity.class);
                    startActivity(intentQR);

                }
            });

            // Set the alert dialog no button click listener
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do something when No button clicked
//                    Toast.makeText(getApplicationContext(),
//                            "No Button Clicked",Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialog = builder.create();
            // Display the alert dialog on interface
            dialog.show();
        }

        else{
            if (nfcAdapter.isNdefPushEnabled()){
                //Toast.makeText(getApplicationContext(), R.string.ntc_activE,Toast.LENGTH_SHORT).show();
                //imgNfcState.setImageResource(R.mipmap.nfc_ouvert_round);
                txtStateNfc.setText(R.string.ntc_activE);
                txtStateNfc.setTextColor(getResources().getColor(R.color.colorAccent));


            }
            else if (nfcAdapter.isEnabled()){
                Toast.makeText(getApplicationContext(),"ouvert",Toast.LENGTH_SHORT).show();
               // imgNfcState.setImageResource(R.mipmap.ssmart_nfc_connect);
                // Start the animation (looped playback by default).
                //frameAnimation.start();
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.veuill_activE_Nfc,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            }

        }

    }
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            StringToHex stringToHex= new StringToHex();
            //String text2=md5Digest.digest(text);
            //String encrypted = "je suis andy";
            String decrypted = "";
            String s="";
            try {

                for(int index = 0; index < text.length(); index+=9) {
                    String temp = text.substring(index, index+8);
                    int num = Integer.parseInt(temp,2);
                    char letter = (char) num;
                    s = s+letter;
                }
                decrypted = s;
                Log.d("TEST", "decrypted:" + decrypted);
            } catch (Exception e) {
                e.printStackTrace();
            }
            callActivity(decrypted);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        //Toast.makeText(getApplicationContext(),"NFC Content: " + text,Toast.LENGTH_LONG).show();
    }


    public void callActivity(String textFromNfcCard){
        int Year = Calendar.YEAR;
        String [] dataFromNfcCard= textFromNfcCard.split("/");
        if ((dataFromNfcCard.length < 6) || (textFromNfcCard.isEmpty())){
            Toast.makeText(this,"Veillez utiliser une carte ssmarty",Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            final String univ;
            final String facDep;
            final String promo;
            final String annee;
            final String codeDacce;
            final String nomEtPrenom;
            final String pass1;
            String pass="";
            univ =dataFromNfcCard[0];
            facDep=dataFromNfcCard[1];
            promo=dataFromNfcCard[2];
            annee=dataFromNfcCard[3];
            codeDacce=dataFromNfcCard[4];
            nomEtPrenom=dataFromNfcCard[5];
            if (codeDacce.equals("01")){
                pass=dataFromNfcCard[6];
            }
            Intent studentIntent;


            //je définis l'accées suivant le code d acces 1=Etudiant 1..= Etud-Chef 2-prof
            switch (codeDacce) {
                case "1": //Etudiant     1
                    dataTosend= new String[]{univ, nomEtPrenom , facDep+"_"+promo +"_"+annee};
                    studentIntent=new Intent(MainActivity.this, MainActivity_student.class);
                    studentIntent.putExtra("data",dataTosend);
                    //studentIntent.putExtra("userName",dataTosend)
                    startActivity(studentIntent);
                break;

                case "0" :// CP ADJOINT
                    dataTosend= new String[]{univ,nomEtPrenom };

                    studentIntent=new Intent(MainActivity.this, MainActivity_Prof.class);
                    studentIntent.putExtra("data",dataTosend);
                    //studentIntent.putExtra("userName",dataTosend)
                    startActivity(studentIntent);
                    break;

                case "01" :




                    pass1=pass;


                                        //Intent studentIntent;
                                       // dataTosend= new String[]{univ,nomEtPrenomCp.getText().toString() };

                                        studentIntent=new Intent(MainActivity.this, ActivityIDCp.class);
                                        studentIntent.putExtra("univ",univ);
                                        studentIntent.putExtra("pass",pass1);
                                        studentIntent.putExtra("facCP",facDep);
                                        studentIntent.putExtra("promoCP",promo);
                                        //studentIntent.putExtra("userName",dataTosend)
                                        startActivity(studentIntent);

                                    //}


                    break;
            }

        }
    }

//    public int alertBox (){
//        final int[] retour = {0};
//        AlertDialog.Builder builder= new AlertDialog.Builder(this);
//
//        builder.setMessage(R.string.messageDeChoixDinterface).setCancelable(false).setPositiveButton("Etudiant", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                retour[0] =1;
//                dialogInterface.cancel();
//            }
//        }).setNegativeButton("Administratif", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//                retour[0] =2;
//            }
//        }).setCancelable(true);
//        builder.create().show();
//
//
//
//
//        return retour[0];
//    }



}
