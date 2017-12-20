package info.keloud.leJOS.utils;

import info.keloud.leJOS.motor.AbstractMotor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class ForwardWithSonar extends AbstractUtil {
    public ForwardWithSonar(AbstractMotor leftMotor, AbstractMotor rightMotor, UltrasonicSensor ultrasonicSensor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.ultrasonicSensor = ultrasonicSensor;
    }

    public void run(int speed, int distance) {
        setSpeed(speed);
        setDistance(distance);
        run();
    }

    @Override
    public void run() {
        // 初期化
        setOperationMode("Forward Sonar");
        int initTachoCount = leftMotor.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeTachoCount = 0;
        leftMotor.setSpeed(speedMin);
        rightMotor.setSpeed(speedMin);

        // 速度から必要な距離を求める(可変距離)
        double distanceVariable = speed * 0.27F;
        double distanceStop = speed * 0.5F;

        // 設定した超音波センサーの距離を角度累計に変換する
        int distanceUltrasonic = (int) ((distance / diameter / Math.PI) * 360);

        // 減速に使用する角度累計
        int distanceDeceleration = degreeTachoCount + (int) distanceVariable;

        // 移動開始
        leftMotor.forward();
        rightMotor.forward();

        // 移動判定
        try {
            while (true) {
                // 設定した超音波センサーの距離+停止までに必要な距離まで更新し続ける。
                if (distanceUltrasonic + distanceStop < (int) ((ultrasonicSensor.getValue() * 100 / diameter / Math.PI) * 360)) {
                    // 減速に必要な角度累計を代入する
                    distanceDeceleration = degreeTachoCount + (int) distanceStop;
                }
                // 停止する
                if ((int) ((ultrasonicSensor.getValue() * 100 / diameter / Math.PI) * 360) < distanceUltrasonic) {
                    break;
                }
                // 減速部
                if (distanceDeceleration - distanceStop < degreeTachoCount) {
                    speedNow = (int) ((float) (speed - speedMin) * (distanceDeceleration - degreeTachoCount) / distanceStop + speedMin);
                }
                // 加速部
                else if (degreeTachoCount < distanceVariable) {
                    speedNow = (int) ((float) ((float) (speed - speedMin) * degreeTachoCount / distanceVariable) + speedMin);
                }
                // 巡行部
                else {
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