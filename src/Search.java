import lejos.hardware.lcd.LCD;

class Search {
    /* 車両情報*/
    // タイヤ直径(cm)
    private final float diameter = 5.6F;
    // 車輪の幅
    private final float width = 9.2F;
    //待機時間
    int wait = 10;
    private leJOS_26 parent = null;

    Search(leJOS_26 parent) {
        this.parent = parent;
    }

    void run() {
        LCD.clear(6);
        LCD.drawString("Searching", 1, 6);
        LCD.refresh();

        // 初期化
        float gyroInit = parent.gyroFloat[0];
        float degreeGyro = 0;
        int speed = 100;
        int angle = 30;
        parent.motorLeft.setSpeed(speed);
        parent.motorRight.setSpeed(speed);

        // 移動開始
        parent.motorLeft.backward();
        parent.motorRight.forward();

        // 移動判定
        try {
            while (degreeGyro < angle) {
                Thread.sleep(wait);
                degreeGyro = parent.gyroFloat[0] - gyroInit;
            }
        } catch (InterruptedException ignored) {

        }

        //初期化処理
        gyroInit = parent.gyroFloat[0];
        degreeGyro = 0;
        angle = -390;

        // 移動開始
        parent.motorLeft.forward();
        parent.motorRight.backward();

        // 移動判定
        try {
            while (angle < degreeGyro) {
                Thread.sleep(wait);
                degreeGyro = parent.gyroFloat[0] - gyroInit;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
