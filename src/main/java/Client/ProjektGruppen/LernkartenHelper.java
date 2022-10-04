package Client.ProjektGruppen;

import Server.ClientStart;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class LernkartenHelper {
    public static boolean nameVergeben(int pid,String name) throws IOException {
        // Arrayliste String
        ArrayList<String> namen = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put("Methode","LernkarteNamenVergeben");
        json.put("P1", pid);
        ClientStart.verbinden.send(json);

        JSONArray jsonArray = ClientStart.verbinden.receiveary();
        System.out.println("Array angekommen");
        ///immer für String Arrays
        if(jsonArray != null){
            for(int i =0;i<jsonArray.length();i++){
                namen.add((String) jsonArray.get(i) +"");
            }
        }
        System.out.println(namen.size());
        for (int i =0;i<namen.size();i++){
            if(namen.get(i).equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
}
