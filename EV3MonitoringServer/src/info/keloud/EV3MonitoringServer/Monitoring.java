package info.keloud.EV3MonitoringServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

class Monitoring extends Thread {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private info.keloud.EV3MonitoringServer.MainFrame mainFrame;

    Monitoring() {
        // System.out.println("info.keloud.EV3MonitoringServer.Monitoring.info.keloud.EV3MonitoringServer.Monitoring");
        mainFrame = new info.keloud.EV3MonitoringServer.MainFrame();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(50000);
            socket = serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String bufferedString = bufferedReader.readLine();

                //System.out.println("EV3 : " + bufferedString);

                mainFrame.updatePanel(bufferedString);

                if (Objects.equals(bufferedString, "All Complete")) {
                    mainFrame.updatePanel(bufferedString + ",Operation complete,Operation complete,Operation complete,Operation complete,Operation complete,Operation complete");
                    break;
                }

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e1) {
                    System.out.println("InterruptedException");
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
