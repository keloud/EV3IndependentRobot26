package info.keloud.leJOS.motor;

import lejos.hardware.motor.BaseRegulatedMotor;

public class AbstractMotor {
    public BaseRegulatedMotor regulatedMotor;

    public void forward() {
        regulatedMotor.forward();
    }

    public void backward() {
        regulatedMotor.backward();
    }

    public void stop() {
        regulatedMotor.stop();
    }

    public void stop(boolean immediateReturn) {
        regulatedMotor.stop(immediateReturn);
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
