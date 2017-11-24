package info.keloud.leJOS.motor;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

import java.util.Objects;

public class Arm extends MotorAdapter {

    private boolean state = false;

    public Arm(RegulatedMotor motorCenter) {
        this.motorCenter = motorCenter;
        setAngle(320);
        setSpeed(800);
        motorCenter.setSpeed(speed);
    }

    public void run() {
        if (state) {
            state = false;
            armClose();
        } else {
            state = true;
            armOpen();
        }

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    public void run(String setState) {
        if (Objects.equals(setState, "Close")) {
            state = false;
            armClose();
        }
        if (Objects.equals(setState, "Open")) {
            state = true;
            armOpen();
        }

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void armOpen() {
        LCD.clear(6);
        LCD.drawString("ArmOpen", 1, 6);
        LCD.refresh();

        // 初期化
        int tacho_C = motorCenter.getTachoCount();
        int degreeCenter = 0;

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        // 移動開始
        motorCenter.forward();

        try {
            while (degreeCenter < cum) {
                Thread.sleep(wait);
                degreeCenter = motorCenter.getTachoCount() - tacho_C;
            }
        } catch (InterruptedException ignored) {
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        motorCenter.stop(true);
    }

    private void armClose() {
        LCD.clear(6);
        LCD.drawString("ArmClose", 1, 6);
        LCD.refresh();

        // 初期化
        int tacho_C = motorCenter.getTachoCount();
        int degreeCenter = 0;

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);
        cum = -cum;

        // 移動開始
        motorCenter.backward();

        try {
            while (cum < degreeCenter) {
                Thread.sleep(wait);
                degreeCenter = motorCenter.getTachoCount() - tacho_C;
            }
        } catch (InterruptedException ignored) {
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        motorCenter.stop(true);
    }
}
