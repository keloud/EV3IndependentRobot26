package info.keloud.leJOS.motor;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class CenterMotor {
    private EV3MediumRegulatedMotor regulatedMotor;

    public CenterMotor() {
        regulatedMotor = new EV3MediumRegulatedMotor(MotorPort.A);
    }

    public void forward() {
        regulatedMotor.forward();
    }

    public void backward() {
        regulatedMotor.backward();
    }

    public void stop() {
        regulatedMotor.stop();
    }

    public void flt() {
        regulatedMotor.flt();
    }

    public int getTachoCount() {
        return regulatedMotor.getTachoCount();
    }

    public void setAcceleration(int acceleration) {
        regulatedMotor.setAcceleration(acceleration);
    }

    public void setSpeed(int speed) {
        regulatedMotor.setSpeed(speed);
    }
}
