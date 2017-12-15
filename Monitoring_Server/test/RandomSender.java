import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class RandomSender {
    private Socket socket;
    private PrintWriter printWriter;

    RandomSender() {
        run();
    }

    void run() {
        String informationSystem = "Forward";
        int accumulationMotorCenter = 0;
        int accumulationMotorLeft = 0;
        int accumulationMotorRight = 0;
        int colorSensor = 0;
        int ultrasonicSensor = 0;
        int gyroSensor = 0;
        try {
            socket = new Socket("localhost", 255);
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            for (int i = 0; i < 500; i++) {
                printWriter.println("Info:" + informationSystem + ", Center:" + accumulationMotorCenter + ", Left:" + accumulationMotorLeft + ", Right:" + accumulationMotorRight + ", ColorId:" + colorSensor + ", Ultrasonic:" + ultrasonicSensor + ", Gyro:" + gyroSensor);
                accumulationMotorCenter++;
                accumulationMotorLeft++;
                accumulationMotorRight++;
                if (ultrasonicSensor < 255) {
                    ultrasonicSensor++;
                } else {
                    ultrasonicSensor = 0;
                }
                if (gyroSensor < 360) {
                    gyroSensor++;
                } else {
                    gyroSensor = -360;
                }
            }
            printWriter.println("close");
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
}