package info.keloud.leJOS.motor;

import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public abstract class MotorAdapter {
    /* Vehicle information*/
    // Diameter of tire(cm)
    protected final float diameter = 5.6F;
    // Width of wheel
    protected final float width = 9.2F;
    // Thread wait time
    protected final int wait = 10;
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
    protected int speed = 400;
    // Distance
    protected double distance = 30;
    // Angle
    protected double angle = 90;
    // Color
    protected int colorId = 0;
    // Behavior mode information
    protected String behavior = "Initialize MotorAdapter";

    public void run() {
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
}
