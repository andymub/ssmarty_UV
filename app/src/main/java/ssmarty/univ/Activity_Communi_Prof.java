package ssmarty.univ;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import java.util.List;

public class Activity_Communi_Prof extends ActivityGroup {

    public String getListFacDetIntent,promoCP1,getPromoCP1,facCP;
    Boolean isACp=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__communi__prof);
        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost_prof);
        tabHost.setup(this.getLocalActivityManager());

        if ((getIntent().getStringExtra("facCP"))!=null){
            facCP=getIntent().getStringExtra("facCP");
            isACp=true;

        }

        String userName =getIntent().getStringExtra("data_nom_user");
        List<String> listFac;

        String getListFacDetIntent= getIntent().getStringExtra("list_fac");
        String getListUnivName = getIntent().getStringExtra("data_nom_univ");
        String sss=getIntent().getStringExtra("promoCP1");
        getPromoCP1=getIntent().getStringExtra("promoCP1");

        //intent
        Intent intentFac =new Intent(this,TabFacCommuni.class);
        if (!isACp){
        intentFac.putExtra("data_nom_user",userName);
        intentFac.putExtra("list_fac",getListFacDetIntent);
        intentFac.putExtra("data_nom_univ",getListUnivName);
        intentFac.putExtra("promoCP","1");}
        else if (isACp){
            intentFac.putExtra("data_nom_user",userName);
            intentFac.putExtra("data_nom_univ",getListUnivName);
            intentFac.putExtra("list_fac",facCP);
            intentFac.putExtra("promoCP","01");
            intentFac.putExtra("promoCP1",sss);
        }

        Intent intentUniv =new Intent(this,TabUnivCommuni.class);
        intentUniv.putExtra("data_nom_user",userName);
        intentUniv.putExtra("data_nom_univ",getListUnivName);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Message Faculté");
        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(R.color.Tab);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Message à l'Univerité");
        tab2.setContent(intentFac);
        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("Faculté");
        //tab1.setContent(new Intent(this,TabFacCommuni.class));
        tab1.setContent(intentFac);

        tab2.setIndicator("Univerité");
//        tab2.setContent(new Intent(this,TabUnivCommuni.class));
        tab2.setContent(intentUniv);

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

    }
}
