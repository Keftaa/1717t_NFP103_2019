package app;

import java.io.IOException;

public class ClientListener implements Runnable {
    private ClientTCP clientTCP;

    public ClientListener(ClientTCP clientTCP) {
        this.clientTCP = clientTCP;
    }

    @Override
    public void run() {
        while (!clientTCP.getClientSocket().isClosed() && clientTCP.getClientSocket().isConnected()) {
            try {
                System.out.println("Waiting response");
                String resp = clientTCP.getSocketInput().readLine();
                System.out.println("Response received");
                if (clientTCP.checkResponse(resp)) {
                    System.out.println(resp);
                } else {
                    break;
                }
            } catch (IOException e) {
                try {
                    this.clientTCP.getClientSocket().close();
                } catch (IOException ignore) {

                }
            }
        }
    }
}
