package info.keloud.leJOS.motor;

import info.keloud.leJOS.sensor.GyroSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class TurnGyro extends MotorAdapter {
    public TurnGyro(RegulatedMotor motorLeft, RegulatedMotor motorRight, GyroSensor gyroSensor) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.gyroSensor = gyroSensor;
        behavior = "TurnGyro";
    }

    @Override
    public void run() {
        LCD.clear(6);
        LCD.drawString(behavior, 1, 6);
        LCD.refresh();
        /*
        Angle
        Left turn is +.
        Right turn is -.
        */
        if (angle < 0) {
            rightGyro(speed, angle);
        } else if (0 < angle) {
            leftGyro(speed, angle);
        }
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void rightGyro(int spped, double angle) {
        // 初期化
        float gyroInit = gyroSensor.gyroFloat[0];
        float degreeGyro = 0;
        motorLeft.setSpeed(speed);
        motorRight.setSpeed(speed);

        // 移動開始
        motorLeft.forward();
        motorRight.backward();

        // 移動判定
        try {
            while (angle < degreeGyro) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.gyroFloat[0] - gyroInit;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);
    }

    private void leftGyro(int spped, double angle) {
        // 初期化
        float gyroInit = gyroSensor.gyroFloat[0];
        float degreeGyro = 0;
        motorLeft.setSpeed(speed);
        motorRight.setSpeed(speed);

        // 移動開始
        motorLeft.backward();
        motorRight.forward();

        // 移動判定
        try {
            while (degreeGyro < angle) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.gyroFloat[0] - gyroInit;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);
    }
}
