package info.keloud.leJOS.motor;

import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public abstract class Motor {
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
}
