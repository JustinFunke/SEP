package Client.Lehrveranstaltung;


public class Lehrveranstaltung {
    private String name;
    private String typ;
    private String semester;

    public Lehrveranstaltung(String name, String typ, String semester) {

        this.name = name;
        this.typ = typ;
        this.semester = semester;

    }

    public String getName(){return name;}
    public String getTyp(){return typ;}
    public String getSemester(){return semester;}

}
