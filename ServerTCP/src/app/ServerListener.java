package app;

import java.io.*;
import java.net.Socket;

public class ServerListener {
    Client client;
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
    public ServerListener(Socket cs) {
        try {
            this.in = this.getInput(cs);
            this.out = this.getOutput(cs);
            this.client = new Client(in.readLine(), cs);
            System.out.println(this.client.getName() + " vient de se connecter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        String line;
        while(true) {
            try {
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
        if("bonjour serveur".equals(message)) {
            this.out.println("bonjour client");
        } else if ("_quit".equals(message)) {
            this.quitClient();
            return false;
        } else {
            this.out.printf("Message reçu: %s", message);
            this.out.println();
        }
        return true;
    }
    public void quitClient() {
        this.out.println("salut!");
        this.out.println(this.client.getName() + " déconnecté");
    }
    public void stopConnection() throws IOException {
        this.in.close();
        this.out.close();
        this.client.getSocket().close();
    }
}
