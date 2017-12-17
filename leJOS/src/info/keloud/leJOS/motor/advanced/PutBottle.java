package info.keloud.leJOS.motor.advanced;

import info.keloud.leJOS.motor.AbstractMotor;

public class PutBottle extends AbstractMotor {
    PutBottle() {
        operationMode = "Put Bottle";
    }

    @Override
    public void run() {
        setOperationMode("Put Bottle");
    }
}
