package info.keloud.leJOS.motor.Advanced;

import info.keloud.leJOS.motor.Arm;
import info.keloud.leJOS.motor.Forward;
import info.keloud.leJOS.motor.ForwardSonar;
import info.keloud.leJOS.motor.MotorAdapter;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class GetBottle extends MotorAdapter {
    private Arm arm;
    private Forward forward;
    private ForwardSonar forwardSonar;

    public GetBottle(RegulatedMotor motorLeft, RegulatedMotor motorRight, RegulatedMotor motorCenter, UltrasonicSensor ultrasonicSensor, Arm arm, Forward forward, ForwardSonar forwardSonar) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.motorCenter = motorCenter;
        this.ultrasonicSensor = ultrasonicSensor;
        this.arm = arm;
        this.forward = forward;
        this.forwardSonar = forwardSonar;
        behavior = "GetBottle";
    }

    @Override
    public void run() {
        LCD.clear(6);
        LCD.drawString(behavior, 1, 6);
        LCD.refresh();

        //速度(800)手前距離(6cm)で前進
        forwardSonar.setSpeed(800);
        forwardSonar.setDistance(6);
        forwardSonar.run();
        //スピード(100)走行距離(7cm)で前進
        forward.setSpeed(100);
        forward.setDistance(7);
        forward.run();
        //アームを閉じる
        arm.run("Close");

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
