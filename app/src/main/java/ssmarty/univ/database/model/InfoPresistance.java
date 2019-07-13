package ssmarty.univ.database.model;

public class InfoPresistance {

    public static final String TABLE_NAME_INFO = "Infos";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FAC = "LISTE_FACDEP";
   // public static final List<messageUniv> COLUMN_ALLMESSAGE = null; //todo save all message

    private int id;
    private String facEnum;
    private String ListeFacDep;
   // private List<messageUniv> allMesage ;

    // Create table SQL query
    public static final String CREATE_TABLE_INFO =
            "CREATE TABLE " + TABLE_NAME_INFO + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FAC +  " TEXT)";

    public InfoPresistance(String listeFacDep) {
        ListeFacDep = listeFacDep;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacEnum() {
        return facEnum;
    }

    public void setFacEnum(String facEnum) {
        this.facEnum = facEnum;
    }

    public String getListeFacDep() {
        return ListeFacDep;
    }

    public void setListeFacDep(String listeFacDep) {
        ListeFacDep = listeFacDep;
    }

    public InfoPresistance() {

    }
}
