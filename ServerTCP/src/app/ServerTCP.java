package app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

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
        ServerListener sl = new ServerListener(cs, this);
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

        Thread inputHandler = new Thread(new InputHandler(server));
        inputHandler.start();

        server.start(2000);
    }

    public void broadcastMessage(String message, Client client) {
        serverListeners.forEach(serverListener -> serverListener.sendMessage(message, client));
    }

    public void notifyAllUsers (String message) {
        serverListeners.forEach(serverListener -> serverListener.notifyUser(message));
    }

    public void removeClient(ServerListener sl) {
        this.serverListeners.remove(sl);
    }

    public void shutdown() {
        this.serverListeners.forEach(ServerListener::kill);
        this.serverListeners.clear();
        System.exit(1);
    }


    ArrayList<ServerListener> getServerListeners() {
        return this.serverListeners;
    }

    public void killUser(String username) {
        ServerListener sl = null;
        for (Iterator<ServerListener> iterator = this.serverListeners.iterator(); iterator.hasNext();) {
            sl = iterator.next();
            if(sl.client.getName().equals(username)) {
                break;
            }
        }
        sl.kickUser();
    }

}
