package ssmarty.univ.database.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.Joiner;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ssmarty.univ.Activity_liste_presence;
import ssmarty.univ.R;
import ssmarty.univ.database.file.ListePresenceToFile;
import ssmarty.univ.database.model.ListeEnumerationModel;
import ssmarty.univ.helper.DatabaseHelper;

public class MyListePresenceAdapter extends ArrayAdapter<ListeEnumerationModel> {

    //the list values in the List of type hero
    List<ListeEnumerationModel> listPresencec;
    public ArrayAdapter<String> adapter;
    public List<String> ListElementsArrayList;

    //activity context
    Context context;
    String facDep,promo,nomCour,typeCHoraire,nomProf,date,getNameDate;
    FileOutputStream fstream;
    public String nomFichier;
    public EditText edttxRaisonAddSudent, edtxtAddNameStudent;
    ImageButton btnStart_addStudents, btnSaveOfline_addStudents, btnAddToListview;
    String ListOfStudentToAdd;
    ImageView imgCloseDialogue;
    ListView listView_addStudents;
    LinearLayout linearlayout_adstudents;
    public String downloadUrl;
    private StorageReference mStorageRef;
    public String nomUniv,TAG="TAG";
    public ListeEnumerationModel listeEnumerationModel;
    int value=0;
    ToneGenerator toneGen1;
    int raisonEmpty=0;
    //public ProgressBar progressBar;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public MyListePresenceAdapter(Context context, int resource, List<ListeEnumerationModel> listPresencec, String getIntentNomUniv) {
        super(context, resource, listPresencec);
        this.context = context;
        this.resource = resource;
        this.listPresencec = listPresencec;
        this.nomUniv=getIntentNomUniv;

    }
    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        final TextView txtNameDate = view.findViewById(R.id.txtNomDateListeProf);
        TextView txtIntitule = view.findViewById(R.id.txtIntitulePresent);
        TextView txtNomprePresence = view.findViewById(R.id.txtNombrePresence);
        final ImageButton btnSendMyListCloud = view.findViewById(R.id.imgbtnSendCloudProfList);
        ImageButton btnSeeMylist = view.findViewById(R.id.imgSeeMyList);
        ImageButton btnModifyMyList = view.findViewById(R.id.imgModifyMyList);
        LinearLayout linnear_custom_all_button= view.findViewById(R.id.linnear_my_custom_all);
        //progressBar= view.findViewById(R.id.progressBar2);
        //txtIntitule.setMovementMethod(new ScrollingMovementMethod());


        //getting the hero of the specified position
        listeEnumerationModel = listPresencec.get(position);


        if(listeEnumerationModel.getEtat().equals("oui")){
            btnSendMyListCloud.setImageResource(R.drawable.ic_cloud_done_black_24dp);
            btnSendMyListCloud.setClickable(false);
        }
        else {
            btnSendMyListCloud.setImageResource(R.drawable.ic_cloud_off_black_24dp);
            btnSendMyListCloud.setClickable(true);
        }

        //adding values to the list item
        txtNameDate.setText(listeEnumerationModel.getNomDateListeProf());
        txtIntitule.setText(listeEnumerationModel.getIntitulePresent());
        txtNomprePresence.setText(listeEnumerationModel.getNombrePresence());

        if (txtNomprePresence.getText().length()==0
                || txtIntitule.getText().equals(" ")
                ||txtNomprePresence.getText().equals(" ")){
           //linnear_custom_all_button.setVisibility(View.INVISIBLE);
            btnSendMyListCloud.setVisibility(View.INVISIBLE);
            btnSeeMylist.setVisibility(View.INVISIBLE);
            btnModifyMyList.setVisibility(View.INVISIBLE);
        }
        else{
            //linnear_custom_all_button.setVisibility(View.VISIBLE);
        }

        View v = convertView;
        //adding a click listener to send data to server
        btnSendMyListCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                //removeHero(position);
                boolean state =false;
                final int[] i = {0};
                //progressBar.setVisibility(View.VISIBLE);
                btnSendMyListCloud.setImageResource(R.drawable.ic_cloud_done_black_24dp);
                splitIntituler(listeEnumerationModel.getIntitulePresent(),listeEnumerationModel.getNomDateListeProf());

                String nomFichier ="Liste présence,"+nomProf+" Cours:"+
                        nomCour+".text";
                ListePresenceToFile listePresenceToFile =new ListePresenceToFile();
                listePresenceToFile.setType(typeCHoraire);
                listePresenceToFile.setDate(date);
                listePresenceToFile.setFac(facDep);
                listePresenceToFile.setPromo(promo);
                listePresenceToFile.setCours(nomCour);
                listePresenceToFile.setNomeEditeur(nomProf);
                listePresenceToFile.setList(convertListToString(listeEnumerationModel.getMyliste()));

                try {
                    fstream = context.openFileOutput("Liste présence,"+nomProf+" Cours:"+
                            nomCour+".text", Context.MODE_PRIVATE);
                    fstream.write((listePresenceToFile.getType()+"--").getBytes());
                    fstream.write((listePresenceToFile.getNomeEditeur()+"--").getBytes());
                    fstream.write((listePresenceToFile.getDate()+"--").getBytes());
                    fstream.write((listePresenceToFile.getCours()+"--").getBytes());
                    fstream.write((listePresenceToFile.getFac()+"--").getBytes());
                    fstream.write((listePresenceToFile.getPromo()+"--").getBytes());
                    fstream.write((listePresenceToFile.getList()+"--").getBytes());
                    fstream.close();

                    //intent = new Intent(Activity_liste_presence.this,MainActivity_Prof.class);

                    //upload file to serveur
                    // Create a storage reference from our app

// Create a storage reference from our app
                    mStorageRef = FirebaseStorage.getInstance().getReference();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy");
                    String strDate = mdformat.format(calendar.getTime());

                    //File files= new File()
                    //File directory = this.getFilesDir();
//            File fileN = new File(directory, nomFichier);
                    String path =context.getApplicationContext().getFilesDir().getPath();
//            Uri file = Uri.fromFile(new File("/data/data/ssmarty.univ/files/"+nomFichier));
                    Uri file = Uri.fromFile(new File(path+"/"+nomFichier));
                    StorageReference riversRef = mStorageRef.child(nomUniv+"/"+strDate+"/"+facDep+"/"+promo+"/"+nomFichier);
                    //StorageReference riversRef = mStorageRef.child("images/12/");

                    riversRef.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    //downloadUrl = taskSnapshot.getUploadSessionUri();
                                    i[0] =1;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    int o = 1;
                                    // ...
                                }
                            });
                    downloadUrl=riversRef.getPath();

                    if (i[0]==0){
                        // Access a Cloud Firestore instance from your Activity
                        String collection = nomUniv+"/"+facDep+"/"+promo;
                        String document ="Listes de présence";
                        Map<String, Object> uploadedFile = new HashMap<>();
                        uploadedFile.put(""+nomFichier,downloadUrl);
                        //db.collection("12").document(document)
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        // Hierarchical Data with Subcollection-Document in a Document
                        //DocumentReference androidTutRef =
                        db.collection(nomUniv).document(facDep)
                                .collection(promo).document(document)
                                .set(uploadedFile, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                        DatabaseHelper databaseHelper= new DatabaseHelper(context);
                        int res= databaseHelper.onUpgradeState(listeEnumerationModel.getNomDateListeProf(),nomUniv);
                        if (res==1){
                            btnSendMyListCloud.setImageResource(R.drawable.ic_cloud_done_black_24dp);
                            Toast.makeText(context.getApplicationContext(), "Liste sauvergardée",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            btnSendMyListCloud.setImageResource(R.drawable.ic_cloud_off_black_24dp);
                        }

                    }


                    //startActivity(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(context.getApplicationContext(), "Exception "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });

        //adding a click listener to see ALL LISTE IN DIALOGUE
        btnSeeMylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                //removeHero(position);

                final String[] value=listeEnumerationModel.getMyliste();
                AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(context);


                String[] date= listeEnumerationModel.getNomDateListeProf().split(" - ");
                alertdialogbuilder.setTitle("Liste de présence, "+date[1]);

                alertdialogbuilder.setItems(listeEnumerationModel.getMyliste(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedText = Arrays.asList(value).get(which);

                        //textview.setText(selectedText);

                    }
                });

                AlertDialog dialog = alertdialogbuilder.create();

                dialog.show();
            }
        });
        //adding a click listener to Modify
        btnModifyMyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                //removeHero(position);
                getNameDate=txtNameDate.getText().toString();
                showDialog(context);
            }
        });



        //finally returning the view
        return view;
    }

    public void splitIntituler(String myIntitule,String myNomInt){
        String [] myReturn;
        String [] myReturn2;
        myIntitule=myIntitule.replace("|",",");
        myReturn=myIntitule.split(",");
        myReturn2=myReturn[0].split("-");
        facDep=myReturn2[0];
        promo=myReturn2[1];
        myReturn2=myReturn[1].split("/");
        nomCour=myReturn2[1];
        typeCHoraire=myReturn2[0];
        myReturn=myNomInt.split("-");
        nomProf=myReturn[0];
        date=myReturn[1];


    }

    private static final String LIST_SEPARATOR = ",";

    public static String convertListToString(String[] array) {
        String result = "";

        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();

            for (String s : array) {
                sb.append(s).append(",");
            }

            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    public void showDialog(final Context context) {  //TODO FINIR AVEC REMPLISSAGE LISTE NFC

        String[] ListElements=new String[] {};
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_students_dialogue_box);

        btnStart_addStudents = dialog.findViewById(R.id.imgbtnStarList_addstudents);
        btnSaveOfline_addStudents = dialog.findViewById(R.id.imgbtn_add_students_list);
        btnAddToListview=dialog.findViewById(R.id.imgBtnAddStudent);
        edttxRaisonAddSudent = dialog.findViewById(R.id.editxt_addstudents);
        edtxtAddNameStudent = dialog.findViewById(R.id.edtxtAddDialogueName);
        listView_addStudents = dialog.findViewById(R.id.listView_addstudents);
        linearlayout_adstudents = dialog.findViewById(R.id.linearlayout_adstudents);
        imgCloseDialogue=dialog.findViewById(R.id.imageViewcloseDialogue);
        //btnSaveOfline_addStudents.setVisibility(View.INVISIBLE);

        btnSaveOfline_addStudents.setVisibility(View.INVISIBLE);

        btnSaveOfline_addStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(ListElementsArrayList.isEmpty())) {
                    String value="";
                    String[] value1 = listeEnumerationModel.getMyliste();
                    for (int i=0; i<value1.length; i++)
                    {
                        value =value+","+value1[i];
                    }

                    String[] stringArray = new String[listView_addStudents.getCount()];
                    // convert in string
                    // use join() method
                    String dataToAdd = Joiner.on(",").join(ListElementsArrayList);
                    value = value + "," +
                            "AJOUT°°" + edttxRaisonAddSudent.getText().toString()
                            + "°°" + dataToAdd;

                    DatabaseHelper databaseHelper=new DatabaseHelper(context);
                    String univ=nomUniv;
                    String nomProfDategetNameDate;
                    databaseHelper.onUpDateLisField(nomUniv,getNameDate,value);

                    edttxRaisonAddSudent.setText("");
                    edtxtAddNameStudent.setText("");

                    dialog.cancel();



                }
            }
        });

        listView_addStudents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                if (!(ListElementsArrayList.isEmpty()))
                {
                ListElementsArrayList.remove(position);
                adapter.notifyDataSetChanged();
                value--;
                }

                return false;
            }

        });

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));
        adapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_list_item_1, ListElementsArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                final TextView tv = view.findViewById(android.R.id.text1);
                if(position == ListElementsArrayList.size()-1) {
                    // Set the text color of TextView (ListView Item)
                    linearlayout_adstudents.setBackgroundResource(R.drawable.listview_add_dorder);
                    //tv.setTextColor(getResources().getColor(R.color.bipNfc_etudiant_added));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // linearlayout_adstudents.setBackgroundResource(R.color.WHITE_nfc);
                            linearlayout_adstudents.setBackgroundResource(R.drawable.listview_border);
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

        listView_addStudents.setAdapter(adapter);

        dialog.show();
        imgCloseDialogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        btnStart_addStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edttxRaisonAddSudent.getText().toString().equals("")){
                    Toast.makeText(context,"Raison d'ajout...",Toast.LENGTH_LONG).show();
                }
                else
                {

                    if (raisonEmpty==0){
                    raisonEmpty=1;
                    btnStart_addStudents.setImageResource(R.drawable.ic_done_bleu_24dp);
                    edttxRaisonAddSudent.setEnabled(false);
                        btnAddToListview.setVisibility(View.VISIBLE);
                        //btnSaveOfline_addStudents.setVisibility(View.VISIBLE);
                    }
                    else if (raisonEmpty==1){
                        btnStart_addStudents.setImageResource(R.drawable.ic_done_one_24dp);
                        edttxRaisonAddSudent.setEnabled(true);
                        btnAddToListview.setVisibility(View.INVISIBLE);
                        //btnSaveOfline_addStudents.setVisibility(View.INVISIBLE);
                        ListElementsArrayList.clear();
                        adapter.notifyDataSetChanged();
                        value=0;
                        raisonEmpty=0;

                    }


                }
            }
        });
        edttxRaisonAddSudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edttxRaisonAddSudent.length()<0){

                }
                else  {

                };

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edttxRaisonAddSudent.getText().length()>0){
                    btnAddToListview.setVisibility(View.VISIBLE);
                }
                else  btnAddToListview.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnAddToListview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtxtAddNameStudent.getText().toString().equals("")||edtxtAddNameStudent.getText().equals(null)){
                    Toast.makeText(context,"Nom vide",Toast.LENGTH_LONG).show();
                }
                else {
                    String studentName =  edtxtAddNameStudent.getText().toString();
                    listView_addStudents.setVisibility(View.VISIBLE);
                    btnSaveOfline_addStudents.setVisibility(View.VISIBLE);

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
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    //GetValue= value+++".";
                    ListElementsArrayList.add(value+++"."+studentName);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ///nfc




        //end nfc
    }





}


