package Client.Lehrveranstaltung;

public class Projektgruppe {

    private String name;
    private  int lvID;

    public Projektgruppe(String name, int lvID) {

        this.name = name;
        this.lvID = lvID;

    }

    public String getName(){return name;}
    public int getLVID(){return lvID;}


}
