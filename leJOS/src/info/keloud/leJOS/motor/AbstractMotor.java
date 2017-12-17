package info.keloud.leJOS.motor;

import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public abstract class AbstractMotor implements ImplementMachine {
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

    abstract void run();

    abstract void setOperationMode(String operationMode);

    abstract void setSpeed(int speed);

    abstract void setDistance(double distance);

    abstract void setAngle(double angle);

    abstract void setColorId(int colorId);
}
