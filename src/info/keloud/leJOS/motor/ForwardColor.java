package info.keloud.leJOS.motor;

import info.keloud.leJOS.sensor.ColorSensor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class ForwardColor extends AbstractMotor {
    public ForwardColor(RegulatedMotor motorLeft, RegulatedMotor motorRight, ColorSensor colorSensor) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.colorSensor = colorSensor;
    }

    public void run(int speed, int colorId) {
        setSpeed(speed);
        setColorId(colorId);
        run();
    }

    @Override
    public void run() {
        // 初期化
        setOperationMode("Forward Color");
        int initTachoCount = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeTachoCount = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speed * 0.24F;
        double distanceStop = 50;

        // 減速に使用する角度累計
        int distanceDeceleration = degreeTachoCount + (int) distanceVariable;

        // 移動開始
        motorLeft.forward();
        motorRight.forward();

        // 移動判定
        try {
            while (true) {
                //ColorIdまで必要な減速距離を更新し続ける
                if (colorSensor.getValue() != colorId) {
                    distanceDeceleration = degreeTachoCount + (int) distanceStop;
                }
                //後退して停止する
                if (distanceDeceleration < degreeTachoCount) {
                    break;
                }
                if (distanceDeceleration - distanceStop < degreeTachoCount) {
                    //減速部
                    speedNow = (int) ((float) (speed - speedMin) * (distanceDeceleration - degreeTachoCount) / distanceStop + speedMin);
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
            Sound.beep();
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);
    }
}
