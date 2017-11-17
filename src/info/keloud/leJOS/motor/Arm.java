package info.keloud.leJOS.motor;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class Arm extends MotorAdapter {
    private boolean state = false;

    public Arm(RegulatedMotor motorCenter) {
        this.motorCenter = motorCenter;
    }

    @Override
    public void run() {
        if (state) {
            LCD.clear(6);
            LCD.drawString("ArmClose", 1, 6);
            LCD.refresh();

            // 初期化
            int tacho_C = motorCenter.getTachoCount();
            int speedNow = 800;
            int degreeCenter = 0;
            int angle = 360;
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
            motorCenter.flt(true);
            state = false;
        } else {
            LCD.clear(6);
            LCD.drawString("ArmOpen", 1, 6);
            LCD.refresh();

            // 初期化
            int tacho_C = motorCenter.getTachoCount();
            int speedNow = 800;
            int degreeCenter = 0;
            int angle = 360;
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
            motorCenter.flt(true);
            state = true;
        }
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
