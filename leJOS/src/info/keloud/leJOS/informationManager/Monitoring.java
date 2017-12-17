package info.keloud.leJOS.informationManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Monitoring extends Thread {
    private Socket socket;
    private PrintWriter printWriter;

    Monitoring() {
    }

    public void run() {
        try {
            socket = new Socket("192.168.44.52", 255);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void monitoringStop() {
        printWriter.println("close");
        try {
            if (printWriter != null) {
                printWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateValue(String operationMode, int accumulationMotorLeft, int accumulationMotorRight, int accumulationMotorCenter, float colorIdValue, float ultrasonicValue, float gyroValue) {
        printWriter.println("Mode:" + operationMode + ",Left:" + accumulationMotorLeft + ",Right:" + accumulationMotorRight + ",Center:" + accumulationMotorCenter + ",ColorId:" + colorIdValue + ",Ultrasonic:" + ultrasonicValue + ",Gyro:" + gyroValue);
    }
}
