package ssmarty.univ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListCommFacAdapter;
import ssmarty.univ.adapter.ListCommUnivAdapter;
import ssmarty.univ.database.model.MessageUniv;
import ssmarty.univ.network.InternetConnectionStatus;

public class CommuUnivActivity extends AppCompatActivity {

    TextView promo;
    ListView listView;
    ProgressBar progressBar;

    //List of Horaire model
    List<MessageUniv> messageCommList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<String[]> listOfMessage;
    public List<MessageUniv> listeOfMessageUniv;
    public List<String> listeOfFacDep;
    public MessageUniv myMessageUniv;
    SwipeRefreshLayout mSwipeRefreshLayout;
    InternetConnectionStatus internetConnectionStatus;
    public String nomUnivDisplayed;
    String univName, facDepart, promotion;
    //    DatabaseHelper databaseHelper =new DatabaseHelper(this);
//    //databaseHelper.insertIntoTableInfo(listFacDep);
//    String fac=databaseHelper.getAllFacDepFromLocal();
    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_univ);
        String facDep = getIntent().getStringExtra("promo");
        univName = getIntent().getStringExtra("univ_name");
        facDepart = getIntent().getStringExtra("fac");
        promotion = getIntent().getStringExtra("promotion");


        promo = findViewById(R.id.txtCommUnivPromo);
        listView = findViewById(R.id.listViewCommUniv);
        progressBar = findViewById(R.id.progressCommuUnivStudent);
        messageCommList = new ArrayList<>();
        listView.setVisibility(View.INVISIBLE);


        promo.setText(facDep);
        fetch3LastMessages(univName);
        messageCommList.add(new MessageUniv("Congé de fin d'Exam Univ", "xxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Journée cultur..", "xxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Déf. 12 juin", "xxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Crise Univ3", "xxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Job", "Offres OCHA, Coord de projet", "Prof Kalume"));
        ListCommFacAdapter listCommFacAdapter = new ListCommFacAdapter(this,
                R.layout.my_custum_list_communication_univ, messageCommList);
        listView.setAdapter(listCommFacAdapter);
    }

    public void fetch3LastMessages(String nomUniv) {
        // CollectionReference univMessage = db.collection(nomUniv+" message");

        listeOfMessageUniv = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        final MessageUniv messageUniv = new MessageUniv();
        ListCommUnivAdapter listCommUnivAdapter;

        db.collection(nomUniv + " message")
                .orderBy(FieldPath.documentId(), Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                messageUniv.setMessage(document.getString("Message"));
                                messageUniv.setEditeur(document.getString("Editeur"));
                                messageUniv.setTitre(document.getString("Titre"));
                                listeOfMessageUniv.add(new MessageUniv(document.getString("Titre"),
                                        document.getString("Message"),
                                        document.getString("Editeur")));
                            }

                            ListCommUnivAdapter listCommUnivAdapter = new ListCommUnivAdapter(getApplicationContext(),
                                    R.layout.my_custum_list_communication_univ,
                                    listeOfMessageUniv);
                            listView.setAdapter(listCommUnivAdapter);
                            listView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);


                        } else {
                            Toast.makeText(getApplicationContext(),"Aucun message",Toast.LENGTH_LONG).show();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }
}
