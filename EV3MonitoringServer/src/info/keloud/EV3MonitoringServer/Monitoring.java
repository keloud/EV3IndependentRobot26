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
        // System.out.println("info.keloud.EV3MonitoringServer.Monitoring.run");
        try {
            serverSocket = new ServerSocket(255);
            socket = serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String bufferedString = bufferedReader.readLine();

                //System.out.println("EV3 : " + bufferedString);

                mainFrame.updatePanel(bufferedString);

                if (Objects.equals(bufferedString, "close")) {
                    mainFrame.updatePanel("All Complete");
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
            System.out.println("IOException");
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (socket != null) socket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                System.out.println("final.IOException");
                e.printStackTrace();
            }
        }
    }
}
