package info.keloud.leJOS.informationHandler;

import lejos.hardware.lcd.LCD;

public class Scheduler extends Thread {
    private Monitoring monitoring;
    private boolean mode;

    public Scheduler() {
        monitoring = new Monitoring();
        mode = true;
    }

    public void run() {
        int timer = 0;
        monitoring.run();
        while (mode) {
            // モニタリング処理に値を渡す
            monitoring.setValue("", 0, 0, 0, 0, 0, 0);
            // カウントタイマーの表示
            LCD.drawInt(timer, 14, 7);
            LCD.refresh();
            timer++;
            // センサー系をアップデートする

            // 例外処理
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
