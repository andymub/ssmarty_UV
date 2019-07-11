package ssmarty.univ.database.model;

public class messageUniv {
    String titre,message,editeur;

    public messageUniv(String titre, String message, String editeur) {
        this.titre = titre;
        this.message = message;
        this.editeur = editeur;
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
