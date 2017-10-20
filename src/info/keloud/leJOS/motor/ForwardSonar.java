package info.keloud.leJOS.motor;

import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class ForwardSonar extends MotorAdapter {
    public ForwardSonar(RegulatedMotor motorLeft, RegulatedMotor motorRight, UltrasonicSensor ultrasonicSensor) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.ultrasonicSensor = ultrasonicSensor;
        behavior = "Forward Sonar";
    }

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

        // 速度から必要な距離を求める(可変距離)
        double distanceVariable = speed * 0.27F;
        double distanceStop = speed * 0.5F;

        // 設定した超音波センサーの距離を角度累計に変換する
        int distanceUltrasonic = (int) ((distance / diameter / Math.PI) * 360);

        // 減速に使用する角度累計
        int distanceDeceleration = degreeLeft + (int) distanceVariable;

        // 移動開始
        motorLeft.forward();
        motorRight.forward();

        // 移動判定
        try {
            while (true) {
                // 設定した超音波センサーの距離+停止までに必要な距離まで更新し続ける。
                if (distanceUltrasonic + distanceStop < (int) ((ultrasonicSensor.ultrasonicFloat[0] * 100 / diameter / Math.PI) * 360)) {
                    // 減速に必要な角度累計を代入する
                    distanceDeceleration = degreeLeft + (int) distanceStop;
                }
                // 停止する
                if ((int) ((ultrasonicSensor.ultrasonicFloat[0] * 100 / diameter / Math.PI) * 360) < distanceUltrasonic) {
                    break;
                }
                // 減速部
                if (distanceDeceleration - distanceStop < degreeLeft) {
                    speedNow = (int) ((float) (speed - speedMin) * (distanceDeceleration - degreeLeft) / distanceStop + speedMin);
                }
                // 加速部
                else if (degreeLeft < distanceVariable) {
                    speedNow = (int) ((float) ((float) (speed - speedMin) * degreeLeft / distanceVariable) + speedMin);
                }
                // 巡行部
                else {
                    speedNow = speed;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = motorLeft.getTachoCount() - tacho_L;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
