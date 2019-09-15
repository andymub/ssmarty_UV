package ssmarty.univ.QR;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import ssmarty.univ.ActivityIDCp;
import ssmarty.univ.MainActivity;
import ssmarty.univ.MainActivity_Prof;
import ssmarty.univ.MainActivity_student;

import static android.Manifest.permission.CAMERA;

public class QrCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView mScannerView;
    String []dataTosend;
     String univ;
     String facDep;
     String promo;
     String annee;
     String codeDacce;
     String nomEtPrenom;
     String pass1;
    Intent studentIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "onCreate");

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                //Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();

            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        final String result = rawResult.getText();
        Log.d("QRCodeScanner", rawResult.getText());
        Log.d("QRCodeScanner", rawResult.getBarcodeFormat().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                startActivity(browserIntent);
            }
        });
        builder.setMessage(rawResult.getText());
        AlertDialog alert1 = builder.create();
        //alert1.show();
        String [] dataFromNfcCard= rawResult.getText().split("/");
        if (dataFromNfcCard.length < 6){
            Toast.makeText(getApplicationContext(), "Not a ssmarty QR code", Toast.LENGTH_LONG).show();
            alert1.dismiss();
            onResume();
        }
        else {
            univ = dataFromNfcCard[0];
            facDep = dataFromNfcCard[1];
            promo = dataFromNfcCard[2];
            annee = dataFromNfcCard[3];
            codeDacce = dataFromNfcCard[4];
            nomEtPrenom = dataFromNfcCard[5];
            if (codeDacce.equals("1")) {

                dataTosend = new String[]{univ, nomEtPrenom, facDep + "_" + promo + "_" + annee};
                studentIntent = new Intent(this, MainActivity_student.class);
                studentIntent.putExtra("data", dataTosend);
                //studentIntent.putExtra("userName",dataTosend)
                startActivity(studentIntent);
            }
            else if (codeDacce.equals("0")){
                dataTosend= new String[]{univ,nomEtPrenom };

                studentIntent=new Intent(this, MainActivity_Prof.class);
                studentIntent.putExtra("data",dataTosend);
                //studentIntent.putExtra("userName",dataTosend)
                startActivity(studentIntent);
            }
            else if (codeDacce.equals("01")){
                pass1=dataFromNfcCard[6];
                studentIntent=new Intent(this, ActivityIDCp.class);
                studentIntent.putExtra("univ",univ);
                studentIntent.putExtra("pass",pass1);
                studentIntent.putExtra("facCP",facDep);
                studentIntent.putExtra("promoCP",promo);
                startActivity(studentIntent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Seul l'accès NFC est requise pour vous", Toast.LENGTH_LONG).show();
                alert1.dismiss();
                onResume();
            }

        }
    }
    private boolean checkPermission() {
        return ( ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(QrCodeScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Fermer", null)
                .create()
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(mScannerView == null) {
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }
}
