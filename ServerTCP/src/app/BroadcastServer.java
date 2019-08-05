package app;

import java.io.IOException;
import java.net.*;

public class BroadcastServer implements Runnable {

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2001, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            byte[] recBuf = new byte[15000];
            DatagramPacket packet = new DatagramPacket(recBuf, recBuf.length);
            socket.receive(packet);

            byte[] sendData = "SERVER_SCAN".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
            socket.send(sendPacket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
