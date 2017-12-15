package info.keloud.leJOS.InformationHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Monitoring extends Thread {
    private Socket socket;
    private PrintWriter printWriter;
    private int accumulationMotorCenter, accumulationMotorLeft, accumulationMotorRight;
    private float colorIdValue, ultrasonicValue, gyroValue;
    private String behavior = "Initial State";
    private boolean mode;

    Monitoring() {
        mode = true;
    }

    public void run() {
        try {
            socket = new Socket("192.168.44.52", 255);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            while (mode) {
                printWriter.println(" Info:" + behavior + ", Center:" + accumulationMotorCenter + ", Left:" + accumulationMotorLeft + ", Right:" + accumulationMotorRight + ", ColorId:" + colorIdValue + ", Ultrasonic:" + ultrasonicValue + ", Gyro:" + gyroValue);
            }
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
        mode = false;
        printWriter.println("close");
    }

    public void getBehavior(String behavior) {
        this.behavior = behavior;
    }

    public void setValue(String behavior, int accumulationMotorCenter, int accumulationMotorLeft, int accumulationMotorRight, float colorIdValue, float ultrasonicValue, float gyroValue) {
        this.behavior = behavior;
        this.accumulationMotorCenter = accumulationMotorCenter;
        this.accumulationMotorLeft = accumulationMotorLeft;
        this.accumulationMotorRight = accumulationMotorRight;
        this.colorIdValue = colorIdValue;
        this.ultrasonicValue = ultrasonicValue;
        this.gyroValue = gyroValue;
    }
}
