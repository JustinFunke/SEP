package Server;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Registrierung {
    public static void registrierungStudent(String email, String nachname, String passwort,String stadt,String strasse,String vorname, File bild, String studienfach) throws IOException {

            RegistrierungStudent student = new RegistrierungStudent(email.toLowerCase().replace(" ",""), passwort.replace(" ",""), vorname.replace(" ",""), nachname.replace(" ",""), stadt.replace(" ",""), strasse.replace(" ",""), studienfach.replace(" ",""), setImage(bild));
            Datenbank.DbStudentHinzufuegen(student);


    }
    public static void registrierungLehrender(String email, String nachname, String passwort,String stadt,String strasse,String vorname, File bild, String lehrstuhl, String forschungsgebiet) throws IOException {

        RegistrierungLehrender lehrender = new RegistrierungLehrender(email.toLowerCase().replace(" ",""), passwort.replace(" ",""), vorname.replace(" ",""), nachname.replace(" ",""), stadt.replace(" ",""), strasse.replace(" ",""),setImage(bild),lehrstuhl,forschungsgebiet);
        Datenbank.DbLehrenderHinzufuegen(lehrender);
    }


    private static String setImage(File selectedFile) throws IOException {
        if(selectedFile != null) {
            byte[] fileContent = FileUtils.readFileToByteArray(selectedFile);
            return Base64.getEncoder().encodeToString(fileContent);
        }else{
            File defaultFile = new File("MavenTest/src/main/resources/DefaultProfilbild.png");
            byte[] fileContent = FileUtils.readFileToByteArray(defaultFile);
            return Base64.getEncoder().encodeToString(fileContent);
        }

    }
}
