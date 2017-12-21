package info.keloud.leJOS.utils.advanced;

import info.keloud.leJOS.motor.AbstractMotor;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import info.keloud.leJOS.utils.AbstractUtil;
import info.keloud.leJOS.utils.Arm;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class GrabBottle2 extends AbstractUtil {
    private Arm arm;
    private boolean usePutBottle;

    public GrabBottle2(AbstractMotor centerMotor, AbstractMotor leftMotor, AbstractMotor rightMotor, UltrasonicSensor ultrasonicSensor, ColorSensor colorSensor, Arm arm) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.centerMotor = centerMotor;
        this.ultrasonicSensor = ultrasonicSensor;
        this.colorSensor = colorSensor;
        this.arm = arm;
    }

    public void run(int angle, boolean usePutBottle) {
        setAngle(angle);
        setUsePutBottle(usePutBottle);
        run();
    }

    @Override
    public void run() {
        setOperationMode("Grab Bottle");

        //最初に探索を行う
        search();

        //速度(800)で手前距離(7cm)で止まる
        setSpeed(800);
        setDistance(7);
        int initTachoCount = leftMotor.getTachoCount();
        float speedMin = 100;
        int degreeTachoCount = 0;
        int outOfMapInt = 0;
        leftMotor.setSpeed(speedMin);
        rightMotor.setSpeed(speedMin);

        // 速度から必要な距離を求める(可変距離)
        float distanceVariable = speed * 0.27F;
        float distanceStop = speed * 0.5F;

        // 設定した超音波センサーの距離を角度累計に変換する
        float distanceUltrasonic = ((distance / diameter / (float) Math.PI) * 360);

        // 減速に使用する角度累計
        float distanceDeceleration = degreeTachoCount + distanceVariable;

        // 移動開始
        leftMotor.forward();
        rightMotor.forward();

        // 移動判定
        try {
            while (true) {
                // 設定した超音波センサーの距離+停止までに必要な距離まで更新し続ける。
                if (distanceUltrasonic + distanceStop < ((ultrasonicSensor.getValue() * 100 / diameter / (float) Math.PI) * 360)) {
                    // 減速に必要な角度累計を代入する
                    distanceDeceleration = degreeTachoCount + distanceStop;
                } else {
                    // 一時停止
                    leftMotor.stop(true);
                    rightMotor.stop(true);
                    float tempTachoCount = leftMotor.getTachoCount();
                    search();
                    //再始動
                    initTachoCount += leftMotor.getTachoCount() - tempTachoCount;
                    leftMotor.forward();
                    rightMotor.forward();
                }
                // 停止する
                if (((ultrasonicSensor.getValue() * 100 / diameter / (float) Math.PI) * 360) < distanceUltrasonic) {
                    break;
                }

                //コース外へ行くのを防ぐ(白と黄と赤以外の色を検知したらペットボトルを取りに行くのをやめる)
                if (colorSensor.getValue() != 6 && colorSensor.getValue() != 3 && colorSensor.getValue() != 0) {
                    if (outOfMapInt == 3) {
                        outOfMap();
                        break;
                    } else {
                        //outOfMapInt++;
                    }
                } else {
                    outOfMapInt = 0;
                }

                // 減速部
                if (distanceDeceleration - distanceStop < degreeTachoCount) {
                    speedNow = ((speed - speedMin) * (distanceDeceleration - degreeTachoCount) / distanceStop + speedMin);
                }
                // 加速部
                else if (degreeTachoCount < distanceVariable) {
                    speedNow = ((speed - speedMin) * degreeTachoCount / distanceVariable + speedMin);
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
        //アームを開ける
        arm.run("OPEN");

        //速度(100)で走行距離(10cm)で前進
        setSpeed(100);
        setDistance(10);
        // 初期化
        setOperationMode("Forward");
        initTachoCount = leftMotor.getTachoCount();
        speedMin = 60;
        degreeTachoCount = 0;
        leftMotor.setSpeed(speedMin);
        rightMotor.setSpeed(speedMin);

        // 角度累計計算
        float cum = ((distance / diameter / (float) Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        distanceVariable = speed * 0.34F;

        // 移動開始
        leftMotor.forward();
        rightMotor.forward();

        // 移動判定
        try {
            while (degreeTachoCount < cum) {
                if (degreeTachoCount > cum - distanceVariable) {
                    //減速部
                    speedNow = ((speed - speedMin) * (cum - degreeTachoCount) / distanceVariable + speedMin);
                } else if (degreeTachoCount < distanceVariable) {
                    //加速部
                    speedNow = ((speed - speedMin) * degreeTachoCount / distanceVariable + speedMin);
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

        //アームを閉じる
        arm.run("CLOSE");
    }

    private void search() {
        setOperationMode("Grab Bottle Search");
        Sound.beep();

        float tempAngle = angle;
        float tempSpeed = speed;

        // 速度(500)角度(angle/2)で回転
        setSpeed(500);
        setAngle(tempAngle / 2);
        // 初期化
        int initTachoCount = leftMotor.getTachoCount();
        int minimumSpeed = 300;
        int degreeCount = 0;
        leftMotor.setSpeed(minimumSpeed);
        rightMotor.setSpeed(minimumSpeed);

        // 角度累計計算
        float cum = ((((angle / 2 * width * (float) Math.PI) / 360) / diameter / (float) Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        float distanceVariable = speed * 0.28F;

        // 移動開始
        leftMotor.forward();
        rightMotor.backward();

        // 移動判定
        try {
            while (degreeCount <= cum) {
                if (degreeCount > cum - distanceVariable) {
                    //減速部
                    speedNow = ((speed - minimumSpeed) * (cum - degreeCount) / distanceVariable + minimumSpeed);
                } else if (degreeCount < distanceVariable) {
                    //加速部
                    speedNow = ((speed - minimumSpeed) * degreeCount / distanceVariable + minimumSpeed);
                } else {
                    //巡航部
                    speedNow = speed;
                }
                leftMotor.setSpeed(speedNow);
                rightMotor.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeCount = leftMotor.getTachoCount() - initTachoCount;
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

        //速度(60)角度(angle)で探索しつつ回転
        setSpeed(100);
        setAngle(tempAngle);
        // 初期化
        initTachoCount = rightMotor.getTachoCount();
        degreeCount = 0;
        float exploreUltrasonicValue = ultrasonicSensor.getValue();
        float nowUltrasonicValue;
        int exploreTachoCount = 0;
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);

        // 角度累計計算
        cum = ((((angle * width * (float) Math.PI) / 360) / diameter / (float) Math.PI) * 360);

        // 移動開始
        leftMotor.backward();
        rightMotor.forward();

        // 移動判定
        try {
            while (degreeCount <= cum) {
                //探索部
                nowUltrasonicValue = ultrasonicSensor.getValue();
                Thread.sleep(wait);
                if (nowUltrasonicValue < exploreUltrasonicValue) {
                    exploreUltrasonicValue = nowUltrasonicValue;
                    exploreTachoCount = degreeCount;
                    Sound.beep();
                }
                degreeCount = rightMotor.getTachoCount() - initTachoCount;
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

        // 速度(500)角度(angle)減算値(exploreTachoCount)で回転
        setSpeed(500);
        setAngle(tempAngle);
        // 初期化
        initTachoCount = leftMotor.getTachoCount();
        degreeCount = 0;
        int maximumSpeed = 100;
        leftMotor.setSpeed(minimumSpeed);
        rightMotor.setSpeed(minimumSpeed);

        // 角度累計計算
        cum = ((((angle * width * (float) Math.PI) / 360) / diameter / (float) Math.PI) * 360) - exploreTachoCount;

        // 移動開始
        leftMotor.forward();
        rightMotor.backward();

        // 移動判定
        try {
            while (degreeCount <= cum) {
                if (degreeCount > cum - distanceVariable) {
                    //減速部
                    speedNow = ((float) (maximumSpeed - minimumSpeed) * (cum - degreeCount) / distanceVariable + minimumSpeed);
                } else if (degreeCount < distanceVariable) {
                    //加速部
                    speedNow = ((float) (maximumSpeed - minimumSpeed) * degreeCount / distanceVariable + minimumSpeed);
                } else {
                    //巡航部
                    speedNow = maximumSpeed;
                }
                leftMotor.setSpeed(speedNow);
                rightMotor.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeCount = leftMotor.getTachoCount() - initTachoCount;
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

        setSpeed(tempSpeed);
        setAngle(tempAngle);
    }

    private void outOfMap() {
        setOperationMode("Grab Bottle Out of Map");
        // 一時停止
        leftMotor.stop(true);
        rightMotor.stop(true);

        //アームを閉じる
        arm.run("Close");
    }

    private void setUsePutBottle(boolean usePutBottle) {
        this.usePutBottle = usePutBottle;
    }
}
