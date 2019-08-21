package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListCommFacAdapter;
import ssmarty.univ.database.model.MessageUniv;

public class CommuUnivActivity extends AppCompatActivity {

    TextView promo;
    ListView listView;

    //List of Horaire model
    List<MessageUniv> messageCommList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_univ);
        String facDep=getIntent().getStringExtra("promo");



        promo=findViewById(R.id.txtCommUnivPromo);
        listView=findViewById(R.id.listViewCommUniv);
        messageCommList=new ArrayList<>();


        promo.setText(facDep);
        messageCommList.add(new MessageUniv("Congé de fin d'Exam Univ","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Journée cultur..","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Déf. 12 juin","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Crise Univ3","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Job","Offres OCHA, Coord de projet","Prof Kalume"));
        ListCommFacAdapter listCommFacAdapter =new ListCommFacAdapter(this,
                R.layout.my_custum_list_communication_univ,messageCommList);
        listView.setAdapter(listCommFacAdapter);
    }
}
