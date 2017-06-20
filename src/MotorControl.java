import lejos.hardware.lcd.LCD;

public class MotorControl {
    /* 車両情報*/
    // タイヤ直径(cm)
    private final float diameter = 5.6F;
    // 車輪の幅
    private final float width = 9.2F;
    private leJOS_26 parent = null;

    MotorControl(leJOS_26 parent) {
        this.parent = parent;
    }

    void moveStraight(int speedMax, int wait, double distance) {
        LCD.clear(6);
        LCD.drawString("moveStraight", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = parent.motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        parent.motorLeft.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speedMax * 0.24F;

        // 移動開始
        parent.motorLeft.forward();
        parent.motorRight.forward();

        // 移動判定
        try {
            while (degreeLeft < cum) {
                if (degreeLeft > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeLeft) / distanceVariable + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speedMax;
                }
                parent.motorLeft.setSpeed(speedNow);
                parent.motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = parent.motorLeft.getTachoCount() - tacho_L;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止 flt()はフロート状態になる
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
    }

    void moveStraightUseSonar(int speedMax, int wait, double distance) {
        LCD.clear(6);
        LCD.drawString("moveStraight", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = parent.motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        parent.motorLeft.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speedMax * 0.24F;

        // 移動開始
        parent.motorLeft.forward();
        parent.motorRight.forward();

        // 移動判定
        try {
            while (true) {
                if (distance * 3 > parent.ultrasonicFloat[0]) {
                    //減速に必要な事前距離
                    final int decelerationDistance = degreeLeft + (int) distanceVariable;
                    //減速部
                    speedNow = (int) ((float) (speedMax - speedMin) * (decelerationDistance - degreeLeft) / distanceVariable + speedMin);
                    if (distance > parent.ultrasonicFloat[0]) {
                        break;
                    }
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speedMax;
                }
                parent.motorLeft.setSpeed(speedNow);
                parent.motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = parent.motorLeft.getTachoCount() - tacho_L;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止 flt()はフロート状態になる
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
    }

    void moveRight(int speedMax, int wait, double angle) {
        LCD.clear(6);
        LCD.drawString("moveRight", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = parent.motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        parent.motorLeft.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speedMax * 0.24F;

        // 移動開始
        parent.motorLeft.forward();
        parent.motorRight.backward();

        // 移動判定
        try {
            while (degreeLeft < cum) {
                if (degreeLeft > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeLeft) / distanceVariable + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speedMax;
                }
                parent.motorLeft.setSpeed(speedNow);
                parent.motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = parent.motorLeft.getTachoCount() - tacho_L;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
    }

    void moveRightUseGyro(int speedMax, int wait, double angle) {
        LCD.clear(6);
        LCD.drawString("moveLeft", 1, 6);
        LCD.refresh();
        // 初期化
        float gyroInit = parent.gyroFloat[0];
        float degreeGyro = 0;
        int speedNow = speedMax;
        int speedMin = 100;
        parent.motorRight.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

        //可変速度に必要な角度を求める
        double distanceVariable = 45;

        // 移動開始
        parent.motorLeft.forward();
        parent.motorRight.backward();

        // 移動判定
        try {
            while (degreeGyro > angle) {
                parent.motorLeft.setSpeed(speedNow);
                parent.motorRight.setSpeed(speedNow);
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
    }

    void moveLeft(int speedMax, int wait, double angle) {
        LCD.clear(6);
        LCD.drawString("moveLeft", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = parent.motorRight.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeRight = 0;
        parent.motorRight.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = speedMax * 0.24F;

        // 移動開始
        parent.motorLeft.backward();
        parent.motorRight.forward();

        // 移動判定
        try {
            while (degreeRight < cum) {
                if (degreeRight > cum - distanceVariable) {
                    //減速部
                    speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeRight) / distanceVariable + speedMin);
                } else if (degreeRight < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeRight / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = speedMax;
                }
                parent.motorLeft.setSpeed(speedNow);
                parent.motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeRight = parent.motorRight.getTachoCount() - tacho_L;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
    }

    void moveLeftUseGyro(int speedMax, int wait, double angle) {
        LCD.clear(6);
        LCD.drawString("moveLeft", 1, 6);
        LCD.refresh();
        // 初期化
        float gyroInit = parent.gyroFloat[0];
        float degreeGyro = 0;
        int speedNow = speedMax;
        int speedMin = 100;
        parent.motorRight.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

        //可変速度に必要な角度を求める
        double distanceVariable = 45;

        // 移動開始
        parent.motorLeft.backward();
        parent.motorRight.forward();

        // 移動判定
        try {
            while (degreeGyro < angle) {
                parent.motorLeft.setSpeed(speedNow);
                parent.motorRight.setSpeed(speedNow);
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
    }

    void moveArm(int wait, boolean direction) {
        LCD.clear(6);
        LCD.drawString("moveArm", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_C = parent.motorCenter.getTachoCount();
        int speedNow;
        int speedMax = 300;
        int speedMin = 100;
        int degreeCenter = 0;
        int angle = 300;
        parent.motorCenter.setSpeed(speedMin);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = cum * 0.3F;

        // 移動判定
        if (direction) {
            parent.motorCenter.forward();
            try {
                while (degreeCenter < cum) {
                    if (degreeCenter > cum - distanceVariable) {
                        //減速部
                        speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeCenter) / distanceVariable + speedMin);
                    } else if (degreeCenter < distanceVariable) {
                        //加速部
                        speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeCenter / distanceVariable) + speedMin);
                    } else {
                        //巡航部
                        speedNow = speedMax;
                    }
                    parent.motorCenter.setSpeed(speedNow);
                    Thread.sleep(wait);
                    degreeCenter = parent.motorCenter.getTachoCount() - tacho_C;
                }
            } catch (InterruptedException ignored) {

            }
        } else {
            parent.motorCenter.backward();
            cum = -cum;
            try {
                while (degreeCenter < cum) {
                    if (degreeCenter > cum - distanceVariable) {
                        //減速部
                        speedNow = (int) ((float) (speedMax - speedMin) * (cum - degreeCenter) / distanceVariable + speedMin);
                    } else if (degreeCenter < distanceVariable) {
                        //加速部
                        speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeCenter / distanceVariable) + speedMin);
                    } else {
                        //巡航部
                        speedNow = speedMax;
                    }
                    parent.motorCenter.setSpeed(speedNow);
                    Thread.sleep(wait);
                    degreeCenter = parent.motorCenter.getTachoCount() - tacho_C;
                }
            } catch (InterruptedException ignored) {

            }
        }

        // 停止
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
    }
}
