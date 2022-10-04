package Client.Registrierung;

import Server.ClientStart;
import Server.ClientVerbinden;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class CheckEingabeRegistrierung {
    private static List<String> emailList = new ArrayList<>();

    //Prüfen ob Email vergeben ist
    public static boolean EmailVergeben(String email) throws IOException {

        //Senden Emailvergeben ausführen
        JSONObject json = new JSONObject();
        json.put("Methode","Emailvergeben");
        json.put("Parameter1",email);
        System.out.println("verschicken");
        ClientStart.verbinden.send(json);
        System.out.println("verschickt");



        //Empfangen
        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        ///immer für String Arrays
        if(jsonArray != null){
            for(int i =0;i<jsonArray.length();i++){
                emailList.add((String) jsonArray.get(i) +"");
            }
        }

            for(int i =0; i<emailList.size();i++){
                if(email.equalsIgnoreCase((String) emailList.get(i))){
                    return true;
                }
            }

        return false;
    }
    public static int Matrikel() throws IOException {
        JSONObject j = ClientStart.verbinden.receiveobj();
        int ausgabe=  Integer.parseInt(j.get("Parameter1")+"");
            return ausgabe;

    }

    //Prüfen ob ein String keine Zahl hat
    public static boolean KeineZahl(String a){
        for(int i =0; i<a.length();i++){
            if(Character.isDigit(a.charAt(i)))
                return false;
        }
        return true;
    }

    //Prüfen ob Email Gültig ist
    public static boolean GueltigeEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }


}
