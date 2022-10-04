package Server;

public class RegistrierungLehrender extends RegistrierungUser {
    private String Lehrstuhl;
    private String Forschungsgebiet;

    public RegistrierungLehrender(String email, String passwort, String vorname, String nachname, String wohnort, String straße, String profilbild, String lehrstuhl, String forschungsgebiet) {
        super(email, passwort, vorname, nachname, wohnort, straße, profilbild);
        Lehrstuhl = lehrstuhl;
        Forschungsgebiet = forschungsgebiet;
    }

    public String getForschungsgebiet() {
        return Forschungsgebiet;
    }

    public String getLehrstuhl() {
        return Lehrstuhl;
    }

    public void setLehrstuhl(String lehrstuhl) {
        Lehrstuhl = lehrstuhl;
    }

    public void setForschungsgebiet(String forschungsgebiet) {
        Forschungsgebiet = forschungsgebiet;
    }
}

