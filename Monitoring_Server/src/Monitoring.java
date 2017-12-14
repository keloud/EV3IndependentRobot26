import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

class Monitoring extends Thread {
    private String bufferedString;
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;

    Monitoring() {
        System.out.println("Monitoring.Monitoring");
    }

    @Override
    public void run() {
        System.out.println("Monitoring.run");
        try {
            serverSocket = new ServerSocket(255);
            socket = serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                bufferedString = bufferedReader.readLine();

                System.out.println("EV3 : " + bufferedString);

                if (Objects.equals(bufferedString, "close")) {
                    break;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (socket != null) socket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
