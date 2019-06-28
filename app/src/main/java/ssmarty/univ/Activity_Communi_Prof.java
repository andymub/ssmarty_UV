package ssmarty.univ;

import android.app.ActivityGroup;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class Activity_Communi_Prof extends ActivityGroup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__communi__prof);
        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost_prof);
        tabHost.setup(this.getLocalActivityManager());


        TabHost.TabSpec tab1 = tabHost.newTabSpec("Message à la Fac/Département");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Message à l'Univerité");
        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("Message à la Fac/Département");
        tab1.setContent(new Intent(this,TabFacCommuni.class));

        tab2.setIndicator("Message à l'Univerité");
        tab2.setContent(new Intent(this,TabUnivCommuni.class));


        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

    }
}
