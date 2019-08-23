package ssmarty.univ.database.file;

import android.app.Activity;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class ListePresenceToFile {
    String nomeEditeur,date,fac,type,list,cours,promo;


    public ListePresenceToFile(String nomeEditeur, String date, String fac, String type ,String list, String cours,String promo) {
        this.nomeEditeur = nomeEditeur;
        this.date = date;
        this.fac = fac;
        this.list = list;
        this.type = type;
        this.cours=cours;
        this.promo=promo;
    }

    public ListePresenceToFile() {

    }

    public String getList() {
        return list;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getCours() {
        return cours;
    }

    public void setCours(String cours) {
        this.cours = cours;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getNomeEditeur() {
        return nomeEditeur;
    }

    public void setNomeEditeur(String nomeEditeur) {
        this.nomeEditeur = nomeEditeur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFac() {
        return fac;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
