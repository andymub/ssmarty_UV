package ssmarty.univ;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView imgNfcState;
    private TextView txtStateNfc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context=getApplicationContext();
        imgNfcState= findViewById(R.id.imgNfcState);
        txtStateNfc=findViewById(R.id.txtStateNfc);
        ncfCompatible ();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ncfCompatible ();
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
            // Device not compatible for NFC support
            Toast.makeText(getApplicationContext(),"Votre dispositif n'est pas supporte pas la téchnologie NFC",Toast.LENGTH_LONG).show();
        }
        else{
            if (nfcAdapter.isNdefPushEnabled()){
                Toast.makeText(getApplicationContext(), R.string.ntc_activE,Toast.LENGTH_LONG).show();
                imgNfcState.setImageResource(R.mipmap.ssmart_nfc_deconnect);
                txtStateNfc.setText(R.string.ntc_activE);


            }
            else if (nfcAdapter.isEnabled()){
                Toast.makeText(getApplicationContext(),"ouvert",Toast.LENGTH_LONG).show();
               // imgNfcState.setImageResource(R.mipmap.ssmart_nfc_connect);
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.veuill_activE_Nfc,Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
            }

        }

    }
}
