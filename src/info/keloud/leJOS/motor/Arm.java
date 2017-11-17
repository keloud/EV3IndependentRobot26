package info.keloud.leJOS.motor;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

import java.util.Objects;

public class Arm extends MotorAdapter {

    public Arm(RegulatedMotor motorCenter) {
        this.motorCenter = motorCenter;
    }

    public void run(String setState) {
        if (Objects.equals(setState, "Close")) {
            LCD.clear(6);
            LCD.drawString("ArmClose", 1, 6);
            LCD.refresh();

            // 初期化
            int tacho_C = motorCenter.getTachoCount();
            int speedNow = 800;
            int degreeCenter = 0;
            int angle = 180;
            motorCenter.setSpeed(speedNow);

            // 移動距離計算
            double distance = (angle * width * Math.PI) / 360;

            // 角度累計計算
            int cum = (int) ((distance / diameter / Math.PI) * 360);
            cum = -cum;

            // 移動開始
            motorCenter.forward();

            try {
                while (degreeCenter < cum) {
                    Thread.sleep(wait);
                    degreeCenter = motorCenter.getTachoCount() - tacho_C;
                }
            } catch (InterruptedException ignored) {

            }

            // 停止
            motorCenter.stop(true);
        } else if (Objects.equals(setState, "Open")) {
            LCD.clear(6);
            LCD.drawString("ArmOpen", 1, 6);
            LCD.refresh();

            // 初期化
            int tacho_C = motorCenter.getTachoCount();
            int speedNow = 800;
            int degreeCenter = 0;
            int angle = 180;
            motorCenter.setSpeed(speedNow);

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

            }

            // 停止
            motorCenter.stop(true);
        }

        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
