package Server;

import Server.Mail.EmailDistributor;

import javax.mail.MessagingException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNeu {
    private int serverPort;
    private Socket clientSocket;

    public ServerNeu(){this.serverPort =9090;}

    public static void main(String[] args) throws MessagingException {
        ServerNeu server = new ServerNeu();
        server.serverStart();
        //Datenbank.dropTableErgebnis();
        if(EmailDistributor.SemesterEndCheck())
        {
            String currentSemester = EmailDistributor.getCurrentSemester();
            System.out.println("Current Semester: " + currentSemester);
            EmailDistributor.sendResultsToStudents(currentSemester);
        }
    }

    public void serverStart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ServerSocket serverSocket = new ServerSocket(serverPort);
                    System.out.println("Server gestartet!");

                    while (true){

                        clientSocket = serverSocket.accept();
                        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                        DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                        Thread neuerClient = new ClientThread(clientSocket, dis, dos);
                        neuerClient.start();
                    }

                }catch (Exception ex){
                    try{
                        clientSocket.close();
                    }catch (IOException ex2){
                        System.out.println(ex.getMessage());
                    }
                    System.out.println(ex.getMessage());
                }
            }
        }).start();
    }
}
