import lejos.hardware.lcd.LCD;

class Search {
    /* 車両情報*/
    // タイヤ直径(cm)
    private final float diameter = 5.6F;
    // 車輪の幅
    private final float width = 9.2F;
    // 移動用
    private Move move;
    // 親から値を持ってくる
    private leJOS parent = null;
    // 待機時間
    private int wait = 10;

    Search(leJOS parent) {
        this.parent = parent;
        move = new Move(parent);
    }

    void stopSearching(int angle) {
        // 探索初期位置へ旋回
        move.angle(100, angle / 2);

        LCD.clear(6);
        LCD.drawString("StopS", 1, 6);
        LCD.refresh();

        // 初期化
        float gyroInit = parent.gyroFloat[0];
        float degreeGyro = parent.gyroFloat[0] - gyroInit;
        float gyroValue = degreeGyro;
        float degreeUltrasonic = parent.ultrasonicFloat[0];
        float ultrasonicValue = degreeUltrasonic;
        int speed = 100;
        parent.motorLeft.setSpeed(speed);
        parent.motorRight.setSpeed(speed);

        // 移動開始
        parent.motorLeft.forward();
        parent.motorRight.backward();

        // 移動判定
        try {
            while (-angle < degreeGyro) {
                Thread.sleep(wait);
                degreeGyro = parent.gyroFloat[0] - gyroInit;
                degreeUltrasonic = parent.ultrasonicFloat[0];
                if (degreeUltrasonic < ultrasonicValue) {
                    ultrasonicValue = degreeUltrasonic;
                    gyroValue = degreeGyro;
                }
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();

        // 探索角度へ旋回
        move.angle(100, angle * 0.86 + gyroValue);
    }
}
