package info.keloud.leJOS;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Monitoring extends Thread {
    private leJOS parent = null;
    private Socket socket;
    private PrintWriter printWriter;
    private boolean mode = false;

    Monitoring(leJOS parent) {
        this.parent = parent;
        mode = true;
    }

    public void run() {
        try {
            socket = new Socket("192.168.44.52", 255);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            while (mode) {
                printWriter.println("C:" + parent.accumulationMotorCenter + " L:" + parent.accumulationMotorLeft + " R:" + parent.accumulationMotorRight + " ColorId:" + parent.colorSensor.getValue() + " USonic:" + parent.ultrasonicSensor.getValue() + " Gyro:" + parent.gyroSensor.getValue());
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
}
