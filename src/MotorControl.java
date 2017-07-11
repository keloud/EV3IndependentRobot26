import lejos.hardware.lcd.LCD;

import java.util.Objects;

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
        LCD.refresh();
    }

    void moveStraightUseSonar(int speedMax, int wait, float valueUltrasonic) {
        LCD.clear(6);
        LCD.drawString("moveStraightUS", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = parent.motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        parent.motorLeft.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

        // 速度から必要な距離を求める(可変距離)
        double distanceVariable = speedMax * 0.27F;
        double distanceStop = speedMax * 0.5F;

        // 設定した超音波センサーの距離を角度累計に変換する
        int distanceUltrasonic = (int) ((valueUltrasonic * 100 / diameter / Math.PI) * 360);

        // 減速に使用する角度累計
        int distanceDeceleration = degreeLeft + (int) distanceVariable;

        // 移動開始
        parent.motorLeft.forward();
        parent.motorRight.forward();

        // 移動判定
        try {
            while (true) {
                // 設定した超音波センサーの距離+停止までに必要な距離まで更新し続ける。
                if (distanceUltrasonic + distanceStop < (int) ((parent.ultrasonicFloat[0] * 100 / diameter / Math.PI) * 360)) {
                    // 減速に必要な角度累計を代入する
                    distanceDeceleration = degreeLeft + (int) distanceStop;
                }
                // 停止する
                if ((int) ((parent.ultrasonicFloat[0] * 100 / diameter / Math.PI) * 360) < distanceUltrasonic) {
                    break;
                }
                // 減速部
                if (distanceDeceleration - distanceStop < degreeLeft) {
                    speedNow = (int) ((float) (speedMax - speedMin) * (distanceDeceleration - degreeLeft) / distanceStop + speedMin);
                }
                // 加速部
                else if (degreeLeft < distanceVariable) {
                    speedNow = (int) ((float) ((float) (speedMax - speedMin) * degreeLeft / distanceVariable) + speedMin);
                }
                // 巡行部
                else {
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

    void moveBackward(int speedMax, int wait, double distance) {
        LCD.clear(6);
        LCD.drawString("moveBackward", 1, 6);
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
        parent.motorLeft.backward();
        parent.motorRight.backward();

        // 移動判定
        try {
            while (degreeLeft < cum) {
                if (cum - distanceVariable < degreeLeft) {
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
                degreeLeft = -(parent.motorLeft.getTachoCount() - tacho_L);
            }
        } catch (InterruptedException ignored) {

        }

        // 停止 flt()はフロート状態になる
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    void moveBackwardUseColor(int speedMax, int wait, float colorId) {
        LCD.clear(6);
        LCD.drawString("moveBackwardUC", 1, 6);
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
        double distanceStop = speedMax * 0.5F;

        // 減速に使用する角度累計
        int distanceDeceleration = degreeLeft + (int) distanceVariable;

        // 移動開始
        parent.motorLeft.backward();
        parent.motorRight.backward();

        // 移動判定
        try {
            while (true) {
                //ColorIdまで必要な減速距離を更新し続ける
                if (parent.colorFloat[0] != colorId) {
                    distanceDeceleration = degreeLeft + (int) distanceStop;
                }
                //20cm後退して停止する
                if (distanceDeceleration - (int) ((18 / diameter / Math.PI) * 360) < degreeLeft) {
                    break;
                }
                if (distanceDeceleration - distanceStop < degreeLeft) {
                    //減速部
                    speedNow = (int) ((float) (speedMax - speedMin) * (distanceDeceleration - degreeLeft) / distanceStop + speedMin);
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
                degreeLeft = -(parent.motorLeft.getTachoCount() - tacho_L);
            }
        } catch (InterruptedException ignored) {

        }

        // 停止 flt()はフロート状態になる
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    void moveRightUseGyro(int speedMax, int wait, double angle) {
        LCD.clear(6);
        LCD.drawString("moveRightUS", 1, 6);
        LCD.refresh();
        // 初期化
        float gyroInit = parent.gyroFloat[0];
        float degreeGyro = 0;
        int speedNow = speedMax;
        int speedMin = 100;
        parent.motorLeft.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

        //角度修正
        angle = -angle;

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

    void moveLeftUseGyro(int speedMax, int wait, double angle) {
        LCD.clear(6);
        LCD.drawString("moveLeftUS", 1, 6);
        LCD.refresh();
        // 初期化
        float gyroInit = parent.gyroFloat[0];
        float degreeGyro = 0;
        int speedNow = speedMax;
        int speedMin = 100;
        parent.motorLeft.setSpeed(speedMin);
        parent.motorRight.setSpeed(speedMin);

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

        // 停止
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    void moveArm(int wait, int angle, String type) {
        if (Objects.equals(type, "Open")) {
            moveArmOpen(wait, angle);
        } else if (Objects.equals(type, "Close")) {
            moveArmClose(wait, angle);
        }
    }

    private void moveArmOpen(int wait, int angle) {
        LCD.clear(6);
        LCD.drawString("armOpen", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_C = parent.motorCenter.getTachoCount();
        int speedNow = 800;
        int degreeCenter = 0;
        parent.motorCenter.setSpeed(speedNow);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        // 移動開始
        parent.motorCenter.forward();

        try {
            while (degreeCenter < cum) {
                Thread.sleep(wait);
                degreeCenter = parent.motorCenter.getTachoCount() - tacho_C;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        parent.motorCenter.flt(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void moveArmClose(int wait, int angle) {
        LCD.clear(6);
        LCD.drawString("armClose", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_C = parent.motorCenter.getTachoCount();
        int speedNow = 800;
        int degreeCenter = 0;
        parent.motorCenter.setSpeed(speedNow);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);
        cum = -cum;

        // 移動開始
        parent.motorCenter.backward();

        try {
            while (cum < degreeCenter) {
                Thread.sleep(wait);
                degreeCenter = parent.motorCenter.getTachoCount() - tacho_C;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        parent.motorCenter.flt(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }
}
