import lejos.hardware.lcd.LCD;

import java.util.Objects;

class Move {
    /* 車両情報*/
    // タイヤ直径(cm)
    private final float diameter = 5.6F;
    // 車輪の幅
    private final float width = 9.2F;
    // 待機時間
    private int wait = 10;
    // 親から値を持ってくる
    private leJOS_26 parent = null;

    Move(leJOS_26 parent) {
        this.parent = parent;
    }

    /*
    Forward
     */

    void forwardDefault(int speedMax, double distance) {
        LCD.clear(6);
        LCD.drawString("Straight", 1, 6);
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

    void forwardUseSonar(int speedMax, float valueUltrasonic) {
        LCD.clear(6);
        LCD.drawString("StraightUS", 1, 6);
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

    /*
    Backward
     */

    void backwardDefault(int speedMax, double distance) {
        LCD.clear(6);
        LCD.drawString("Backward", 1, 6);
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

    void backwardUseColor(int speedMax, float colorId) {
        LCD.clear(6);
        LCD.drawString("BackwardUC", 1, 6);
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
                //後退して停止する
                if (distanceDeceleration < degreeLeft) {
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

    /*
    Angle
    Right is +.
    Left is -.
     */

    void angle(int speedMax, double angle) {
        if (angle < 0) {
            rightUseGyro(speedMax, angle);
        } else if (0 < angle) {
            leftUseGyro(speedMax, angle);
        }
    }

    private void rightUseGyro(int speed, double angle) {
        LCD.clear(6);
        LCD.drawString("RightUS", 1, 6);
        LCD.refresh();
        // 初期化
        float gyroInit = parent.gyroFloat[0];
        float degreeGyro = 0;
        parent.motorLeft.setSpeed(speed);
        parent.motorRight.setSpeed(speed);

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

    private void leftUseGyro(int speed, double angle) {
        LCD.clear(6);
        LCD.drawString("LeftUS", 1, 6);
        LCD.refresh();
        // 初期化
        float gyroInit = parent.gyroFloat[0];
        float degreeGyro = 0;
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

        // 停止
        parent.motorLeft.stop(true);
        parent.motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    /*
    Arm
    It can use "Open" or "Close".
     */

    void arm(int angle, String behavior) {
        LCD.clear(6);
        LCD.drawString("arm" + behavior, 1, 6);
        LCD.refresh();
        if (Objects.equals(behavior, "Open")) {
            armOpen(angle);
        } else if (Objects.equals(behavior, "Close")) {
            armClose(angle);
        }
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void armOpen(int angle) {
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
    }

    private void armClose(int angle) {
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
        parent.motorCenter.stop(true);
    }
}
