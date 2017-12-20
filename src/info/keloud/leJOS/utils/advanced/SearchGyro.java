package info.keloud.leJOS.utils.advanced;

import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import info.keloud.leJOS.utils.AbstractMotor;
import info.keloud.leJOS.utils.TurnWithGyro;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

// Process to search by spinning on the spot
public class SearchGyro extends AbstractMotor {
    private TurnWithGyro turnWithGyro;

    public SearchGyro(RegulatedMotor motorLeft, RegulatedMotor motorRight, UltrasonicSensor ultrasonicSensor, GyroSensor gyroSensor, TurnWithGyro turnWithGyro) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.ultrasonicSensor = ultrasonicSensor;
        this.gyroSensor = gyroSensor;
        this.turnWithGyro = turnWithGyro;
    }

    @Override
    public void run() {
        setOperationMode("Search Gyro");
        //サーチ初期位置に動く
        turnWithGyro.setSpeed(100);
        turnWithGyro.setAngle(angle / 2);
        turnWithGyro.run();

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

        turnWithGyro.setAngle(angle * 0.86 + gyroValue);
        turnWithGyro.run();

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
