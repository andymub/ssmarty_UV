package ssmarty.univ.database.model;

public class listViewModel {
    public static final String TABLE_NAME = "Listes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_N0M_DATE = "Nom_Date";
    public static final String COLUMN_TYPE_INTITULE = "Type";
    public static final String COLUMN_LISTE = "Liste";
    public static final String COLUMN_ETAT = "Etat";

    private int id;
    private String Nom_Date;
    private String Type;
    private String Liste;
    private String Etat;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_N0M_DATE + " TEXT,"
                    + COLUMN_TYPE_INTITULE + " TEXT,"
                    + COLUMN_LISTE + " TEXT,"
                    + COLUMN_ETAT + " TEXT,"
                    + ")";

    public listViewModel() {
    }

    public listViewModel(int id, String nom_Date, String type, String liste, String etat) {
        this.id = id;
        Nom_Date = nom_Date;
        Type = type;
        Liste = liste;
        Etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_Date() {
        return Nom_Date;
    }

    public void setNom_Date(String nom_Date) {
        Nom_Date = nom_Date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getListe() {
        return Liste;
    }

    public void setListe(String liste) {
        Liste = liste;
    }

    public String getEtat() {
        return Etat;
    }

    public void setEtat(String etat) {
        Etat = etat;
    }
}