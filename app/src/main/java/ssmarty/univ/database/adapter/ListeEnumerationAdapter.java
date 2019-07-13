package ssmarty.univ.database.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import ssmarty.univ.R;
import ssmarty.univ.database.model.ListeEnumerationModel;

public class ListeEnumerationAdapter extends ArrayAdapter<ListeEnumerationModel> {

    //the list values in the List of type hero
    List<ListeEnumerationModel> listPresencec;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public ListeEnumerationAdapter(Context context, int resource, List<ListeEnumerationModel> listPresencec) {
        super(context, resource, listPresencec);
        this.context = context;
        this.resource = resource;
        this.listPresencec = listPresencec;
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
        TextView txtNameDate = view.findViewById(R.id.txtNomDateListeProf);
        TextView txtIntitule = view.findViewById(R.id.txtIntitulePresent);
        TextView txtNomprePresence = view.findViewById(R.id.txtNombrePresence);
        ImageButton btnSendMyListCloud = view.findViewById(R.id.imgbtnSendCloudProfList);
        ImageButton btnSeeMylist = view.findViewById(R.id.imgSeeMyList);
        ImageButton btnModifyMyList = view.findViewById(R.id.imgModifyMyList);
        //txtIntitule.setMovementMethod(new ScrollingMovementMethod());

        //getting the hero of the specified position
        final ListeEnumerationModel listeEnumerationModel = listPresencec.get(position);


        if(listeEnumerationModel.getEtat().equals("oui")){
            btnSendMyListCloud.setImageResource(R.drawable.ic_cloud_done_black_24dp);
            btnSendMyListCloud.setEnabled(false);
        }
        else {
            btnSendMyListCloud.setImageResource(R.drawable.ic_cloud_off_black_24dp);
            btnSendMyListCloud.setEnabled(true);
        }

        //adding values to the list item
        txtNameDate.setText(listeEnumerationModel.getNomDateListeProf());
        txtIntitule.setText(listeEnumerationModel.getIntitulePresent());
        txtNomprePresence.setText(listeEnumerationModel.getNombrePresence());


        //adding a click listener to send data to server
        btnSendMyListCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                //removeHero(position);
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
            }
        });


        //finally returning the view
        return view;
    }


}


