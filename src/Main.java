import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

public class Main {
    /* モーター登録*/
    final RegulatedMotor motorCenter = Motor.A;
    final RegulatedMotor motorLeft = Motor.B;
    final RegulatedMotor motorRight = Motor.C;
    /* 車両情報登録*/
    // タイヤ直径(cm)
    private final float diameter = 5.6F;
    // 車輪の幅
    private final float width = 9.2F;
    /* 累積モーター角度*/
    int accumulationCenter, accumulationLeft, accumulationRight;
    /* カラーセンサー*/
    private EV3ColorSensor color;
    private SensorMode colorID;
    private float[] colorFloat;
    /* 超音波センサー*/
    private EV3UltrasonicSensor ultrasonic;
    private SampleProvider ultrasonicProvider;
    private float[] ultrasonicFloat;
    /* ジャイロセンサー*/
    private EV3GyroSensor gyro;
    private SampleProvider gyroProvider;
    private float[] gyroFloat;

    private Main() {
        /* 初期化処理*/
        // ディスプレイ案内
        LCD.clear();
        LCD.drawString("Initializing", 1, 6);
        LCD.refresh();
        // カラーセンサー
        color = new EV3ColorSensor(SensorPort.S1);
        colorID = color.getColorIDMode();
        colorFloat = new float[colorID.sampleSize()];
        colorUpdate();
        // 超音波センサー
        ultrasonic = new EV3UltrasonicSensor(SensorPort.S2);
        ultrasonicProvider = ultrasonic.getDistanceMode();
        ultrasonicFloat = new float[ultrasonicProvider.sampleSize()];
        ultrasonicUpdate();
        // ジャイロセンサー
        gyro = new EV3GyroSensor(SensorPort.S3);
        gyroProvider = gyro.getAngleMode();
        gyroFloat = new float[gyroProvider.sampleSize()];
        gyroUpdate();
        // モーター
        motorCenter.resetTachoCount();
        motorLeft.resetTachoCount();
        motorRight.resetTachoCount();
        // スレッド起動
        Scheduler st = new Scheduler(this);
        st.start();
        /* 選択*/
        Menu menu = new Menu();
        /* 開始確認*/
        LCD.drawString("Press Enter to Start", 1, 6);
        Button.ENTER.waitForPress();
        /* メイン処理*/
        LCD.drawString("Running", 1, 6);
        int wait = 10;
        if (menu.select() == 0) {
            moveStraight(300, wait, 90);
            moveRight(300, wait, 360);
            moveArm(wait, true);
            moveArm(wait, false);
        } else if (menu.select() == 1) {
            moveArm(wait, true);
            moveArm(wait, false);
        }
        /* 終了処理*/
        LCD.drawString("All Complete", 1, 6);
        st.countStop();
        Button.DOWN.waitForPress();
    }

    public static void main(String[] args) {
        new Main();
    }

    void lcdUpdate() {
        LCD.clear(0);
        LCD.drawString(String.valueOf((float) ((int) (Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.clear(1);
        LCD.drawString("C:" + accumulationCenter + " L:" + accumulationLeft + " R:" + accumulationRight, 1, 1);
        LCD.clear(2);
        LCD.drawString("Color:" + colorFloat[0], 1, 2);
        LCD.drawString("ColorN:" + colorID.getName(), 10, 2);
        LCD.clear(3);
        LCD.drawString("Ultrasonic:" + ultrasonicFloat[0], 1, 3);
        LCD.clear(4);
        LCD.drawString("Gyro:" + gyroFloat[0], 1, 4);
        LCD.refresh();
    }

    void colorUpdate() {
        colorID.fetchSample(colorFloat, 0);
    }

    void ultrasonicUpdate() {
        ultrasonicProvider.fetchSample(ultrasonicFloat, 0);
    }

    void gyroUpdate() {
        gyroProvider.fetchSample(gyroFloat, 0);
    }

    private void moveStraight(int speedMax, int wait, double distance) {
        LCD.clear(6);
        LCD.drawString("moveStraight", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = cum * 0.3F;

        // 移動開始
        motorLeft.forward();
        motorRight.forward();

        // 移動判定
        try {
            while (degreeLeft < cum) {
                if (degreeLeft > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeLeft) / distanceVariable + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speedMax;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                lcdUpdate();
                Thread.sleep(wait);
                degreeLeft = motorLeft.getTachoCount() - tacho_L;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止 flt()はフロート状態になる
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
    }

    private void moveRight(int speedMax, int wait, double angle) {
        LCD.clear(6);
        LCD.drawString("moveRight", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = cum * 0.3F;

        // 移動開始
        motorLeft.forward();
        motorRight.backward();

        // 移動判定
        try {
            while (degreeLeft < cum) {
                if (degreeLeft > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeLeft) / distanceVariable + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speedMax;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                lcdUpdate();
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
    }

    private void moveArm(int wait, boolean direction) {
        LCD.clear(6);
        LCD.drawString("moveArm", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_C = motorCenter.getTachoCount();
        int speedNow;
        int speedMax = 300;
        int speedMin = 100;
        int degreeLeft = 0;
        int angle = 300;
        motorCenter.setSpeed(speedMin);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = cum * 0.3F;

        // 移動判定
        if (direction) {
            motorCenter.forward();
            try {
                while (degreeLeft < cum) {
                    if (degreeLeft > cum - distanceVariable) {
                        //減速部
                        speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeLeft) / distanceVariable + speedMin);
                    } else if (degreeLeft < distanceVariable) {
                        //加速部
                        speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeLeft / distanceVariable) + speedMin);
                    } else {
                        //巡航部
                        speedNow = speedMax;
                    }
                    motorCenter.setSpeed(speedNow);
                    lcdUpdate();
                    Thread.sleep(wait);
                    degreeLeft = motorCenter.getTachoCount() - tacho_C;
                }
            } catch (InterruptedException ignored) {

            }
        } else {
            motorCenter.backward();
            cum = -cum;
            try {
                while (degreeLeft < cum) {
                    if (degreeLeft > cum - distanceVariable) {
                        //減速部
                        speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeLeft) / distanceVariable + speedMin);
                    } else if (degreeLeft < distanceVariable) {
                        //加速部
                        speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeLeft / distanceVariable) + speedMin);
                    } else {
                        //巡航部
                        speedNow = speedMax;
                    }
                    motorCenter.setSpeed(speedNow);
                    lcdUpdate();
                    Thread.sleep(wait);
                    degreeLeft = motorCenter.getTachoCount() - tacho_C;
                }
            } catch (InterruptedException ignored) {

            }
        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
    }
}
