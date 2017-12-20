package info.keloud.leJOS.manager;

import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;

public class DisplayUpdater {


    DisplayUpdater() {
    }

    public void updateValue(String operationMode, int accumulationleftMotor, int accumulationrightMotor, int accumulationcenterMotor, float colorIdValue, float ultrasonicValue, float gyroValue, int timer) {
        LCD.clear(0);
        LCD.drawString(String.valueOf((float) ((Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.clear(1);
        LCD.drawString("L:" + accumulationleftMotor + " R:" + accumulationrightMotor + " C:" + accumulationcenterMotor, 1, 1);
        LCD.clear(2);
        LCD.drawString("ColorId:" + colorIdValue, 1, 2);
        LCD.clear(3);
        LCD.drawString("USonic:" + ultrasonicValue, 1, 3);
        LCD.clear(4);
        LCD.drawString("Gyro:" + gyroValue, 1, 4);
        LCD.clear(6);
        LCD.drawString(operationMode, 1, 6);
        LCD.refresh();
        LCD.clear(7);
        LCD.drawString("Timer:" + timer, 1, 7);
        LCD.refresh();
    }
}
