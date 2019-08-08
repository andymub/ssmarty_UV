package ssmarty.univ.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ssmarty.univ.R;
import ssmarty.univ.model.HoraireCourOuExam;

public class ListExamEtudiantAdapter extends ArrayAdapter<HoraireCourOuExam> {
    //the list values in the List of type hero
    List<HoraireCourOuExam> examList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    public ListExamEtudiantAdapter(Context context, int resource, List<HoraireCourOuExam> examList) {
        super(context, resource, examList);
        this.context = context;
        this.resource = resource;
        this.examList = examList;
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

        HoraireCourOuExam ExamCour= examList.get(position);

        viewNomCourHoraire.setText(ExamCour.getNomCourExam());
        viewTitulaire.setText(ExamCour.getTitulaire());
        viewDate.setText(ExamCour.getDate());
        viewLieuHoraireCour.setText(ExamCour.getLieu());
        viewAutresInfoCours.setText(ExamCour.getAutres());



        return view;
    }
}
