package app;

import java.io.IOException;
import java.net.*;

public class BroadcastClient implements Runnable {
    @Override
    public void run() {
        try {
            DatagramSocket c = new DatagramSocket();
            c.setBroadcast(true);
            byte[] sendData = "SERVER_SCAN".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 2001);
            c.send(sendPacket);

            byte[] recBuf = new byte[15000];
            DatagramPacket receivePacket = new DatagramPacket(recBuf, recBuf.length);
            c.receive(receivePacket);

            System.out.println("Serveur disponible: " + receivePacket.getAddress().getHostAddress());
            c.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
