package app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTCP {

    ServerSocket serverSocket;
    ArrayList<ServerListener> serverListeners;
    PrintWriter out;
    BufferedReader in;

    public ServerTCP(){
        this.serverListeners = new ArrayList<ServerListener>();
    }



    private void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.printServerAddress();
        Socket clientSocket = null;
        while(true) {
            clientSocket = serverSocket.accept();
            this.listenToClient(clientSocket);
        }
    }

    private void listenToClient(Socket cs) {
        ServerListener sl = new ServerListener(cs);
        sl.start();
        this.serverListeners.add(sl);
    }
    private void printServerAddress(){
        System.out.printf("L'addresse de la socket du serveur est %s\n", serverSocket.getLocalSocketAddress());
    }

    public static void main(String[] args) throws IOException
    {
        ServerTCP server = new ServerTCP();
        Thread serverThread = new Thread(new BroadcastServer());
        serverThread.start();
        server.start(2000);
    }
}
