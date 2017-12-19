package info.keloud.leJOS.informationManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Monitoring extends Thread {
    private Socket socket;
    private PrintWriter printWriter;

    Monitoring() {
        try {
            socket = new Socket("192.168.44.52", 50000);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("start");
        } catch (IOException e) {
            monitoringStop();
            e.getStackTrace();
        }
    }

    void monitoringStop() {
        try {
            if (printWriter != null) {
                printWriter.println("close");
                printWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateValue(String operationMode, int accumulationMotorLeft, int accumulationMotorRight, int accumulationMotorCenter, float colorIdValue, float ultrasonicValue, float gyroValue, int timer) {
        printWriter.println("Mode:" + operationMode + ",Left:" + accumulationMotorLeft + ",Right:" + accumulationMotorRight + ",Center:" + accumulationMotorCenter + ",ColorId:" + colorIdValue + ",Ultrasonic:" + ultrasonicValue + ",Gyro:" + gyroValue + ",Timer" + timer);
    }
}
