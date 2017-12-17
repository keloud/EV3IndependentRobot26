package info.keloud.leJOS.informationManager;

import lejos.hardware.lcd.LCD;

public class Scheduler extends Thread {
    private String operationMode = "non Operation";
    private int accumulationMotorCenter, accumulationMotorLeft, accumulationMotorRight;
    private float colorIdValue, ultrasonicValue, gyroValue;
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
            monitoring.setValue(operationMode, accumulationMotorCenter, accumulationMotorLeft, accumulationMotorRight, colorIdValue, ultrasonicValue, gyroValue);
            // 表示を更新する
            displayUpdater.updateValue(operationMode, accumulationMotorCenter, accumulationMotorLeft, accumulationMotorRight, colorIdValue, ultrasonicValue, gyroValue);
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

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }
}
