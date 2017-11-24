package info.keloud.leJOS.motor;

import info.keloud.leJOS.sensor.ColorSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class BackwardColor extends MotorAdapter {
    public BackwardColor(RegulatedMotor motorLeft, RegulatedMotor motorRight, ColorSensor colorSensor) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.colorSensor = colorSensor;
        behavior = "Backward Color";
    }

    @Override
    public void run() {
        LCD.clear(6);
        LCD.drawString(behavior, 1, 6);
        LCD.refresh();

        // 初期化
        int tacho_L = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speed * 0.24F;
        double distanceStop = 50;

        // 減速に使用する角度累計
        int distanceDeceleration = degreeLeft + (int) distanceVariable;

        // 移動開始
        motorLeft.backward();
        motorRight.backward();

        // 移動判定
        try {
            while (true) {
                //ColorIdまで必要な減速距離を更新し続ける
                if (colorSensor.colorFloat[0] != colorId) {
                    distanceDeceleration = degreeLeft + (int) distanceStop;
                }
                //後退して停止する
                if (distanceDeceleration < degreeLeft) {
                    break;
                }
                if (distanceDeceleration - distanceStop < degreeLeft) {
                    //減速部
                    speedNow = (int) ((float) (speed - speedMin) * (distanceDeceleration - degreeLeft) / distanceStop + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speed - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speed;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = -(motorLeft.getTachoCount() - tacho_L);
            }
        } catch (InterruptedException ignored) {
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止 flt()はフロート状態になる
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
