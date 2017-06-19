import lejos.hardware.lcd.LCD;

public class Scheduler extends Thread {
    private final int sleepTime = 20;
    private Main parent = null;
    private boolean start = false;
    private boolean mode = true;

    public Scheduler(Main parent) {
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
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ie) {
            }
        }
    }

    public void countStop() {
        start = false;
    }
}
