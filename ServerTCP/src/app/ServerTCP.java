package app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {

    ServerSocket serverSocket;
    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;

    private static BufferedReader getInput(Socket p) throws IOException
    {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static PrintWriter getOutput(Socket p) throws IOException
    {
        return new PrintWriter(new OutputStreamWriter(p.getOutputStream()), true);
    }

    private void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.printServerAddress();

        clientSocket = serverSocket.accept();
        this.printClientAddress();

        out = this.getOutput(clientSocket);
        in = this.getInput(clientSocket);
        String message;
        while((message = in.readLine()) != null) {
            if(!this.checkMessage(message)) {
                this.stop();
                break;
            }
        }
    }

    private void printServerAddress(){
        System.out.printf("L'addresse de la socket du serveur est %s\n", serverSocket.getLocalSocketAddress());
    }

    private void printClientAddress(){
        System.out.printf("L'addresse de la socket du client est %s\n", clientSocket.getRemoteSocketAddress());
    }

    private void quitClient(){
        out.println("bye");
        System.out.printf("Client %s déconnecté!", clientSocket.getRemoteSocketAddress());
        try {
            this.stop();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    private boolean checkMessage(String message) {
        if("bonjour serveur".equals(message)) {
            out.println("bonjour client!");
        } else if("_quit".equals(message)) {
            this.quitClient();
            return false;
        } else {
            out.println("unrecognized message");
        }
        return true;
    }

    public static void main(String[] args) throws IOException
    {
        ServerTCP server = new ServerTCP();
        server.start(2000);
    }
}
