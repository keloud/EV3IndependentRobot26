package info.keloud.leJOS;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Monitoring extends Thread {
    private leJOS parent;
    private Socket socket;
    private PrintWriter printWriter;
    private String behavior;
    private boolean mode;

    Monitoring(leJOS parent) {
        this.parent = parent;
        mode = true;
    }

    public void run() {
        try {
            socket = new Socket("192.168.44.52", 255);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            while (mode) {
                printWriter.println("C:" + parent.accumulationMotorCenter + " L:" + parent.accumulationMotorLeft + " R:" + parent.accumulationMotorRight + " C:" + parent.colorSensor.getValue() + " U:" + parent.ultrasonicSensor.getValue() + " G:" + parent.gyroSensor.getValue() + " B:" + behavior);
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

    void close() {
        mode = false;
        printWriter.println("close");
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
}
