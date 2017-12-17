package info.keloud.leJOS.informationManager;

import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;

public class DisplayUpdater {


    DisplayUpdater() {
    }

    public void updateValue(String operationMode, int accumulationMotorCenter, int accumulationMotorLeft, int accumulationMotorRight, float colorIdValue, float ultrasonicValue, float gyroValue) {
        LCD.clear(0);
        LCD.drawString(String.valueOf((float) ((int) (Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.clear(1);
        LCD.drawString("C:" + accumulationMotorCenter + " L:" + accumulationMotorLeft + " R:" + accumulationMotorRight, 1, 1);
        LCD.clear(2);
        LCD.drawString("ColorId:" + colorIdValue, 1, 2);
        LCD.clear(3);
        LCD.drawString("USonic:" + ultrasonicValue, 1, 3);
        LCD.clear(4);
        LCD.drawString("Gyro:" + gyroValue, 1, 4);
        LCD.clear(6);
        LCD.drawString(operationMode, 1, 6);
        LCD.refresh();
    }
}
