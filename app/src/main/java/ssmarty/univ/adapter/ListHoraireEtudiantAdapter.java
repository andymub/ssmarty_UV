package ssmarty.univ.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ssmarty.univ.R;
import ssmarty.univ.model.HoraireCourOuExam;

public class ListHoraireEtudiantAdapter extends ArrayAdapter<HoraireCourOuExam> {
    //the list values in the List of type hero
    List<HoraireCourOuExam> horaireList;

    //activity context
    Context context;

    TextView  nom, date,lieu, titulaire, details;
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
        nom = view.findViewById(R.id.txtNomCoursExamInteroStudent);
        date= view.findViewById(R.id.txtDateCoursExamInteroStudent);
        lieu = view.findViewById(R.id.txtLieuCoursExamInteroStudent);
        titulaire = view.findViewById(R.id.txtTitulaireCoursExamInteroStudent);
        details = view.findViewById(R.id.txtDetailsCoursExamInteroStudent);


        HoraireCourOuExam horaireCour= horaireList.get(position);

        if (horaireCour.getNomCourExam().equals("")){

            viewNomCourHoraire.setVisibility(View.INVISIBLE);
            viewTitulaire.setVisibility(View.INVISIBLE);
            viewDate.setText("Interrogation".toUpperCase());
            viewDate.setGravity(Gravity.CENTER);
            viewDate.setTextColor(view.getResources().getColor(R.color.black_nfc));
            viewLieuHoraireCour.setVisibility(View.INVISIBLE);
            viewAutresInfoCours.setVisibility(View.INVISIBLE);

            nom.setVisibility(View.INVISIBLE);
            titulaire.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
            lieu.setVisibility(View.INVISIBLE);
            details.setVisibility(View.INVISIBLE);
        }
        else {
            nom.setVisibility(View.VISIBLE);
            titulaire.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            lieu.setVisibility(View.VISIBLE);
            details.setVisibility(View.VISIBLE);
            viewNomCourHoraire.setText(horaireCour.getNomCourExam());
            viewTitulaire.setText(horaireCour.getTitulaire());
            viewDate.setText(horaireCour.getDate());
            viewDate.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            viewLieuHoraireCour.setText(horaireCour.getLieu());
            viewAutresInfoCours.setText(horaireCour.getAutres());
        }



        return view;
    }
}
