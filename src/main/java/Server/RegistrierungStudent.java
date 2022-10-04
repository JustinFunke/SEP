package Server;

public class RegistrierungStudent extends RegistrierungUser {
    private String Studienfach;

    public RegistrierungStudent(String email, String passwort, String vorname, String nachname, String wohnort, String straße, String studienfach, String profilbild) {
        super(email, passwort, vorname, nachname, wohnort, straße, profilbild);
        Studienfach = studienfach;
    }

    public String getStudienfach() {
        return Studienfach;
    }

    public void setStudienfach(String studienfach) {
        Studienfach = studienfach;
    }



}

