import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public class Main {
    // 超音波センサー
    private static final EV3UltrasonicSensor sonar = new EV3UltrasonicSensor(SensorPort.S2);

    // カラーセンサー
    private static final EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);

    // ジャイロセンサー
    private static final EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);

    // モーター
    final RegulatedMotor motorCenter = Motor.A;
    final RegulatedMotor motorLeft = Motor.B;
    final RegulatedMotor motorRight = Motor.C;
    // 車両情報
    // タイヤ直径(cm)
    private final float wr = 5.6F;
    // 車輪の幅
    private final float track = 9.2F;
    //基礎計算情報
    // 円周率
    private final double pi = Math.PI;
    private int degreeCenter = 0, degreeLeft = 0, degreeRight = 0;

    public static void main(String[] args) {
        sonar.enable();
        color.getColorIDMode();
        gyro.getAngleMode();
    }

    private void lcdUpdate() {
        LCD.clear();
        LCD.drawString(String.valueOf((float) ((int) (Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.refresh();
    }

    private void sonarUpdate() {

    }

    private void colorUpdate() {

    }

    private void gyroUpdate() {

    }
}
