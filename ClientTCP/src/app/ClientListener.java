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
                String resp = clientTCP.getSocketInput().readLine();
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
