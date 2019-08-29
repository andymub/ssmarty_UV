package ssmarty.univ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ssmarty.univ.database.model.MessageUniv;
import ssmarty.univ.time.getCurrentDate;

import android.content.ClipData;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class TabFacCommuni extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 1;
    private Spinner spinnerSelecteFac,spinnerPromo;
    private EditText objectMessage, messageTextFac;
    private ImageButton btnSendFacMessag,imgBtnGalleriePhoto,imgBtnTakePhoto;
    private TextView txtSenderAndDate;
    private String nameOfSender;
    List<String> listFac;
    ImageButton btnGatlerie, btnTakePicture;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String userChoosenTask="";

    String currentPhotoPath;
    ImageView imageView14;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG="TAG";
    ProgressBar progeProgressBar;
    int count=0;
    String getUnivName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1_fac_communi);
        spinnerSelecteFac = findViewById(R.id.spinner_fac_message_Univ);
        objectMessage=findViewById(R.id.objetMessage_fac);
        messageTextFac=findViewById(R.id.editxtxMessage_fac);
        btnSendFacMessag=findViewById(R.id.btnSendFacMessage);
        txtSenderAndDate=findViewById(R.id.txtSenderDateMessage_Fac);
        imgBtnGalleriePhoto=findViewById(R.id.imgBtnGallerie);
        imgBtnTakePhoto=findViewById(R.id.imgBtntakePhotos);
        btnGatlerie=findViewById(R.id.imgBtnGallerie);
        btnTakePicture=findViewById(R.id.imgBtntakePhotos);
        spinnerPromo=findViewById(R.id.spinnerPromoCommFca);
        imageView14=findViewById(R.id.gv);
        progeProgressBar=findViewById(R.id.progressBar3);
        progeProgressBar.setVisibility(View.INVISIBLE);


        //View v = this.getLayoutInflater().inflate(R.layout.activity_tab1_fac_communi,null);
        //todo get name of sender
        nameOfSender= getIntent().getStringExtra("data_nom_user");
         getUnivName =getIntent().getStringExtra("data_nom_univ");
        //set sender name and date
        getCurrentDate getCurrentDate = new getCurrentDate();
        txtSenderAndDate.setText(nameOfSender+" - Date :"+getCurrentDate.getCurrentDate());

        String getListFacDetIntent= getIntent().getStringExtra("list_fac");
        getListFacDetIntent=getListFacDetIntent.replace("|",",");
        getListFacDetIntent= getListFacDetIntent.replaceFirst("","Choisir la Fac/Dép");
        // getListFacDetIntent= getListFacDetIntent.substring(getListFacDetIntent.indexOf(","),getListFacDetIntent.indexOf(","));
        String [] tabl=getListFacDetIntent.split(",");
        listFac= Arrays.asList(tabl);
        //Fill my spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listFac);
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
                Toast.makeText(parent.getContext(), "Choiisir une Faculté" ,          Toast.LENGTH_LONG).show();
            }


        });

        //btn send Data
        btnSendFacMessag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfMessageIsEmpty())btnSendFacMessag.setImageResource(R.drawable.ic_send_red_24dp);
                else{
                    btnSendFacMessag.setClickable(false);
                    MessageUniv messageUniv = new MessageUniv();
                    messageUniv.setTitre(objectMessage.getText().toString());
                    messageUniv.setEditeur(txtSenderAndDate.getText().toString());
                    messageUniv.setMessage(messageTextFac.getText().toString());
                    final Map<String, Object> messageToMap = new HashMap<>();
                    messageToMap.put("Titre",messageUniv.getTitre());
                    messageToMap.put("Editeur",messageUniv.getEditeur());
                    messageToMap.put("Message",messageUniv.getMessage());
                    progeProgressBar.setVisibility(View.VISIBLE);

                    db.collection(getUnivName).document(spinnerSelecteFac.getSelectedItem().toString())
                            .collection(spinnerPromo.getSelectedItem().toString()).document("Message")
                            .collection("Message")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            count++;
                                        }
                                        db.collection(getUnivName).document(spinnerSelecteFac.getSelectedItem().toString())
                                                .collection(spinnerPromo.getSelectedItem().toString()).document("Message")
                                                .collection("Message").document("Message"+count)
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
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                btnSendFacMessag.setClickable(true);
                                                progeProgressBar.setVisibility(View.INVISIBLE);
                                                btnSendFacMessag.setImageResource(R.drawable.ic_send_red_24dp);
                                                count=0;
                                                Toast.makeText(getApplicationContext(),"Message Non envoyé",Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }

                                }
                            });



//                    btnSendFacMessag.setImageResource(R.drawable.ic_send_bleu_24dp);
//                    Toast.makeText(getApplicationContext(),"Message envoyé",Toast.LENGTH_SHORT).show();
                    //TODO CHEK IF CONNECTION
                }
            }
        });

        btnGatlerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);


            }
        });

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  dispatchTakePictureIntent();
                selectImage();
            }
        });
    }

    private Boolean checkIfMessageIsEmpty(){
        Boolean chk=true;
        if ((objectMessage.length()!=0)&&(messageTextFac.length()!=0)){
            if(spinnerSelecteFac == null || (spinnerSelecteFac.getSelectedItemPosition()==0)) {
                //name = (String)spinnerName.getSelectedItem();
                Toast.makeText(getApplicationContext(),"Choisir une faculté",Toast.LENGTH_LONG).show();
                chk=true;
            }
            else if (spinnerPromo.getSelectedItemPosition()==0)
            {
                chk=true;Toast.makeText(getApplicationContext(),"Choisir une promo.",Toast.LENGTH_LONG).show();
            }
            else  chk=false;
        }

        else  {Toast.makeText(getApplicationContext(),"Remplir l'objet ou message",Toast.LENGTH_LONG).show();
        chk=true;}
        return chk;
    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File ...
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }

    public void clearFiel(){
        objectMessage.setText("");
        messageTextFac.setText("");
        spinnerSelecteFac.setSelection(0);
        spinnerPromo.setSelection(0);
        btnSendFacMessag.setImageResource(R.drawable.ic_send_black_24dp);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data!= null) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//            imageView14.setImageBitmap(imageBitmap);
//        }
//    }
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "SSMARTY_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TabFacCommuni.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(TabFacCommuni.this);


                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();

                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView14.setImageBitmap(bm);
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView14.setImageBitmap(thumbnail);
    }





}
