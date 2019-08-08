package ssmarty.univ;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ssmarty.univ.adapter.ListCommUnivAdapter;
import ssmarty.univ.database.model.MessageUniv;
import ssmarty.univ.model.HoraireCourOuExam;

public class CommuUnivActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commu_univ);


    }
}
