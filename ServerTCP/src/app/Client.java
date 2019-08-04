package app;

import java.net.Socket;

public class Client {
    Socket socket;
    String name;

    public Client(String name, Socket socket){
        this.socket = socket;
        this.name = name;
    }
    public Socket getSocket(){
        return this.socket;
    }
    public String getName(){
        return this.name;

    }
}
