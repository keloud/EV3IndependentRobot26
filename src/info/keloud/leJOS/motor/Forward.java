package info.keloud.leJOS.motor;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class Forward extends Motor {
    public Forward(RegulatedMotor motorLeft, RegulatedMotor motorRight) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
    }

    public void run(int maximumSpeed, double distance) {
        LCD.clear(6);
        LCD.drawString("Forward", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = maximumSpeed * 0.24F;

        // 移動開始
        motorLeft.forward();
        motorRight.forward();

        // 移動判定
        try {
            while (degreeLeft < cum) {
                if (degreeLeft > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (maximumSpeed - speedMin) * (cum - degreeLeft) / distanceVariable + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (maximumSpeed - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = maximumSpeed;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = motorLeft.getTachoCount() - tacho_L;
            }
        } catch (InterruptedException ignored) {
        }

        // 停止 flt()はフロート状態になる
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
