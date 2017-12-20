package info.keloud.leJOS.utils;

import info.keloud.leJOS.motor.AbstractMotor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class Forward extends AbstractUtil {
    public Forward(AbstractMotor leftMotor, AbstractMotor rightMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    public void run(int speed, int distance) {
        setSpeed(speed);
        setDistance(distance);
        run();
    }

    @Override
    public void run() {
        // 初期化
        setOperationMode("Forward");
        int initTachoCount = leftMotor.getTachoCount();
        int speedMin = 100;
        int degreeTachoCount = 0;
        leftMotor.setSpeed(speedMin);
        rightMotor.setSpeed(speedMin);

        // 角度累計計算
        float cum = ((distance / diameter / (float) Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        float distanceVariable = speed * 0.24F;

        // 移動開始
        leftMotor.forward();
        rightMotor.forward();

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

        // 停止
        leftMotor.stop(true);
        rightMotor.stop(true);
    }

    @Override
    public String getOperationMode() {
        return operationMode;
    }
}
