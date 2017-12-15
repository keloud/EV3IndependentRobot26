package info.keloud.leJOS;

import info.keloud.leJOS.InformationHandler.Monitoring;
import info.keloud.leJOS.InformationHandler.Scheduler;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

public class leJOS {
    // Regulated モーター
    public RegulatedMotor motorCenter;
    public RegulatedMotor motorLeft;
    public RegulatedMotor motorRight;
    // カラーセンサー
    public ColorSensor colorSensor;
    // 超音波センサー
    public UltrasonicSensor ultrasonicSensor;
    // ジャイロセンサー
    public GyroSensor gyroSensor;
    // モニタリング処理
    public Monitoring monitoring;
    // モーターの累積角度
    int accumulationMotorCenter, accumulationMotorLeft, accumulationMotorRight;

    leJOS() {
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
        motorCenter = Motor.A;
        motorCenter.resetTachoCount();
        motorLeft = Motor.B;
        motorLeft.resetTachoCount();
        motorRight = Motor.C;
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
        info.keloud.leJOS.Motor motor = new info.keloud.leJOS.Motor(this);
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
        motor.run();
        // 終了処理
        LCD.clear(6);
        LCD.drawString("All Complete", 1, 6);
        LCD.refresh();
        // Enterキーを押して次に進む
        Button.ENTER.waitForPress();
        scheduler.countStop();
    }

    void sensorUpdate() {
        LCD.clear(0);
        LCD.drawString(String.valueOf((float) ((int) (Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.clear(1);
        LCD.drawString("C:" + accumulationMotorCenter + " L:" + accumulationMotorLeft + " R:" + accumulationMotorRight, 1, 1);
        LCD.clear(2);
        LCD.drawString("ColorId:" + colorSensor.getValue(), 1, 2);
        LCD.clear(3);
        LCD.drawString("USonic:" + ultrasonicSensor.getValue(), 1, 3);
        LCD.clear(4);
        LCD.drawString("Gyro:" + gyroSensor.getValue(), 1, 4);
        LCD.refresh();
    }
}
