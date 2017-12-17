package info.keloud.leJOS.informationManager;

import lejos.hardware.lcd.LCD;

public class Scheduler extends Thread {
    private Monitoring monitoring;
    private DisplayUpdater displayUpdater;
    private boolean mode;

    public Scheduler() {
        monitoring = new Monitoring();
        displayUpdater = new DisplayUpdater();
        mode = true;
    }

    public void run() {
        int timer = 0;
        monitoring.run();
        while (mode) {
            // サーバーに値を渡す
            monitoring.setValue("Non Operation", 0, 0, 0, 0, 0, 0);
            // 表示を更新する
            displayUpdater.updateValue("Non Operation", 0, 0, 0, 0, 0, 0);
            // カウントタイマーの表示
            LCD.drawInt(timer, 14, 7);
            LCD.refresh();
            timer++;
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
