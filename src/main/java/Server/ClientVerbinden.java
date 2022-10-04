package Server;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;

///////////////////Senden und Empfangen

public class ClientVerbinden {
    private int port;
    private String serverIP;
    public Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    public ClientVerbinden(String serverIP){
        this.port = 9090;
        this.serverIP = serverIP;
    }

    public void connectToServer() throws IOException {
        socket = new Socket(serverIP,port);

        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    public void send(JSONObject j){
        System.out.println("Senden");
        String jsonString = j.toJSONString();
        PrintWriter pw = new PrintWriter(dos);
        pw.println(jsonString);
        pw.flush();
    }

    public JSONObject receiveobj() throws IOException {
        JSONObject json = new JSONObject();
        try{
            JSONParser parser = new JSONParser();
            BufferedReader in = new BufferedReader(new InputStreamReader(dis,"UTF-8"));
            Object obj;
            String s = null;
            while (s==null){
                s= in.readLine();
            }
            //Gelesene in ein Objekt speichern
            obj = parser.parse(s);
            //Object in json umwandeln
            json = (JSONObject) obj;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  json;
    }
    public JSONArray receiveary() throws IOException {
        JSONArray json = new JSONArray();
        try{

            JSONParser parser = new JSONParser();
            BufferedReader in = new BufferedReader(new InputStreamReader(dis,"UTF-8"));
            Object obj;
            String s = null;
            while (s==null){
                s= in.readLine();
            }
            json = new JSONArray(s);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }



}
