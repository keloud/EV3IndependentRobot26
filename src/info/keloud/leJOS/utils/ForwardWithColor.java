package info.keloud.leJOS.utils;

import info.keloud.leJOS.motor.AbstractMotor;
import info.keloud.leJOS.sensor.ColorSensor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ForwardWithColor extends AbstractUtil {
    public ForwardWithColor(AbstractMotor leftMotor, AbstractMotor rightMotor, ColorSensor colorSensor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.colorSensor = colorSensor;
    }

    public void run(int speed, int colorId) {
        setSpeed(speed);
        setColorId(colorId);
        run();
    }

    public void run(int speed, String colorId) {
        setSpeed(speed);
        setColorId(colorId);
        run();
    }

    @Override
    public void run() {
        // 初期化
        setOperationMode("Forward Color");
        int initTachoCount = leftMotor.getTachoCount();
        int speedMin = 100;
        int degreeTachoCount = 0;
        leftMotor.setSpeed(speedMin);
        rightMotor.setSpeed(speedMin);

        //速度から必要な距離を求める(可変距離)
        float distanceVariable = speed * 0.24F;
        float distanceStop = 50;

        // 減速に使用する角度累計
        float distanceDeceleration = degreeTachoCount + distanceVariable;

        // 移動開始
        leftMotor.forward();
        rightMotor.forward();

        // 移動判定
        try {
            while (true) {
                //ColorIdまで必要な減速距離を更新し続ける
                if (colorSensor.getValue() != colorId) {
                    distanceDeceleration = degreeTachoCount + distanceStop;
                }
                //後退して停止する
                if (distanceDeceleration < degreeTachoCount) {
                    break;
                }
                if (distanceDeceleration - distanceStop < degreeTachoCount) {
                    //減速部
                    speedNow = ((speed - speedMin) * (distanceDeceleration - degreeTachoCount) / distanceStop + speedMin);
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
