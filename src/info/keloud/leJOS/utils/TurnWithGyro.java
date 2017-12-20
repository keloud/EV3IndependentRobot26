package info.keloud.leJOS.utils;

import info.keloud.leJOS.motor.AbstractMotor;
import info.keloud.leJOS.sensor.GyroSensor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class TurnWithGyro extends AbstractUtil {
    public TurnWithGyro(AbstractMotor leftMotor, AbstractMotor rightMotor, GyroSensor gyroSensor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.gyroSensor = gyroSensor;
    }

    public void run(float speed, int angle) {
        setSpeed(speed);
        setAngle(angle);
        run();
    }

    @Override
    public void run() {
        /*
        Angle
        Left turn is +.
        Right turn is -.
        */
        if (angle < 0) {
            rightTurn();
        } else if (0 < angle) {
            leftTurn();
        }
    }


    private void leftTurn() {
        // 初期化
        setOperationMode("Turn Left Gyro");
        float gyroInit = gyroSensor.getValue();
        float degreeGyro = 0;
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);

        // 移動開始
        leftMotor.backward();
        rightMotor.forward();

        // 移動判定
        try {
            while (degreeGyro < angle) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.getValue() - gyroInit;
            }
        } catch (InterruptedException ignored) {
            Sound.beep();
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        leftMotor.stop(true);
        rightMotor.stop(true);
    }

    private void rightTurn() {
        // 初期化
        setOperationMode("Turn Right Gyro");
        float gyroInit = gyroSensor.getValue();
        float degreeGyro = 0;
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);

        // 移動開始
        leftMotor.forward();
        rightMotor.backward();

        // 移動判定
        try {
            while (angle < degreeGyro) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.getValue() - gyroInit;
            }
        } catch (InterruptedException ignored) {
            Sound.beep();
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        leftMotor.stop(true);
        rightMotor.stop(true);
    }

}
