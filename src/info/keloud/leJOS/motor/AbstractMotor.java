package info.keloud.leJOS.motor;

import info.keloud.leJOS.leJOS;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public abstract class AbstractMotor implements ImplementMachine, ImplementMotor {
    // the left running motor
    protected RegulatedMotor motorLeft;
    // the right running motor
    protected RegulatedMotor motorRight;
    // the right running motor
    protected RegulatedMotor motorCenter;
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
    protected String operationMode = "Init AbstractMotor";

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
                setColorId(2);
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
