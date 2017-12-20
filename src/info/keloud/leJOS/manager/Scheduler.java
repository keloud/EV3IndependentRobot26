package info.keloud.leJOS.manager;

import info.keloud.leJOS.motor.AbstractMotor;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;

public class Scheduler extends Thread {
    private String operationMode = "non Operation";
    // private Monitoring monitoring;
    private DisplayUpdater displayUpdater;
    private boolean mode;
    // the right running motor
    private AbstractMotor centerMotor;
    // the left running motor
    private AbstractMotor leftMotor;
    // the right running motor
    private AbstractMotor rightMotor;
    // the color sensor
    private ColorSensor colorSensor;
    // the ultrasonic sensor
    private UltrasonicSensor ultrasonicSensor;
    // the gyro sensor
    private GyroSensor gyroSensor;

    public Scheduler(AbstractMotor centerMotor, AbstractMotor leftMotor, AbstractMotor rightMotor, UltrasonicSensor ultrasonicSensor, ColorSensor colorSensor, GyroSensor gyroSensor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.centerMotor = centerMotor;
        this.ultrasonicSensor = ultrasonicSensor;
        this.colorSensor = colorSensor;
        this.gyroSensor = gyroSensor;
        //monitoring = new Monitoring();
        displayUpdater = new DisplayUpdater();
        mode = true;
    }

    public void run() {
        int timer = 0;
        while (mode) {
            int accumulationleftMotor = leftMotor.getTachoCount();
            int accumulationrightMotor = rightMotor.getTachoCount();
            int accumulationcenterMotor = centerMotor.getTachoCount();
            float colorIdValue = colorSensor.getValue();
            float ultrasonicValue = ultrasonicSensor.getValue();
            float gyroValue = gyroSensor.getValue();
            timer++;
            // サーバーに値を渡す
            //monitoring.updateValue(operationMode, accumulationleftMotor, accumulationrightMotor, accumulationcenterMotor, colorIdValue, ultrasonicValue, gyroValue, timer);
            // 表示を更新する
            displayUpdater.updateValue(operationMode, accumulationleftMotor, accumulationrightMotor, accumulationcenterMotor, colorIdValue, ultrasonicValue, gyroValue, timer);
            // 例外処理
            try {
                Thread.sleep(20);
            } catch (Exception ie) {
                LCD.clear(6);
                LCD.drawString("Error", 1, 6);
                LCD.refresh();
            }
        }
    }

    public void countStop() {
        //monitoring.monitoringStop();
        mode = false;
    }

    public void setOperationMode(String operatingMode) {
        this.operationMode = operatingMode;
    }
}
