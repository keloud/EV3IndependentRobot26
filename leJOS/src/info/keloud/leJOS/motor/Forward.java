package info.keloud.leJOS.motor;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class Forward extends Motor {
    public Forward(RegulatedMotor motorLeft, RegulatedMotor motorRight) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
    }

    @Override
    public void run() {
        // 初期化
        int initTachoCount = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeTachoCount = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speed * 0.24F;

        // 移動開始
        motorLeft.forward();
        motorRight.forward();

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
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeTachoCount = motorLeft.getTachoCount() - initTachoCount;
            }
        } catch (InterruptedException ignored) {
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
