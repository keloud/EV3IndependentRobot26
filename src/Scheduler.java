import lejos.hardware.lcd.LCD;

public class Scheduler extends Thread {
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
            parent.accumulationCenter = parent.motorCenter.getTachoCount();
            parent.accumulationLeft = parent.motorLeft.getTachoCount();
            parent.accumulationRight = parent.motorRight.getTachoCount();
            parent.colorUpdate();
            parent.ultrasonicUpdate();
            parent.gyroUpdate();
            i++;
            if (mode) {
                LCD.drawInt(i, 14, 7);
                LCD.refresh();
            }
            parent.lcdUpdate();
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
