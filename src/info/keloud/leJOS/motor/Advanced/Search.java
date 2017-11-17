package info.keloud.leJOS.motor.Advanced;

import info.keloud.leJOS.motor.MotorAdapter;
import info.keloud.leJOS.motor.TurnGyro;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

// Process to search by spinning on the spot
public class Search extends MotorAdapter {
    private TurnGyro turnGyro;

    public Search(RegulatedMotor motorLeft, RegulatedMotor motorRight, UltrasonicSensor ultrasonicSensor, TurnGyro turnGyro) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.ultrasonicSensor = ultrasonicSensor;
        this.turnGyro = turnGyro;
        behavior = "StopSearching";
    }

    @Override
    public void run() {
        LCD.clear(6);
        LCD.drawString(behavior, 1, 6);
        LCD.refresh();

        turnGyro.setSpeed(100);
        turnGyro.setAngle(angle / 2);

        // 初期化
        float gyroInit = gyroSensor.gyroFloat[0];
        float degreeGyro = 0;
        float gyroValue = degreeGyro;
        float degreeUltrasonic = ultrasonicSensor.ultrasonicFloat[0];
        float ultrasonicValue = degreeUltrasonic;
        motorLeft.setSpeed(speed);
        motorRight.setSpeed(speed);

        // 移動開始
        motorLeft.forward();
        motorRight.backward();

        // 移動判定
        try {
            while (angle < degreeGyro) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.gyroFloat[0];
                degreeUltrasonic = ultrasonicSensor.ultrasonicFloat[0];
                if (degreeUltrasonic < ultrasonicValue) {
                    ultrasonicValue = degreeUltrasonic;
                    gyroValue = degreeGyro;
                }
                degreeGyro = gyroSensor.gyroFloat[0] - gyroInit;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);

        turnGyro.setAngle(angle * 0.86 + gyroValue);
    }
}
