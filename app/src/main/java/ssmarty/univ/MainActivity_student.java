package ssmarty.univ;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ssmarty.univ.database.file.ImageSaver;

public class MainActivity_student extends AppCompatActivity {

    private ImageView btnHoraire, btnExam, btnComFac, btnComUniv;
    Intent switchActiv;
    TextView displayUnivName,displayStudentName,searchStudentName,searchEditorName,searchMessage;
    //SearchView researchFinace;
    String univName,facDep,promo,annee;
    Intent switch_prof_acti;
    ImageView unvLogo;
    String localPathFileStorage;
    ImageSaver imageSaver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student2);
        btnComFac=findViewById(R.id.imgbtn_my_listes);
        btnComUniv=findViewById(R.id.imgbtn_com_univ);
        btnExam=findViewById(R.id.imgbtn_communi_prof);
        btnHoraire=findViewById(R.id.imgbtn_liste);
        displayUnivName=findViewById(R.id.studen_txtNom_univ);
        displayStudentName=findViewById(R.id.nom_etudiant);
        searchEditorName =findViewById(R.id.txtediteurSituationFinance);
        searchMessage=findViewById(R.id.messageSituationFinance);
        searchStudentName=findViewById(R.id.txtNomEtudiantSituationFinance);
        unvLogo=findViewById(R.id.imageViewLogoUniv);

//        researchFinace= findViewById(R.id.recherchSituationFinance);
//        researchFinace.setVisibility(View.VISIBLE);

       // setSearchWidgetInvisible();
        
        //todo get student's name and uniV n
        String []getDataFromCard = getIntent().getStringArrayExtra("data");
        displayUnivName.setText(getDataFromCard[0]+"");
        displayStudentName.setText(getDataFromCard[1]+" - ");
        String promoYear = getDataFromCard[2];
        String[] facPromoYear = promoYear.split("_");
        univName=getDataFromCard[0];
        facDep= facPromoYear[0]+"_"+facPromoYear[1];
        promo=facPromoYear[2];
        annee=facPromoYear[3];
        imageSaver = new ImageSaver(getApplicationContext());

        localPathFileStorage=getApplicationContext().getFilesDir().getPath();
        File file = new File(getApplicationContext().getFilesDir(),univName+".png");
        if(file.exists()){
            //Do something
            //Toast.makeText(this,"file"+univName+" existe",Toast.LENGTH_LONG).show();
            //imageSaver.load();
            //loadImageFromStorage(getApplicationContext().getFilesDir().getPath(),univName);

        }
        else{
            //Nothing
            //Toast.makeText(this,"file"+univName+" No ",Toast.LENGTH_LONG).show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://ssmartyuniv.appspot.com/"+univName).child(univName+".png");

            try {
                final File localFile = File.createTempFile(univName, "png");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        unvLogo.setImageBitmap(bitmap);
                        //imageSaver.save(bitmap);
                        //saveToInternalStorage(bitmap,univName);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            } catch (IOException e ) {}
        }



//        researchFinace.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                setSearchWidgetInvisible();
//            }
//        });
//        researchFinace.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String getResearchTxt= String.valueOf(researchFinace.getInputType());
//                searchInDB(getResearchTxt);
//            }
//        });
//
        displayUnivName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_prof_acti=new Intent(MainActivity_student.this,Activity_contatUniv_prof.class);
                switch_prof_acti.putExtra("data_nom_univ",univName);
                startActivity(switch_prof_acti);
            }
        });
                //boutton Horaire
        btnHoraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, HoraireActivity.class);
                switchActiv.putExtra("promo",facDep+" "+promo+"."+annee);
                switchActiv.putExtra("fac",facDep);
                switchActiv.putExtra("promotion",promo);
                switchActiv.putExtra("univ_name",univName);
                startActivity(switchActiv);

            }
        });
        //bouton exam
        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, ExamActivity.class);
                switchActiv.putExtra("promo",facDep+" "+promo+"."+annee);
                switchActiv.putExtra("fac",facDep);
                switchActiv.putExtra("promotion",promo);
                switchActiv.putExtra("univ_name",univName);
                startActivity(switchActiv);

            }
        });

        //bouton communication fac
        btnComFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, CommuFacActivity.class);
                switchActiv.putExtra("promo",facDep+" "+promo+"."+annee);
                switchActiv.putExtra("fac",facDep);
                switchActiv.putExtra("promotion",promo);
                switchActiv.putExtra("univ_name",univName);
                startActivity(switchActiv);

            }
        });

        //btn com univ

        btnComUniv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiv= new Intent(MainActivity_student.this, CommuUnivActivity.class);
                switchActiv.putExtra("promo",facDep+" "+promo+"."+annee);
                switchActiv.putExtra("fac",facDep);
                switchActiv.putExtra("promotion",promo);
                switchActiv.putExtra("univ_name",univName);
                startActivity(switchActiv);
            }
        });
    }

    private void loadImageFromStorage(String path,String univName)
    {

        try {
            File f=new File(path, univName+".png");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            unvLogo.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    private String saveToInternalStorage(Bitmap bitmapImage,String univName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("files", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,univName+".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    
    public void searchInDB(String matricu)
    {
        //todo search in DB 
        
        
        //TODO gET RESULTES AND DISPLAYS
        
        setSearchWidgetVisible();
        
    }
    
    public void setSearchWidgetInvisible(){
        //set all research widget affiliated invible
        searchStudentName.setVisibility(View.INVISIBLE);
        searchMessage.setVisibility(View.INVISIBLE);
        searchEditorName.setVisibility(View.INVISIBLE);
        
    }
    public void setSearchWidgetVisible(){
        //set all research widget affiliated invible
        searchStudentName.setVisibility(View.VISIBLE);
        searchMessage.setVisibility(View.VISIBLE);
        searchEditorName.setVisibility(View.VISIBLE);

    }

}
