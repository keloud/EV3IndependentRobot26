package info.keloud.leJOS.motor;

import info.keloud.leJOS.informationHandler.Monitoring;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

import java.util.Objects;

public class Motor implements Machine {
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
    protected String behavior;
    protected Monitoring monitoring;

    public void run() {

    }

    public String setBehavior(String behavior) {
        LCD.clear(6);
        LCD.drawString(behavior, 1, 6);
        LCD.refresh();
        return behavior;
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

    public void setColorId(String colorId) {
        if (Objects.equals(colorId, "red")) {
            this.colorId = 0;
        } else if (Objects.equals(colorId, "yellow")) {
            this.colorId = 3;
        } else if (Objects.equals(colorId, "white")) {
            this.colorId = 6;
        }
    }
}
