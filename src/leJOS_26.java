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

public class leJOS_26 {
    /* モーター*/
    final RegulatedMotor motorCenter = Motor.A;
    final RegulatedMotor motorLeft = Motor.B;
    final RegulatedMotor motorRight = Motor.C;
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

    private leJOS_26() {
        /* 初期化処理*/
        // ディスプレイ案内
        LCD.clear();
        LCD.drawString("Initializing", 1, 6);
        LCD.refresh();
        // モーター
        motorCenter.resetTachoCount();
        motorLeft.resetTachoCount();
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
        Menu menu = new Menu();
        MotorControl motor = new MotorControl(this);
        /* 開始確認*/
        LCD.clear(6);
        LCD.drawString("Press Enter to Start", 1, 6);
        LCD.refresh();
        Button.ENTER.waitForPress();
        /* メイン処理*/
        LCD.clear(6);
        LCD.drawString("Running", 1, 6);
        LCD.refresh();
        int wait = 10;
        motor.moveStraight(500, wait, 90);
        /* 終了処理*/
        LCD.clear(6);
        LCD.drawString("All Complete", 1, 6);
        LCD.refresh();
        st.countStop();
        Button.DOWN.waitForPress();
    }

    public static void main(String[] args) {
        new leJOS_26();
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
