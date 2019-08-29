package ssmarty.univ.database.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ListeEnumerationModel {
  String nomDateListeProf,intitulePresent,nombrePresence,etat;
  String[] myliste;


  public ListeEnumerationModel(String nomDateListeProf, String intitulePresent, String nombrePresence, String[] myliste, String etat) {
    this.nomDateListeProf = nomDateListeProf;
    this.intitulePresent = intitulePresent;
    this.nombrePresence = nombrePresence;
    this.etat=etat;
    this.myliste=myliste;
  }

  public void setIntitulePresent(String intitulePresent) {
    this.intitulePresent = intitulePresent;
  }

  public String getEtat() {
    return etat;
  }

  public void setEtat(String etat) {
    this.etat = etat;
  }

  public String getNomDateListeProf() {
    return nomDateListeProf;
  }

  public void setNomDateListeProf(String nomDateListeProf) {
    this.nomDateListeProf = nomDateListeProf;
  }

  public String getIntitulePresent() {
    return intitulePresent;
  }


  public String getNombrePresence() {
    return nombrePresence;
  }

  public void setNombrePresence(String nombrePresence) {
    this.nombrePresence = nombrePresence;
  }
  public String[] getMyliste() {
    return myliste;
  }

  public void setMyliste(String[] myliste) {
    this.myliste = myliste;
  }



  }

