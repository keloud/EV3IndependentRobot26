package info.keloud.leJOS.utils;

import info.keloud.leJOS.leJOS;
import info.keloud.leJOS.motor.AbstractMotor;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;

public abstract class AbstractUtil implements ImplementMachine, ImplementUtil {
    // the left running motor
    protected AbstractMotor leftMotor;
    // the right running motor
    protected AbstractMotor rightMotor;
    // the right running motor
    protected AbstractMotor centerMotor;
    // the color sensor
    protected ColorSensor colorSensor;
    // the ultrasonic sensor
    protected UltrasonicSensor ultrasonicSensor;
    // the gyro sensor
    protected GyroSensor gyroSensor;
    // Speed
    protected int speed;
    // Distance
    protected double distance;
    // Angle
    protected double angle;
    // Color
    protected int colorId;
    // Operation mode
    protected String operationMode = "Init AbstractUtil";

    @Override
    public void run() {
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    void setColorId(String colorId) {
        switch (colorId) {
            case "BLACK":
                setColorId(7);
                break;
            case "BLUE":
                setColorId(2);
                break;
            case "GREEN":
                //
                break;
            case "YELLOW":
                setColorId(3);
                break;
            case "RED":
                setColorId(0);
                break;
            case "WHITE":
                setColorId(6);
                break;
            case "BROWN":
                //
                break;
            default:
                //
                break;
        }
    }

    @Override
    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        leJOS.setOperationMode(operationMode);
    }
}
