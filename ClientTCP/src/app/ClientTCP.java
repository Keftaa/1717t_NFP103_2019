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
    Thread clientListener;



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
        this.startClientListener();
        System.out.println("Connecté!");
    }

    private void startClientListener() {
        clientListener = new Thread(new ClientListener(this));
        clientListener.start();
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

    private boolean processInput(String input) throws IOException {
        if(input.startsWith("_fetch")){
            System.out.println("Recherche en cours, patientez...");
            Thread bc = new Thread(new BroadcastClient());
            bc.start();
            System.out.println("Recherche terminée");
            return true;
        } else if(input.startsWith("_connect")) {
            this.connectToServer(input);
            return true;
        } else if(input.equals("_help")) {
            this.printHelp();
            return true;
        } else if(input.equals("_quit")) {
            this.stop();
            return true;
        }
        return false;
    }

    private void printHelp(){
        System.out.println("Tapez _fetch pour rechercher des serveurs");
        System.out.println("_connect <ip serveur> <votre pseudonyme>");
        System.out.println("_help pour ce message");
        System.out.println("_who pour une liste des utilisateurs actifs.");
        System.out.println("_quit pour quitter");

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
    boolean checkResponse(String resp) {
        if ("_quit".equals(resp)) {
            try {
                this.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else if ("_kill".equals(resp)) {
            this.killConnection();
            return false;
        }
        return true;
    }

    private void killConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientListener.interrupt();
        clientListener = null;
    }


    public void stop() throws IOException {
        in.close();
        out.close();
        hasStopped = true;
        clientSocket.close();
        System.exit(1);
    }

    Socket getClientSocket() {
        return clientSocket;
    }

    BufferedReader getSocketInput() {
        return in;
    }

    public static void main(String[] args) throws IOException
    {
        ClientTCP client = new ClientTCP();
        if (args.length > 2) {
            if (args[0].equals("_connect")) {
                client.connectToServer(String.join(" ", args));
            }
        }
        client.initUserInput();
    }
}
