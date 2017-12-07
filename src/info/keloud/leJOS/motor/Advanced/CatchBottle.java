package info.keloud.leJOS.motor.Advanced;

import info.keloud.leJOS.motor.Arm;
import info.keloud.leJOS.motor.Forward;
import info.keloud.leJOS.motor.MotorAdapter;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class CatchBottle extends MotorAdapter {
    private Arm arm;
    private Forward forward;
    private float ultrasonicValue = 0;

    public CatchBottle(RegulatedMotor motorLeft, RegulatedMotor motorRight, RegulatedMotor motorCenter, UltrasonicSensor ultrasonicSensor, Arm arm, Forward forward) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.motorCenter = motorCenter;
        this.ultrasonicSensor = ultrasonicSensor;
        this.arm = arm;
        this.forward = forward;
        behavior = "CatchBottle";
    }

    @Override
    public void run() {
        LCD.clear(6);
        LCD.drawString(behavior, 1, 6);
        LCD.refresh();
        if (angle < 0) {
            angle = -angle;
        }
        setSpeed(300);

        //初期探索
        search();

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
                    if (degreeLeft % 20 == 0) {
                        //searchGyro.setAngle(20);
                        //searchGyro.run();

                        //定期的な探索処理
                        search();
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

    private void search() {
        //対象の距離が短くなっているか判定する
        if (ultrasonicSensor.ultrasonicFloat[0] < ultrasonicValue + 0.02 || ultrasonicValue == 0) {
            //もし、遠くなっていたら以下の処理を行う
            // 一時停止
            motorLeft.stop(true);
            motorRight.stop(true);

            //サーチ処理(初期位置移動)
            // 初期化
            int initTachoCount = motorLeft.getTachoCount();
            int speedNow;
            int speedMin = 100;
            int degreeCount = 0;
            motorLeft.setSpeed(speedMin);
            motorRight.setSpeed(speedMin);

            // 角度累計計算
            int cum = (int) ((((angle / 2 * width * Math.PI) / 360) / diameter / Math.PI) * 360);

            //速度から必要な距離を求める(可変距離)
            double distanceVariable = speed * 0.28F;

            // 移動開始
            motorLeft.forward();
            motorRight.backward();

            // 移動判定
            try {
                while (degreeCount <= cum) {
                    if (degreeCount > cum - distanceVariable) {
                        //減速部
                        speedNow = (int) ((float) (speed - speedMin) * (cum - degreeCount) / distanceVariable + speedMin);
                    } else if (degreeCount < distanceVariable) {
                        //加速部
                        speedNow = (int) ((float) ((float) (speed - speedMin) * degreeCount / distanceVariable) + speedMin);
                    } else {
                        //巡航部
                        speedNow = speed;
                    }
                    motorLeft.setSpeed(speedNow);
                    motorRight.setSpeed(speedNow);
                    Thread.sleep(wait);
                    degreeCount = motorLeft.getTachoCount() - initTachoCount;
                }
            } catch (InterruptedException ignored) {
                LCD.clear(6);
                LCD.drawString("Error", 1, 6);
                LCD.refresh();
            }

            // 停止
            motorLeft.stop(true);
            motorRight.stop(true);

            //サーチ処理(探索)
            // 初期化
            initTachoCount = motorRight.getTachoCount();
            degreeCount = 0;
            float exploreUltrasonicValue = 0;
            float nowUltrasonicValue;
            int exploreTachoCount = 0;
            motorLeft.setSpeed(40);
            motorRight.setSpeed(40);

            // 角度累計計算
            cum = (int) ((((angle * width * Math.PI) / 360) / diameter / Math.PI) * 360);

            // 移動開始
            motorLeft.backward();
            motorRight.forward();

            // 移動判定
            try {
                while (degreeCount <= cum) {
                    //探索部
                    nowUltrasonicValue = ultrasonicSensor.ultrasonicFloat[0];
                    if (nowUltrasonicValue < exploreUltrasonicValue) {
                        exploreUltrasonicValue = nowUltrasonicValue;
                        exploreTachoCount = degreeCount;
                    }
                    Thread.sleep(wait);
                    degreeCount = motorRight.getTachoCount() - initTachoCount;
                }
            } catch (InterruptedException ignored) {
                LCD.clear(6);
                LCD.drawString("Error", 1, 6);
                LCD.refresh();
            }

            // 停止
            motorLeft.stop(true);
            motorRight.stop(true);

            //探索処理(探索した位置に戻る)
            // 初期化
            initTachoCount = motorLeft.getTachoCount();
            degreeCount = 0;
            motorLeft.setSpeed(speedMin);
            motorRight.setSpeed(speedMin);

            // 角度累計計算
            cum = (int) ((((angle / 2 * width * Math.PI) / 360) / diameter / Math.PI) * 360) - exploreTachoCount;

            // 移動開始
            motorLeft.forward();
            motorRight.backward();

            // 移動判定
            try {
                while (degreeCount <= cum) {
                    if (degreeCount > cum - distanceVariable) {
                        //減速部
                        speedNow = (int) ((float) (speed - speedMin) * (cum - degreeCount) / distanceVariable + speedMin);
                    } else if (degreeCount < distanceVariable) {
                        //加速部
                        speedNow = (int) ((float) ((float) (speed - speedMin) * degreeCount / distanceVariable) + speedMin);
                    } else {
                        //巡航部
                        speedNow = speed;
                    }
                    motorLeft.setSpeed(speedNow);
                    motorRight.setSpeed(speedNow);
                    Thread.sleep(wait);
                    degreeCount = motorLeft.getTachoCount() - initTachoCount;
                }
            } catch (InterruptedException ignored) {
                LCD.clear(6);
                LCD.drawString("Error", 1, 6);
                LCD.refresh();
            }

            // 停止
            motorLeft.stop(true);
            motorRight.stop(true);

            //再始動
            motorLeft.forward();
            motorRight.forward();
        }
        ultrasonicValue = ultrasonicSensor.ultrasonicFloat[0];
    }
}
