package info.keloud.leJOS.utils.advanced;

import info.keloud.leJOS.motor.AbstractMotor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import info.keloud.leJOS.utils.AbstractUtil;
import info.keloud.leJOS.utils.TurnWithGyro;
import lejos.hardware.lcd.LCD;

// Process to search by spinning on the spot
public class SearchGyro extends AbstractUtil {
    private TurnWithGyro turnWithGyro;

    public SearchGyro(AbstractMotor leftMotor, AbstractMotor rightMotor, UltrasonicSensor ultrasonicSensor, GyroSensor gyroSensor, TurnWithGyro turnWithGyro) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.ultrasonicSensor = ultrasonicSensor;
        this.gyroSensor = gyroSensor;
        this.turnWithGyro = turnWithGyro;
    }

    @Override
    public void run() {
        setOperationMode("Search Gyro");
        //サーチ初期位置に動く
        turnWithGyro.setSpeed(100);
        turnWithGyro.setAngle(angle / 2);
        turnWithGyro.run();

        // 初期化
        float gyroInit = gyroSensor.getValue();
        float degreeGyro = 0;
        float gyroValue = degreeGyro;
        float degreeUltrasonic = ultrasonicSensor.getValue();
        float ultrasonicValue = degreeUltrasonic;
        setSpeed(80);
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);

        // 移動開始
        leftMotor.forward();
        rightMotor.backward();

        // 移動判定
        try {
            while (-angle < degreeGyro) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.getValue() - gyroInit;
                degreeUltrasonic = ultrasonicSensor.getValue();
                if (degreeUltrasonic < ultrasonicValue) {
                    ultrasonicValue = degreeUltrasonic;
                    gyroValue = degreeGyro;
                }
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        leftMotor.stop(true);
        rightMotor.stop(true);

        //turnWithGyro.setAngle(angle * 0.86 + gyroValue);
        turnWithGyro.run();

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
