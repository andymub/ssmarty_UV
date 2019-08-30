package ssmarty.univ.database.model;

import android.text.Html;
import android.widget.Spinner;

public class MessageUniv {
    String titre,message,editeur;

    //Todo message clickable spannad
//    public MessageUniv(String titre, String message, String editeur) {
//        this.titre = titre;
//        this.message = Html.fromHtml(message);
//        this.editeur = editeur;
//
//    }
    public MessageUniv(String titre, String message, String editeur) {
        this.titre = titre;
        this.message = message;
        this.editeur = editeur;

    }

    public MessageUniv() {

    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }
}
