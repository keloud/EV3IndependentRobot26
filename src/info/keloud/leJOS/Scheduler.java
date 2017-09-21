package info.keloud.leJOS;

import lejos.hardware.lcd.LCD;

class Scheduler extends Thread {
    private final int sleepTime = 20;
    private leJOS parent = null;
    private boolean start = false;
    private boolean mode = true;

    Scheduler(leJOS parent) {
        this.parent = parent;
        start = true;
    }

    public void run() {
        int i = 0;
        while (start) {
            parent.accumulationMotorCenter = parent.motorCenter.getTachoCount();
            parent.accumulationMotorLeft = parent.motorLeft.getTachoCount();
            parent.accumulationMotorRight = parent.motorRight.getTachoCount();
            parent.sensorUpdate();
            i++;
            if (mode) {
                LCD.drawInt(i, 14, 7);
                LCD.refresh();
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ie) {
                LCD.clear(6);
                LCD.drawString("Error", 1, 6);
                LCD.refresh();
            }
        }
    }

    void countStop() {
        start = false;
    }
}
