package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListCommFacAdapter;
import ssmarty.univ.database.model.MessageUniv;

public class CommuFacActivity extends AppCompatActivity {

    TextView promo;
    ListView listView;

    //List of Horaire model
    List<MessageUniv> messageCommList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_fac);
        String facDep=getIntent().getStringExtra("promo");


        promo=findViewById(R.id.txtCommfacPromo);
        listView=findViewById(R.id.listViewCommfac);
        messageCommList=new ArrayList<>();


        promo.setText(facDep);
        messageCommList.add(new MessageUniv("Message Univ Fac 1","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Fac 1 infos","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("infos fac 1","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("prévision Exam","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Congé Prod Mugisho","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        ListCommFacAdapter listCommFacAdapter =new ListCommFacAdapter(this,
                R.layout.my_custum_list_communication_univ,messageCommList);
        listView.setAdapter(listCommFacAdapter);

    }

}
