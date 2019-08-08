package ssmarty.univ.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import ssmarty.univ.R;
import ssmarty.univ.model.HoraireCourOuExam;

public class ListHoraireEtudiantAdapter extends ArrayAdapter<HoraireCourOuExam> {
    //the list values in the List of type hero
    List<HoraireCourOuExam> horaireList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public ListHoraireEtudiantAdapter(Context context, int resource, List<HoraireCourOuExam> horaireList) {
        super(context, resource, horaireList);
        this.context = context;
        this.resource = resource;
        this.horaireList = horaireList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view =layoutInflater.inflate(resource,null,false);
        TextView viewNomCourHoraire = view.findViewById(R.id.txtCoursName);
        TextView  viewDate=view.findViewById(R.id.txtDateHoraireExam);
        TextView viewLieuHoraireCour= view.findViewById(R.id.txtlieuCourHoraireExam);
        TextView viewTitulaire =view.findViewById(R.id.txtTitulaireCours);
        TextView viewAutresInfoCours =view.findViewById(R.id.edittxtAutreRaison);

        HoraireCourOuExam horaireCour= horaireList.get(position);

        viewNomCourHoraire.setText(horaireCour.getNomCourExam());
        viewTitulaire.setText(horaireCour.getTitulaire());
        viewDate.setText(horaireCour.getDate());
        viewLieuHoraireCour.setText(horaireCour.getLieu());
        viewAutresInfoCours.setText(horaireCour.getAutres());



        return view;
    }
}
