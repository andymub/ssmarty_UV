package ssmarty.univ;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import java.util.List;

public class Activity_Communi_Prof extends ActivityGroup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__communi__prof);
        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost_prof);
        tabHost.setup(this.getLocalActivityManager());


        String userName =getIntent().getStringExtra("data_nom_user");
        List<String> listFac;

        String getListFacDetIntent= getIntent().getStringExtra("list_fac");
        String getListUnivName = getIntent().getStringExtra("data_nom_univ");

        //intent
        Intent intentFac =new Intent(this,TabFacCommuni.class);
        intentFac.putExtra("data_nom_user",userName);
        intentFac.putExtra("list_fac",getListFacDetIntent);
        intentFac.putExtra("data_nom_univ",getListUnivName);

        Intent intentUniv =new Intent(this,TabUnivCommuni.class);
        intentUniv.putExtra("data_nom_user",userName);
        intentUniv.putExtra("data_nom_univ",getListUnivName);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Message Faculté");
        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(R.color.Tab);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Message à l'Univerité");
        tab2.setContent(intentFac);
        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("Message à la Fac/Département");
        //tab1.setContent(new Intent(this,TabFacCommuni.class));
        tab1.setContent(intentFac);

        tab2.setIndicator("Message à l'Univerité");
//        tab2.setContent(new Intent(this,TabUnivCommuni.class));
        tab2.setContent(intentUniv);

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

    }
}
