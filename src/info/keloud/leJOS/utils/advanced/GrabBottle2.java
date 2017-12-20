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

        //速度(800)で手前距離(7cm)で止まる
        setSpeed(800);
        setDistance(7);
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
        int cum = (int) ((distance / diameter / Math.PI) * 360);

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
                    speedNow = (int) ((float) (speed - speedMin) * (cum - degreeTachoCount) / distanceVariable + speedMin);
                } else if (degreeTachoCount < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speed - speedMin) * degreeTachoCount / distanceVariable) + speedMin);
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
