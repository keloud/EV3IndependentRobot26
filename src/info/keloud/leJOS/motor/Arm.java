package info.keloud.leJOS.motor;

import info.keloud.leJOS.Monitoring;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

import java.util.Objects;

public class Arm extends MotorAdapter {

    private boolean state = false;

    public Arm(Monitoring monitoring, RegulatedMotor motorCenter) {
        this.monitoring = monitoring;
        this.motorCenter = motorCenter;
        setAngle(320);
        setSpeed(800);
        motorCenter.setSpeed(speed);
    }

    @Override
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
            if (state) {
                state = false;
                armClose();
            }
        }
        if (Objects.equals(setState, "Open")) {
            if (!state) {
                state = true;
                armOpen();
            }
        }

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void armOpen() {
        setBehavior("ArmOpen");

        // 初期化
        int initTachoCount = motorCenter.getTachoCount();
        int degreeTachoCount = 0;

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        // 移動開始
        motorCenter.forward();

        try {
            while (degreeTachoCount < cum) {
                Thread.sleep(wait);
                degreeTachoCount = motorCenter.getTachoCount() - initTachoCount;
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
        setBehavior("ArmClose");

        // 初期化
        int initTachoCount = motorCenter.getTachoCount();
        int degreeTachoCount = 0;

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);
        cum = -cum;

        // 移動開始
        motorCenter.backward();

        try {
            while (cum < degreeTachoCount) {
                Thread.sleep(wait);
                degreeTachoCount = motorCenter.getTachoCount() - initTachoCount;
            }
        } catch (InterruptedException ignored) {
            LCD.clear(6);
            LCD.drawString("Error", 1, 6);
            LCD.refresh();
        }

        // 停止
        motorCenter.stop(true);
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
