package Client.Kalender;

public class Termin {
    String Datum;
    String anfang;
    String dauer;
    String desc;
    boolean RVorhanden;
    String rErinnerung;
    String erinnerungsart;

    public Termin(String datum, String anfang, String dauer, String desc, boolean RVorhanden, String rErinnerung, String erinnerungsart) {
        Datum = datum;
        this.anfang = anfang;
        this.dauer = dauer;
        this.desc = desc;
        this.RVorhanden = RVorhanden;
        this.rErinnerung = rErinnerung;
        this.erinnerungsart = erinnerungsart;
    }

    public Termin(String datum, String anfang, String dauer, String description) {
        this.Datum=datum;
        this.anfang=anfang;
        this.dauer=dauer;
        this.desc=description;
    }

    public void setDatum(String datum) {
        Datum = datum;
    }

    public void setAnfang(String anfang) {
        this.anfang = anfang;
    }

    public void setDauer(String dauer) {
        this.dauer = dauer;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setRVorhanden(boolean RVorhanden) {
        this.RVorhanden = RVorhanden;
    }

    public void setrErinnerung(String rErinnerung) {
        this.rErinnerung = rErinnerung;
    }

    public void setErinnerungsart(String erinnerungsart) {
        this.erinnerungsart = erinnerungsart;
    }

    public String getDatum() {
        return Datum;
    }

    public String getAnfang() {
        return anfang;
    }

    public String getDauer() {
        return dauer;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isRVorhanden() {
        return RVorhanden;
    }

    public String getrErinnerung() {
        return rErinnerung;
    }

    public String getErinnerungsart() {
        return erinnerungsart;
    }
}

