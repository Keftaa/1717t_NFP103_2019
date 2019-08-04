package app;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientTCP {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean hasStopped = false;



    private BufferedReader getInput() throws IOException
    {
        return new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
    }

    private PrintWriter getOutput() throws IOException
    {
        return new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()), true);
    }

    public void start(String ip, int port) throws IOException, UnknownHostException {
        clientSocket = new Socket(ip, port);
        out = this.getOutput();
        in = this.getInput();
        System.out.println("Connecté!");
    }

    public void send(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        this.checkResponse(resp);
    }

    public void sendName(String msg) {
        out.println(msg);
    }

    public void initUserInput() throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("TCP client prêt");
        System.out.println("_help pour commencer");
        while(!hasStopped){
            String input = scanner.nextLine();
            if(clientSocket != null && !clientSocket.isClosed()) {
                this.send(input);
            }
            this.processInput(input);
        }
    }

    private void processInput(String input) {
        if(input.startsWith("_fetch")){
            System.out.println("Recherche en cours, patientez...");
            Thread bc = new Thread(new BroadcastClient());
            bc.start();
            System.out.println("Recherche terminée");
        } else if(input.startsWith("_connect")) {
            this.connectToServer(input);
        } else if(input.equals("_help")) {
            this.printHelp();
        }
    }

    private void printHelp(){
        System.out.println("Tapez _fetch pour rechercher des serveurs");
        System.out.println("_connect <ip serveur> <votre pseudonyme>");
        System.out.println("_help pour ce message");
    }

    private void connectToServer(String input) {
        String[] params = input.split(" ");
        System.out.println("Connexion en cours...");
        try {
            this.start(params[1], 2000);
            this.sendName(params[2]);
        } catch (IOException e) {
            System.err.println("Connexion échouée!");
            e.printStackTrace();
        }
    }
    private void checkResponse(String resp){
        System.out.println(resp);
        if("bye".equals(resp)) {
            try {
                this.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        hasStopped = true;
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException
    {
        ClientTCP client = new ClientTCP();
        client.initUserInput();
    }
}
