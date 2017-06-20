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
    /* モーター*/
    final RegulatedMotor motorCenter;
    final RegulatedMotor motorLeft;
    final RegulatedMotor motorRight;
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
        // モーター
        motorCenter = Motor.A;
        motorCenter.resetTachoCount();
        motorLeft = Motor.B;
        motorLeft.resetTachoCount();
        motorRight = Motor.C;
        motorRight.resetTachoCount();
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
        // スレッド起動
        Scheduler st = new Scheduler(this);
        st.start();
        /* オブジェクト化*/
        MotorControl motor = new MotorControl(this);
        Menu menu = new Menu();
        /* 開始確認*/
        LCD.drawString("Press Enter to Start", 1, 6);
        Button.ENTER.waitForPress();
        /* メイン処理*/
        LCD.drawString("Running", 1, 6);
        int wait = 10;
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


}
