package info.keloud.leJOS.motor.Advanced;

import info.keloud.leJOS.Monitoring;
import info.keloud.leJOS.motor.MotorAdapter;
import info.keloud.leJOS.motor.TurnGyro;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

// Process to search by spinning on the spot
public class SearchGyro extends MotorAdapter {
    private TurnGyro turnGyro;

    public SearchGyro(Monitoring monitoring, RegulatedMotor motorLeft, RegulatedMotor motorRight, UltrasonicSensor ultrasonicSensor, GyroSensor gyroSensor, TurnGyro turnGyro) {
        this.monitoring = monitoring;
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.ultrasonicSensor = ultrasonicSensor;
        this.gyroSensor = gyroSensor;
        this.turnGyro = turnGyro;
    }

    @Override
    public void run() {
        monitoring.setBehavior(setBehavior("Searching"));

        //サーチ初期位置に動く
        turnGyro.setSpeed(100);
        turnGyro.setAngle(angle / 2);
        turnGyro.run();

        // 初期化
        float gyroInit = gyroSensor.getValue();
        float degreeGyro = 0;
        float gyroValue = degreeGyro;
        float degreeUltrasonic = ultrasonicSensor.getValue();
        float ultrasonicValue = degreeUltrasonic;
        setSpeed(80);
        motorLeft.setSpeed(speed);
        motorRight.setSpeed(speed);

        // 移動開始
        motorLeft.forward();
        motorRight.backward();

        // 移動判定
        try {
            while (-angle < degreeGyro) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.getValue() - gyroInit;
                degreeUltrasonic = ultrasonicSensor.getValue();
                if (degreeUltrasonic < ultrasonicValue) {
                    ultrasonicValue = degreeUltrasonic;
                    gyroValue = degreeGyro;
                }
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);

        turnGyro.setAngle(angle * 0.86 + gyroValue);
        turnGyro.run();

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
