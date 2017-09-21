package info.keloud.leJOS;

import lejos.hardware.lcd.LCD;

class Search {
    /* Vehicle information*/
    // Diameter of tire(cm)
    private float diameter;
    // Width of wheel
    private float width;
    // Thread wait time
    private int wait;
    // Objectization of leJOS
    private leJOS parent = null;

    Search(leJOS parent) {
        this.parent = parent;
        diameter = parent.diameter;
        width = parent.width;
        wait = parent.wait;
    }

    void stopSearching(int angle) {
        // 探索初期位置へ旋回
        parent.angle(100, angle / 2);

        LCD.clear(6);
        LCD.drawString("StopS", 1, 6);
        LCD.refresh();

        // 初期化
        float gyroInit = parent.gyroSensor.gyroFloat[0];
        float degreeGyro = parent.gyroSensor.gyroFloat[0] - gyroInit;
        float gyroValue = degreeGyro;
        float degreeUltrasonic = parent.ultrasonicSensor.ultrasonicFloat[0];
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
                degreeGyro = parent.gyroSensor.gyroFloat[0] - gyroInit;
                degreeUltrasonic = parent.ultrasonicSensor.ultrasonicFloat[0];
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
        parent.angle(100, angle * 0.86 + gyroValue);
    }
}
