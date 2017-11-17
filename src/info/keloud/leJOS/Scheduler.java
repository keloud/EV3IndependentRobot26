package info.keloud.leJOS;

import lejos.hardware.lcd.LCD;

class Scheduler extends Thread {
    private leJOS parent = null;
    private boolean mode = false;

    Scheduler(leJOS parent) {
        this.parent = parent;
        mode = true;
    }

    public void run() {
        int i = 0;
        while (mode) {
            parent.accumulationMotorCenter = parent.motorCenter.getTachoCount();
            parent.accumulationMotorLeft = parent.motorLeft.getTachoCount();
            parent.accumulationMotorRight = parent.motorRight.getTachoCount();
            parent.sensorUpdate();

            LCD.drawInt(i, 14, 7);
            LCD.refresh();
            i++;

            try {
                Thread.sleep(20);
            } catch (InterruptedException ie) {
                LCD.clear(6);
                LCD.drawString("Error", 1, 6);
                LCD.refresh();
            }
        }
    }

    void countStop() {
        mode = false;
    }
}
