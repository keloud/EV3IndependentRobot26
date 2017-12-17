package info.keloud.leJOS.motor;

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
    protected String operationMode;

    @Override
    public void run() {
    }

    @Override
    public String getOperationMode() {
        return "Init AbstractMotor";
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

    public void setColorId(String colorId) {
        switch (colorId) {
            case "BLACK":
                this.colorId = 1;
                break;
            case "BLUE":
                this.colorId = 2;
                break;
            case "GREEN":
                this.colorId = 3;
                break;
            case "YELLOW":
                this.colorId = 4;
                break;
            case "RED":
                this.colorId = 5;
                break;
            case "WHITE":
                this.colorId = 6;
                break;
            case "BROWN":
                this.colorId = 7;
                break;
            default:
                this.colorId = 0;
        }
    }
}
