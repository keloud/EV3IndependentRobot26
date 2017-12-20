package info.keloud.leJOS.motor;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class CenterMotor extends AbstractMotor {
    public CenterMotor() {
        regulatedMotor = new EV3MediumRegulatedMotor(MotorPort.A);
    }
}
