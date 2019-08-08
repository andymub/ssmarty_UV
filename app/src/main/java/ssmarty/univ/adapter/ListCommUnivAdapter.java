package ssmarty.univ.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ssmarty.univ.R;
import ssmarty.univ.database.model.MessageUniv;

public class ListCommUnivAdapter extends ArrayAdapter {
   List<MessageUniv> messageUnivListe;

   Context context;

   int resource;
   TextView messageComm,titreComm,editeurComm;


    public ListCommUnivAdapter(Context context, int resource, List<MessageUniv> messageUnivListe) {
        super(context, resource, messageUnivListe);
        this.context=context;
        this.resource=resource;
        this.messageUnivListe=messageUnivListe;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view =layoutInflater.inflate(resource,null,false);
        titreComm=view.findViewById(R.id.txtTitreMessageCom);
        editeurComm=view.findViewById(R.id.txtEditeurMessageComm);
        messageComm=view.findViewById(R.id.txtMessageComm);

        MessageUniv messageUniv =messageUnivListe.get(position);

        titreComm.setText(messageUniv.getTitre());
        editeurComm.setText(messageUniv.getEditeur());
        messageComm.setText(messageUniv.getMessage());


        return view;
    }
}
