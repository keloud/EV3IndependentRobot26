package info.keloud.leJOS.informationManager;

import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class Scheduler extends Thread {
    private String operationMode = "non Operation";
    private Monitoring monitoring;
    private DisplayUpdater displayUpdater;
    private boolean mode;
    // the left running motor
    private RegulatedMotor motorLeft;
    // the right running motor
    private RegulatedMotor motorRight;
    // the right running motor
    private RegulatedMotor motorCenter;
    // the color sensor
    private ColorSensor colorSensor;
    // the ultrasonic sensor
    private UltrasonicSensor ultrasonicSensor;
    // the gyro sensor
    private GyroSensor gyroSensor;

    public Scheduler(RegulatedMotor motorLeft, RegulatedMotor motorRight, RegulatedMotor motorCenter, UltrasonicSensor ultrasonicSensor, ColorSensor colorSensor, GyroSensor gyroSensor) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.motorCenter = motorCenter;
        this.ultrasonicSensor = ultrasonicSensor;
        this.colorSensor = colorSensor;
        this.gyroSensor = gyroSensor;
        // monitoring = new Monitoring();
        displayUpdater = new DisplayUpdater();
        mode = true;
    }

    public void run() {
        int timer = 0;
        while (mode) {
            int accumulationMotorLeft = motorLeft.getTachoCount();
            int accumulationMotorRight = motorRight.getTachoCount();
            int accumulationMotorCenter = motorCenter.getTachoCount();
            float colorIdValue = colorSensor.getValue();
            float ultrasonicValue = ultrasonicSensor.getValue();
            float gyroValue = gyroSensor.getValue();
            timer++;
            // サーバーに値を渡す
            // monitoring.updateValue(operationMode, accumulationMotorLeft, accumulationMotorRight, accumulationMotorCenter, colorIdValue, ultrasonicValue, gyroValue, timer);
            // 表示を更新する
            displayUpdater.updateValue(operationMode, accumulationMotorLeft, accumulationMotorRight, accumulationMotorCenter, colorIdValue, ultrasonicValue, gyroValue, timer);
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
        monitoring.monitoringStop();
        mode = false;
    }

    public void setOperationMode(String operatingMode) {
        this.operationMode = operatingMode;
    }
}
