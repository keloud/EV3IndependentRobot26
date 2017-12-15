package info.keloud.leJOS.InformationHandler;

import lejos.hardware.lcd.LCD;

public class Scheduler extends Thread {
    private Monitoring monitoring;
    private boolean mode;

    public Scheduler() {
        monitoring = new Monitoring();
        mode = true;
    }

    public void run() {
        int i = 0;
        monitoring.run();
        while (mode) {
            // モニタリング処理に値を渡す

            // カウントタイマーの表示
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

    public void countStop() {
        monitoring.monitoringStop();
        mode = false;
    }
}
