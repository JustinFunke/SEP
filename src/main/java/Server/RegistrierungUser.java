package Server;

public class RegistrierungUser {
    private String email;
    private String passwort;
    private String vorname;
    private String nachname;
    private String wohnort;
    private String strasse;
    private String profilbild;

    public RegistrierungUser(String email, String passwort, String vorname, String nachname, String wohnort, String strasse, String profilbild) {
        this.email = email;
        this.passwort = passwort;
        this.vorname = vorname;
        this.nachname = nachname;
        this.wohnort = wohnort;
        this.strasse = strasse;
        this.profilbild = profilbild;
    }
    public String getProfilbild() {
        return profilbild;
    }

    public String getStrasse() {
        return strasse;
    }

    public String getWohnort() {
        return wohnort;
    }

    public String getNachname() {
        return nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public String getPasswort() {
        return passwort;
    }

    public String getEmail() {
        return email;
    }



}
