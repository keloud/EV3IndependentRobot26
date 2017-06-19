import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Main {
    // カラーセンサー
    private static final EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);

    // 超音波センサー
    private static final EV3UltrasonicSensor sonar = new EV3UltrasonicSensor(SensorPort.S2);
    // ジャイロセンサー
    private static final EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
    // モーター登録
    final RegulatedMotor motorCenter = Motor.A;
    final RegulatedMotor motorLeft = Motor.B;
    final RegulatedMotor motorRight = Motor.C;
    // タイヤ直径(cm)
    private final float wr = 5.6F;
    // 車輪の幅
    private final float track = 9.2F;
    // 円周率
    private final double pi = Math.PI;
    private SampleProvider sonarProvider;
    private float[] sonarFloat;
    // モーター角度
    private int degreeCenter = 0, degreeLeft = 0, degreeRight = 0;

    private Main() {
        for (int i = 0; i < 1500; i++) {
            colorUpdate();
            sonarUpdate();
            gyroUpdate();
            Battery.getVoltageMilliVolt();
        }
        Delay.msDelay(10000);
        lcdUpdate();
        sonar.enable();
        color.getColorIDMode();
        gyro.getAngleMode();
    }

    public static void main(String[] args) {
        new Main();
    }

    private void lcdUpdate() {
        LCD.clear();
        LCD.drawString(String.valueOf((float) ((int) (Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.refresh();
    }

    private void colorUpdate() {

    }

    private float sonarUpdate() {
        sonarProvider.fetchSample(sonarFloat, 0);
        return sonarFloat[0];
    }

    private void gyroUpdate() {

    }
}
