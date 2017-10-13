package info.keloud.leJOS.motor;

import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

abstract class MotorAdapter {
    /* Vehicle information*/
    // Diameter of tire(cm)
    final float diameter = 5.6F;
    // Width of wheel
    final float width = 9.2F;
    // Thread wait time
    final int wait = 10;
    // the left running motor
    RegulatedMotor motorLeft;
    // the right running motor
    RegulatedMotor motorRight;
    // the color sensor
    ColorSensor colorSensor;
    // the ultrasonic sensor
    UltrasonicSensor ultrasonicSensor;
    // the gyro sensor
    GyroSensor gyroSensor;
    // Speed
    int speed=400;
    // Distance
    int distance=30;
    // Angle
    int angle=90;
    // Behavior mode information
    String behavior = "Initialize MotorAdapter";

    public void run(){
        LCD.clear(6);
        LCD.drawString(behavior, 1, 6);
        LCD.refresh();

        running();

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void running(){

    }

    public void setSpeed(int speed){
        this.speed = speed;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public void setAngle(int angle){
        this.angle = angle;
    }
}
