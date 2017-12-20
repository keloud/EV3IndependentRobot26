package info.keloud.leJOS.utils;

import info.keloud.leJOS.motor.AbstractMotor;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

import java.util.Objects;

public class Arm extends AbstractUtil {
    // Arm open is true
    private boolean state = false;

    public Arm(AbstractMotor centerMotor) {
        this.centerMotor = centerMotor;
        setAngle(290);
        setSpeed(800);
        centerMotor.setSpeed(speed);
    }

    public void run(int speed, int angle) {
        setSpeed(speed);
        setAngle(angle);
        run();
    }

    @Override
    public void run() {
        if (state) {
            armClose();
        } else {
            armOpen();
        }

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    public void run(String setState) {
        if (Objects.equals(setState, "CLOSE")) {
            if (state) {
                armClose();
            }
        }
        if (Objects.equals(setState, "OPEN")) {
            if (!state) {
                armOpen();
            }
        }
    }

    public void run(boolean setState) {
        if (!setState) {
            if (state) {
                armClose();
            }
        }
        if (setState) {
            if (!state) {
                armOpen();
            }
        }
    }

    private void armOpen() {
        // 初期化
        setOperationMode("Arm Open");
        state = true;
        int initTachoCount = centerMotor.getTachoCount();
        int degreeTachoCount = 0;

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        // 移動開始
        centerMotor.forward();

        try {
            while (degreeTachoCount < cum) {
                Thread.sleep(wait);
                degreeTachoCount = centerMotor.getTachoCount() - initTachoCount;
            }
        } catch (InterruptedException ignored) {
            Sound.beep();
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        centerMotor.stop(true);
    }

    private void armClose() {
        // 初期化
        setOperationMode("Arm Close");
        state = false;
        int initTachoCount = centerMotor.getTachoCount();
        int degreeTachoCount = 0;

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);
        cum = -cum;

        // 移動開始
        centerMotor.backward();

        try {
            while (cum < degreeTachoCount) {
                Thread.sleep(wait);
                degreeTachoCount = centerMotor.getTachoCount() - initTachoCount;
            }
        } catch (InterruptedException ignored) {
            Sound.beep();
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        centerMotor.stop(true);
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
