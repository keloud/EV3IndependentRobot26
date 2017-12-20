package info.keloud.leJOS.utils;

import info.keloud.leJOS.motor.AbstractMotor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class Turn extends AbstractUtil {
    public Turn(AbstractMotor leftMotor, AbstractMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    public void run(int speed, int angle) {
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
        if (0 < angle) {
            leftTurn();
        } else if (angle < 0) {
            angle = -angle;
            rightTurn();
        }
    }


    private void leftTurn() {
        // 初期化
        setOperationMode("Turn Left");
        int initTachoCount = rightMotor.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeTachoCount = 0;
        leftMotor.setSpeed(speedMin);
        rightMotor.setSpeed(speedMin);

        // 角度累計計算
        int cum = (int) ((((angle * width * Math.PI) / 360) / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speed * 0.28F;
        if (cum - distanceVariable <= 0) {
            distanceVariable = 0;
            setSpeed(100);
        }

        // 移動開始
        leftMotor.backward();
        rightMotor.forward();

        // 移動判定
        try {
            while (degreeTachoCount < cum) {
                if (degreeTachoCount > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (speed - speedMin) * (cum - degreeTachoCount) / distanceVariable + speedMin);
                } else if (degreeTachoCount < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speed - speedMin) * degreeTachoCount / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speed;
                }
                leftMotor.setSpeed(speedNow);
                rightMotor.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeTachoCount = rightMotor.getTachoCount() - initTachoCount;
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
        setOperationMode("Turn Right");
        int initTachoCount = leftMotor.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeTachoCount = 0;
        leftMotor.setSpeed(speedMin);
        rightMotor.setSpeed(speedMin);

        // 角度累計計算
        int cum = (int) ((((angle * width * Math.PI) / 360) / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speed * 0.28F;
        if (cum - distanceVariable <= 0) {
            distanceVariable = 0;
            setSpeed(100);
        }

        // 移動開始
        leftMotor.forward();
        rightMotor.backward();

        // 移動判定
        try {
            while (degreeTachoCount < cum) {
                if (degreeTachoCount > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (speed - speedMin) * (cum - degreeTachoCount) / distanceVariable + speedMin);
                } else if (degreeTachoCount < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speed - speedMin) * degreeTachoCount / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speed;
                }
                leftMotor.setSpeed(speedNow);
                rightMotor.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeTachoCount = leftMotor.getTachoCount() - initTachoCount;
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
