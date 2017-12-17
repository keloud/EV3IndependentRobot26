package info.keloud.leJOS.motor;

import lejos.hardware.lcd.LCD;

import java.util.Objects;

public class Motor extends AbstractMotor implements ImplementMachine {
    @Override
    public void run() {
    }

    @Override
    public void setOperationMode(String operationMode) {
        LCD.clear(6);
        LCD.drawString(operationMode, 1, 6);
        LCD.refresh();
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
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
