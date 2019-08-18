package app;

import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerListener extends Thread {
    Client client;
    PrintWriter out;
    BufferedReader in;
    ServerTCP serverTCP;

    private static BufferedReader getInput(Socket p) throws IOException
    {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private static PrintWriter getOutput(Socket p) throws IOException
    {
        return new PrintWriter(new OutputStreamWriter(p.getOutputStream()), true);
    }
    public ServerListener(Socket cs, ServerTCP serverTCP) {
        try {
            this.in = this.getInput(cs);
            this.out = this.getOutput(cs);
            this.client = new Client(in.readLine(), cs);
            this.serverTCP = serverTCP;
            this.serverTCP.notifyAllUsers(this.client.getName() + " vient de se connecter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        String line;
        while(true) {
            try {
                if (!this.client.getSocket().isConnected() || this.client.getSocket().isClosed()) {
                    break;
                }
                line = in.readLine();
                if(!this.checkMessage(line)) {
                    this.stopConnection();
                    break;
                }
            } catch (IOException e) {
                try {
                    this.stopConnection();
                } catch(IOException e2) {
                    e2.printStackTrace();
                }
                break;
            }
        }
    }
    private boolean checkMessage(String message) {
        if(message == null) {
            return false;
        }
        if("bonjour serveur".equals(message)) {
            this.out.println("bonjour client");
        } else if ("_quit".equals(message)) {
            this.quitClient();
            return false;
        } else if("_who".equals(message)) {
            this.sendConnectedUsers();
        } else {
            this.serverTCP.broadcastMessage(message, this.client);
        }
        return true;
    }
    private void sendConnectedUsers() {
        final String[] response = {"Utilisateurs actifs: \n"};
        ServerListener _this = this;
        this.serverTCP.getServerListeners().forEach(ServerListener -> {
            response[0] += "\t- ";
            if (ServerListener == _this) {
                response[0] +=  "Vous: " + ServerListener.client.getName();
            } else {
                response[0] +=  ServerListener.client.getName();
            }
            response[0] += " ( Adresse: " + ServerListener.client.getSocket().getInetAddress() + ":" + ServerListener.client.getSocket().getPort() + " )\n";
        });
        this.out.println(response[0]);
    }



    public void quitClient() {
        this.out.println("salut!");
        try {
            this.stopConnection();
            this.serverTCP.removeClient(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.serverTCP.notifyAllUsers(this.client.getName() + " s'est déconnecté!");

    }
    public void stopConnection() throws IOException {
        this.in.close();
        this.out.close();
        this.client.getSocket().close();
        this.interrupt();
    }

    void sendMessage(String message, Client client) {
        this.out.printf("%s : %s > %s", this.getDate(), client.getName(), message);
        this.out.println();
    }

    void notifyUser(String message) {
        this.out.println(message);
    }

    void kill() {
        try {
            this.out.println("Le serveur s'est déconnecté.");
            this.out.println("_kill");
            this.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kickUser() {
        try {
            this.out.println("Vous avez été exclu.");
            this.out.println("_kill");
            this.stopConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }



}
