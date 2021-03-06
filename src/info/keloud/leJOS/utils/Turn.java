package info.keloud.leJOS.utils;

import info.keloud.leJOS.motor.AbstractMotor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class Turn extends AbstractUtil {
    public Turn(AbstractMotor leftMotor, AbstractMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    public void run(float speed, int angle) {
        setSpeed(speed);
        setAngle(angle);
        run();
    }

    @Override
    public void run() {
        // 初期化
        setOperationMode("Turn Initialize");
        int initTachoCount = rightMotor.getTachoCount();
        float speedMin = 100;
        int degreeTachoCount = 0;
        boolean turnBoolean = false;
        leftMotor.setSpeed(speedMin);
        rightMotor.setSpeed(speedMin);

        // 角度累計計算
        if (angle < 0) {
            setAngle(-angle);
            turnBoolean = true;
            initTachoCount = leftMotor.getTachoCount();
        }

        float cum = ((((angle * width * (float) Math.PI) / 360) / diameter / (float) Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        float distanceVariable = speed * 0.28F;
        if (cum - distanceVariable <= 0) {
            distanceVariable = 0;
            setSpeed(100);
        }

        /*
        Angle
        Left turn is +.
        Right turn is -.
        */
        if (!turnBoolean) {
            setOperationMode("Turn Left");
            // 移動開始
            leftMotor.backward();
            rightMotor.forward();
            // 移動判定
            try {
                while (degreeTachoCount < cum) {
                    if (degreeTachoCount > cum - distanceVariable) {
                        //減速部
                        speedNow = ((speed - speedMin) * (cum - degreeTachoCount) / distanceVariable + speedMin);
                    } else if (degreeTachoCount < distanceVariable) {
                        //加速部
                        speedNow = (((speed - speedMin) * degreeTachoCount / distanceVariable) + speedMin);
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
        } else {
            setOperationMode("Turn Right");
            // 移動開始
            leftMotor.forward();
            rightMotor.backward();
            // 移動判定
            try {
                while (degreeTachoCount < cum) {
                    if (degreeTachoCount > cum - distanceVariable) {
                        //減速部
                        speedNow = ((speed - speedMin) * (cum - degreeTachoCount) / distanceVariable + speedMin);
                    } else if (degreeTachoCount < distanceVariable) {
                        //加速部
                        speedNow = ((speed - speedMin) * degreeTachoCount / distanceVariable + speedMin);
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
        }

        // 停止
        leftMotor.stop(true);
        rightMotor.stop(true);
    }
}
