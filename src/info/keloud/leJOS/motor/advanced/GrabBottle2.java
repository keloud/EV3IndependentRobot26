package info.keloud.leJOS.motor.advanced;

import info.keloud.leJOS.motor.AbstractMotor;
import info.keloud.leJOS.motor.Arm;
import info.keloud.leJOS.motor.Forward;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class GrabBottle2 extends AbstractMotor {
    float ultrasonicValue;
    private Arm arm;
    private Forward forward;
    private boolean usePutBottle;

    public GrabBottle2(RegulatedMotor motorLeft, RegulatedMotor motorRight, RegulatedMotor motorCenter, UltrasonicSensor ultrasonicSensor, ColorSensor colorSensor, Arm arm, Forward forward) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.motorCenter = motorCenter;
        this.ultrasonicSensor = ultrasonicSensor;
        this.colorSensor = colorSensor;
        this.arm = arm;
        this.forward = forward;
    }

    @Override
    public void run() {
        setOperationMode("Grab Bottle");
        setSpeed(300);
        ultrasonicValue = ultrasonicSensor.getValue();
        // search();
        // 初期化
        int initTachoCount = motorLeft.getTachoCount();
        int speedNow;
        int minimumSpeed = 100;
        int degreeTachoCount = 0;
        int outOfMapInt = 0;
        //速度(800)で手前距離(7cm)で止まる
        setSpeed(800);
        setDistance(7);
        motorLeft.setSpeed(minimumSpeed);
        motorRight.setSpeed(minimumSpeed);

        // 速度から必要な距離を求める(可変距離)
        double distanceVariable = speed * 0.27F;
        double distanceStop = speed * 0.5F;

        // 設定した超音波センサーの距離を角度累計に変換する
        setDistance(6);
        int distanceUltrasonic = (int) ((distance / diameter / Math.PI) * 360);

        // 減速に使用する角度累計
        int distanceDeceleration = degreeTachoCount + (int) distanceVariable;

        // 移動開始
        motorLeft.forward();
        motorRight.forward();

        // 移動判定
        try {
            while (true) {
                // 停止する
                if ((int) ((ultrasonicSensor.getValue() * 100 / diameter / Math.PI) * 360) < distanceUltrasonic) {
                    break;
                }

                //コース外へ行くのを防ぐ(白と黄と赤以外の色を検知したらペットボトルを取りに行くのをやめる)
                if (colorSensor.getValue() != 6 && colorSensor.getValue() != 3 && colorSensor.getValue() != 0) {
                    if (outOfMapInt == 3) {
                        outOfMap();
                        break;
                    } else {
                        outOfMapInt++;
                    }
                } else {
                    outOfMapInt = 0;
                }

                // 設定した超音波センサーの距離+停止までに必要な距離まで更新し続ける。
                if (distanceUltrasonic + distanceStop < (int) ((ultrasonicSensor.getValue() * 100 / diameter / Math.PI) * 360)) {
                    // 減速に必要な角度累計を代入する
                    distanceDeceleration = degreeTachoCount + (int) distanceStop;

                    //定期的な探査する
                    float actualUltrasonicValue = ultrasonicSensor.getValue();
                    if (ultrasonicValue < actualUltrasonicValue && degreeTachoCount % 20 == 0) {
                        int tmpValue = degreeTachoCount;
                        // search();
                        degreeTachoCount += motorLeft.getTachoCount() - tmpValue;
                        ultrasonicValue = actualUltrasonicValue;
                    }
                }

                // 減速部
                if (distanceDeceleration - distanceStop < degreeTachoCount) {
                    speedNow = (int) ((float) (speed - minimumSpeed) * (distanceDeceleration - degreeTachoCount) / distanceStop + minimumSpeed);
                }

                // 加速部
                else if (degreeTachoCount < distanceVariable) {
                    speedNow = (int) ((float) ((float) (speed - minimumSpeed) * degreeTachoCount / distanceVariable) + minimumSpeed);
                }

                // 巡行部
                else {
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

        //スピード(100)走行距離(7cm)で前進
        forward.setSpeed(100);
        forward.setDistance(7);
        forward.run();
        //アームを閉じる
        arm.run("Close");

        // usePutBottle がオンの時
        if (usePutBottle) {
            /*
        //速度(600)カラー(赤)で後進
        backwardColor.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        //アームを開ける
        arm.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
             */
        }
    }

    private void search() {
        setOperationMode("Grab Bottle Search");
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int tempSpeed = speed;
        setSpeed(300);

        //サーチ処理(初期位置移動)(右旋回)
        // 初期化
        int initTachoCount = motorLeft.getTachoCount();
        int speedNow;
        int minimumSpeed = 300;
        int degreeCount = 0;
        motorLeft.setSpeed(minimumSpeed);
        motorRight.setSpeed(minimumSpeed);

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
                    speedNow = (int) ((float) (speed - minimumSpeed) * (cum - degreeCount) / distanceVariable + minimumSpeed);
                } else if (degreeCount < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speed - minimumSpeed) * degreeCount / distanceVariable) + minimumSpeed);
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
            Sound.beep();
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
        float exploreUltrasonicValue = ultrasonicSensor.getValue();
        float nowUltrasonicValue;
        int exploreTachoCount = 0;
        motorLeft.setSpeed(100);
        motorRight.setSpeed(100);

        // 角度累計計算
        cum = (int) ((((angle * width * Math.PI) / 360) / diameter / Math.PI) * 360);

        // 移動開始
        motorLeft.backward();
        motorRight.forward();

        // 移動判定
        try {
            while (degreeCount <= cum) {
                //探索部
                nowUltrasonicValue = ultrasonicSensor.getValue();
                if (nowUltrasonicValue < exploreUltrasonicValue) {
                    exploreUltrasonicValue = nowUltrasonicValue;
                    ultrasonicValue = nowUltrasonicValue;
                    exploreTachoCount = degreeCount;
                    Sound.beep();
                }
                Thread.sleep(wait);
                degreeCount = motorRight.getTachoCount() - initTachoCount;
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

        //探索処理(探索した位置に戻る)
        // 初期化
        initTachoCount = motorLeft.getTachoCount();
        degreeCount = 0;
        int maximumSpeed = 300;
        motorLeft.setSpeed(minimumSpeed);
        motorRight.setSpeed(minimumSpeed);

        // 角度累計計算
        cum = (int) ((((angle * width * Math.PI) / 360) / diameter / Math.PI) * 360) - exploreTachoCount;

        // 移動開始
        motorLeft.forward();
        motorRight.backward();

        // 移動判定
        try {
            while (degreeCount <= cum) {
                if (degreeCount > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (maximumSpeed - minimumSpeed) * (cum - degreeCount) / distanceVariable + minimumSpeed);
                } else if (degreeCount < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (maximumSpeed - minimumSpeed) * degreeCount / distanceVariable) + minimumSpeed);
                } else {
                    //巡航部
                    speedNow = maximumSpeed;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeCount = motorLeft.getTachoCount() - initTachoCount;
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

        // 再始動
        setSpeed(tempSpeed);
        motorLeft.setSpeed(tempSpeed);
        motorRight.setSpeed(tempSpeed);
        motorLeft.forward();
        motorRight.forward();
    }

    private void outOfMap() {
        setOperationMode("Grab Bottle Out of Map");
        // 一時停止
        motorLeft.stop(true);
        motorRight.stop(true);

        //アームを閉じる
        arm.run("Close");
    }

    public void setUsePutBottle(boolean usePutBottle) {
        this.usePutBottle = usePutBottle;
    }
}
