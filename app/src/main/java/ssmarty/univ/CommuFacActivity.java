package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListCommUnivAdapter;
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



        promo=findViewById(R.id.txtCommUnivPromo);
        listView=findViewById(R.id.listViewCommUniv);
        messageCommList=new ArrayList<>();


        promo.setText(facDep);
        messageCommList.add(new MessageUniv("Crise Univ","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Crise Univ1","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Crise Univ2","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Crise Univ3","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        messageCommList.add(new MessageUniv("Crise Univ4","xxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxx"));
        ListCommUnivAdapter listCommUnivAdapter=new ListCommUnivAdapter(this,
                R.layout.my_custum_list_communication_univ,messageCommList);
        listView.setAdapter(listCommUnivAdapter);

    }

}
