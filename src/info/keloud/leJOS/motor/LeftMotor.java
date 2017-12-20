package info.keloud.leJOS.motor;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;

public class LeftMotor {
    private EV3LargeRegulatedMotor regulatedMotor;

    public LeftMotor() {
        regulatedMotor = new EV3LargeRegulatedMotor(MotorPort.B);
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
