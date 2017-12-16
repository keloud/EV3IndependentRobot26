package info.keloud.leJOS;

import info.keloud.leJOS.informationHandler.Monitoring;
import info.keloud.leJOS.informationHandler.Scheduler;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class leJOS {
    // Regulated モーター
    public static RegulatedMotor motorCenter;
    public static RegulatedMotor motorLeft;
    public static RegulatedMotor motorRight;
    // カラーセンサー
    public static ColorSensor colorSensor;
    // 超音波センサー
    public static UltrasonicSensor ultrasonicSensor;
    // ジャイロセンサー
    public static GyroSensor gyroSensor;
    // モニタリング処理
    public static Monitoring monitoring;

    public static void main(String[] args) {
        // ディスプレイ案内開始
        LCD.clear();
        LCD.drawString("Init ColorSensor", 1, 6);
        LCD.refresh();
        // カラーセンサーの初期化
        colorSensor = new ColorSensor();
        // ディスプレイ案内更新
        LCD.clear();
        LCD.drawString("Init UltrasonicSensor", 1, 6);
        LCD.refresh();
        // 超音波センサーの初期化
        ultrasonicSensor = new UltrasonicSensor();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Init GyroSensor", 1, 6);
        LCD.refresh();
        // ジャイロセンサーの初期化
        gyroSensor = new GyroSensor();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Init Motor", 1, 6);
        LCD.refresh();
        // モーターの初期化
        motorCenter = lejos.hardware.motor.Motor.A;
        motorCenter.resetTachoCount();
        motorLeft = lejos.hardware.motor.Motor.B;
        motorLeft.resetTachoCount();
        motorRight = lejos.hardware.motor.Motor.C;
        motorRight.resetTachoCount();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Init Thread", 1, 6);
        LCD.refresh();
        // センサーの初期化を促す
        for (int i = 0; i < 300; i++) {
            sensorUpdate();
        }
        // スレッド起動
        Scheduler scheduler = new Scheduler();
        scheduler.start();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("End of initialization processing", 1, 6);
        LCD.refresh();
        // 開始確認
        LCD.clear(6);
        LCD.drawString("Press Enter", 1, 6);
        LCD.refresh();
        Button.ENTER.waitForPress();
        // 動作開始

        // 終了処理
        LCD.clear(6);
        LCD.drawString("All Complete", 1, 6);
        LCD.refresh();
        // Enterキーを押して次に進む
        Button.ENTER.waitForPress();
        scheduler.countStop();
    }

    static void sensorUpdate() {
        LCD.clear(0);
        LCD.drawString(String.valueOf((float) ((int) (Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.clear(1);
        LCD.drawString("C:" + motorCenter.getTachoCount() + " L:" + motorLeft.getTachoCount() + " R:" + motorRight.getTachoCount(), 1, 1);
        LCD.clear(2);
        LCD.drawString("ColorId:" + colorSensor.getValue(), 1, 2);
        LCD.clear(3);
        LCD.drawString("USonic:" + ultrasonicSensor.getValue(), 1, 3);
        LCD.clear(4);
        LCD.drawString("Gyro:" + gyroSensor.getValue(), 1, 4);
        LCD.refresh();
    }
}