package Client.javaFx;

public class Thema {

    private String titel;
    private String beschreibung;
    private int userID;

    public Thema(String titel, String beschreibung, int userID) {

        this.titel = titel;
        this.beschreibung = beschreibung;
        this.userID = userID;

    }

    public String getTitel(){return titel;}
    public String getBeschreibung(){return beschreibung;}
    public int getUserID(){return  userID;}
}
