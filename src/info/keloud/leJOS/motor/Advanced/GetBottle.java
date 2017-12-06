package info.keloud.leJOS.motor.Advanced;

import info.keloud.leJOS.motor.Arm;
import info.keloud.leJOS.motor.Forward;
import info.keloud.leJOS.motor.MotorAdapter;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class GetBottle extends MotorAdapter {
    private Arm arm;
    private Forward forward;
    private SearchGyro searchGyro;

    public GetBottle(RegulatedMotor motorLeft, RegulatedMotor motorRight, RegulatedMotor motorCenter, UltrasonicSensor ultrasonicSensor, Arm arm, Forward forward, SearchGyro searchGyro) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.motorCenter = motorCenter;
        this.ultrasonicSensor = ultrasonicSensor;
        this.arm = arm;
        this.forward = forward;
        this.searchGyro = searchGyro;
        behavior = "GetBottle";
    }

    @Override
    public void run() {
        LCD.clear(6);
        LCD.drawString(behavior, 1, 6);
        LCD.refresh();

        //停止状態で探索処理
        searchGyro.setAngle(50);
        searchGyro.run();

        // 初期化
        int initTachoCount = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        //速度(800)で手前距離(7cm)で止まる
        setSpeed(800);
        setDistance(7);
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
                    if (degreeLeft % 250 == 0) {
                        //searchGyro.setAngle(20);
                        //searchGyro.run();

                        // 一時停止
                        motorLeft.stop(true);
                        motorRight.stop(true);

                        //サーチ処理

                        //再始動
                        motorLeft.forward();
                        motorRight.forward();
                    }
                    speedNow = speed;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = motorLeft.getTachoCount() - initTachoCount;
            }
        } catch (InterruptedException ignored) {
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);

        //スピード(100)走行距離(7cm)で前進
        forward.setSpeed(100);
        forward.setDistance(7);
        forward.run();
        //アームを閉じる
        arm.run("Close");

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
