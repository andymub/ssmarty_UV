package ssmarty.univ.model;

public class HoraireCourOuExam {
    String nomCourExam,lieu, titulaire,date,autres;

    public HoraireCourOuExam(String nomCourExam, String lieu, String titulaire, String date, String autres) {
        this.nomCourExam = nomCourExam;
        this.lieu = lieu;
        this.titulaire = titulaire;
        this.date = date;
        this.autres = autres;
    }

    public HoraireCourOuExam(HoraireCourOuExam courModel) {
        this.nomCourExam = courModel.nomCourExam;
        this.lieu = courModel.lieu;
        this.titulaire = courModel.titulaire;
        this.date = courModel.date;
        this.autres = courModel.autres;
    }

    public HoraireCourOuExam() {

    }

    public HoraireCourOuExam(String examen) {
    }

    public String getNomCourExam() {
        return nomCourExam;
    }

    public void setNomCourExam(String nomCourExam) {
        this.nomCourExam = nomCourExam;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(String titulaire) {
        this.titulaire = titulaire;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAutres() {
        return autres;
    }

    public void setAutres(String autres) {
        this.autres = autres;
    }
}
