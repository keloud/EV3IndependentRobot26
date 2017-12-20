package info.keloud.leJOS.motor;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class LeftMotor extends AbstractMotor {
    public LeftMotor() {
        regulatedMotor = new EV3LargeRegulatedMotor(MotorPort.B);
    }
}
