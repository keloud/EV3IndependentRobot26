package info.keloud.leJOS;

import info.keloud.leJOS.motor.Forward;
import info.keloud.leJOS.motor.ForwardSonar;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

import java.util.Objects;

class leJOS {
    /* Vehicle information*/
    // Diameter of tire(cm)
    final float diameter = 5.6F;
    // Width of wheel
    final float width = 9.2F;
    // Thread wait time
    int wait = 10;

    ColorSensor colorSensor;
    UltrasonicSensor ultrasonicSensor;
    GyroSensor gyroSensor;

    /* Motor information*/
    RegulatedMotor motorCenter;
    RegulatedMotor motorLeft;
    RegulatedMotor motorRight;
    // Cumulative rotation of the motor
    int accumulationMotorCenter, accumulationMotorLeft, accumulationMotorRight;

    leJOS() {
        /* 初期化処理*/
        // ディスプレイ案内開始
        LCD.clear();
        LCD.drawString("Initializing", 1, 6);
        LCD.refresh();
        // カラーセンサーの初期化
        colorSensor = new ColorSensor();
        colorSensor.initialize();
        // ディスプレイ案内更新
        LCD.clear();
        LCD.drawString("Initializing.", 1, 6);
        LCD.refresh();
        // 超音波センサーの初期化
        ultrasonicSensor = new UltrasonicSensor();
        ultrasonicSensor.initialize();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Initializing..", 1, 6);
        LCD.refresh();
        // ジャイロセンサーの初期化
        gyroSensor = new GyroSensor();
        gyroSensor.initialize();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Initializing...", 1, 6);
        LCD.refresh();
        // モーターの初期化
        motorCenter = Motor.A;
        motorCenter.resetTachoCount();
        motorLeft = Motor.B;
        motorLeft.resetTachoCount();
        motorRight = Motor.C;
        motorRight.resetTachoCount();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Initializing....", 1, 6);
        LCD.refresh();
        // センサーの初期化を促す
        for (int i = 0; i < 300; i++) {
            sensorUpdate();
        }
        // スレッド起動
        Scheduler scheduler = new Scheduler(this);
        scheduler.start();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Initializing.....", 1, 6);
        LCD.refresh();
        /* オブジェクト化*/
        Search search = new Search(this);
        Forward forward = new Forward(motorLeft, motorRight);
        ForwardSonar forwardSonar = new ForwardSonar(motorLeft, motorRight, ultrasonicSensor);
        /* 開始確認*/
        LCD.clear(6);
        LCD.drawString("Press Enter", 1, 6);
        LCD.refresh();
        Button.ENTER.waitForPress();
        /* メイン処理*/
        LCD.clear(6);
        LCD.drawString("Running", 1, 6);
        LCD.refresh();
        forwardSonar.run(800, 0.06F);
        arm("Open");
        forward.run(100, 7);
        arm("Close");
        angle(100,-90);
        backwardColor(600,0);
        arm("Open");
        backward(300,10);
        angle(100,-90);
        forwardColor(400,6);
        search.stopSearching(50);
        forwardSonar.run(800, 0.06F);
        forward.run(100, 7);
        arm("Close");
        backwardColor(600,0);
        arm("Open");
        backward(300,10);
        angle(100,20);
        forward.run(800, 100);
        forwardColor(200,7);
        angle(100,60);
        forwardColor(800,3);
        arm("Close");

        /* 終了処理*/
        LCD.clear(6);
        LCD.drawString("All Complete", 1, 6);
        LCD.refresh();
        Button.ENTER.waitForPress();
        scheduler.countStop();
    }

    void sensorUpdate() {
        colorSensor.update();
        ultrasonicSensor.update();
        gyroSensor.update();
        LCD.clear(0);
        LCD.drawString(String.valueOf((float) ((int) (Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.clear(1);
        LCD.drawString("C:" + accumulationMotorCenter + " L:" + accumulationMotorLeft + " R:" + accumulationMotorRight, 1, 1);
        LCD.clear(2);
        LCD.drawString("ColorN:" + colorSensor.colorFloat[0], 1, 2);
        LCD.clear(3);
        LCD.drawString("Sonic:" + ultrasonicSensor.ultrasonicFloat[0], 1, 3);
        LCD.clear(4);
        LCD.drawString("Gyro:" + gyroSensor.gyroFloat[0] + " ℃", 1, 4);
        LCD.refresh();
    }
    
    /*
    Forward
     */

    private void forwardSonar() {

    }

    private void forwardColor(int maximumSpeed, float colorId) {
        LCD.clear(6);
        LCD.drawString("ForwardUC", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = maximumSpeed * 0.24F;
        double distanceStop = maximumSpeed * 0.5F;

        // 減速に使用する角度累計
        int distanceDeceleration = degreeLeft + (int) distanceVariable;

        // 移動開始
        motorLeft.forward();
        motorRight.forward();

        // 移動判定
        try {
            while (true) {
                //ColorIdまで必要な減速距離を更新し続ける
                if (colorSensor.colorFloat[0] != colorId) {
                    distanceDeceleration = degreeLeft + (int) distanceStop;
                }
                //後退して停止する
                if (distanceDeceleration < degreeLeft) {
                    break;
                }
                if (distanceDeceleration - distanceStop < degreeLeft) {
                    //減速部
                    speedNow = (int) ((float) (maximumSpeed - speedMin) * (distanceDeceleration - degreeLeft) / distanceStop + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (maximumSpeed - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = maximumSpeed;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = motorLeft.getTachoCount() - tacho_L;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止 flt()はフロート状態になる
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    /*
    Backward
     */

    private void backward(int maximumSpeed, double distance) {
        LCD.clear(6);
        LCD.drawString("Backward", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = maximumSpeed * 0.24F;

        // 移動開始
        motorLeft.backward();
        motorRight.backward();

        // 移動判定
        try {
            while (degreeLeft < cum) {
                if (cum - distanceVariable < degreeLeft) {
                    //減速部
                    speedNow = (int) ((float) (maximumSpeed - speedMin) * (cum - degreeLeft) / distanceVariable + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (maximumSpeed - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = maximumSpeed;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = -(motorLeft.getTachoCount() - tacho_L);
            }
        } catch (InterruptedException ignored) {

        }

        // 停止 flt()はフロート状態になる
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void backwardColor(int maximumSpeed, float colorId) {
        LCD.clear(6);
        LCD.drawString("BackwardUC", 1, 6);
        LCD.refresh();
        // 初期化
        int tacho_L = motorLeft.getTachoCount();
        int speedNow;
        int speedMin = 100;
        int degreeLeft = 0;
        motorLeft.setSpeed(speedMin);
        motorRight.setSpeed(speedMin);

        //速度から必要な距離を求める(可変距離)
        double distanceVariable = maximumSpeed * 0.24F;
        double distanceStop = maximumSpeed * 0.5F;

        // 減速に使用する角度累計
        int distanceDeceleration = degreeLeft + (int) distanceVariable;

        // 移動開始
        motorLeft.backward();
        motorRight.backward();

        // 移動判定
        try {
            while (true) {
                //ColorIdまで必要な減速距離を更新し続ける
                if (colorSensor.colorFloat[0] != colorId) {
                    distanceDeceleration = degreeLeft + (int) distanceStop;
                }
                //後退して停止する
                if (distanceDeceleration < degreeLeft) {
                    break;
                }
                if (distanceDeceleration - distanceStop < degreeLeft) {
                    //減速部
                    speedNow = (int) ((float) (maximumSpeed - speedMin) * (distanceDeceleration - degreeLeft) / distanceStop + speedMin);
                } else if (degreeLeft < distanceVariable) {
                    //加速部
                    speedNow = (int) ((float) ((float) (maximumSpeed - speedMin) * degreeLeft / distanceVariable) + speedMin);
                } else {
                    //巡航部
                    speedNow = maximumSpeed;
                }
                motorLeft.setSpeed(speedNow);
                motorRight.setSpeed(speedNow);
                Thread.sleep(wait);
                degreeLeft = -(motorLeft.getTachoCount() - tacho_L);
            }
        } catch (InterruptedException ignored) {

        }

        // 停止 flt()はフロート状態になる
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    /*
    Angle
    Left turn is +.
    Right turn is -.
     */

    void angle(int maximumSpeed, double angle) {
        if (angle < 0) {
            rightGyro(maximumSpeed, angle);
        } else if (0 < angle) {
            leftGyro(maximumSpeed, angle);
        }
    }

    private void rightGyro(int speed, double angle) {
        LCD.clear(6);
        LCD.drawString("RightUS", 1, 6);
        LCD.refresh();
        // 初期化
        float gyroInit = gyroSensor.gyroFloat[0];
        float degreeGyro = 0;
        motorLeft.setSpeed(speed);
        motorRight.setSpeed(speed);

        // 移動開始
        motorLeft.forward();
        motorRight.backward();

        // 移動判定
        try {
            while (angle < degreeGyro) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.gyroFloat[0] - gyroInit;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void leftGyro(int speed, double angle) {
        LCD.clear(6);
        LCD.drawString("LeftUS", 1, 6);
        LCD.refresh();
        // 初期化
        float gyroInit = gyroSensor.gyroFloat[0];
        float degreeGyro = 0;
        motorLeft.setSpeed(speed);
        motorRight.setSpeed(speed);

        // 移動開始
        motorLeft.backward();
        motorRight.forward();

        // 移動判定
        try {
            while (degreeGyro < angle) {
                Thread.sleep(wait);
                degreeGyro = gyroSensor.gyroFloat[0] - gyroInit;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorLeft.stop(true);
        motorRight.stop(true);
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    /*
    Arm
    It can use "Open" or "Close".
     */

    private void arm(String behavior) {
        LCD.clear(6);
        LCD.drawString("arm" + behavior, 1, 6);
        LCD.refresh();
        if (Objects.equals(behavior, "Open")) {
            armOpen();
        } else if (Objects.equals(behavior, "Close")) {
            armClose();
        }
        LCD.clear(6);
        LCD.drawString("Stopped", 1, 6);
        LCD.refresh();
    }

    private void armOpen() {
        // 初期化
        int tacho_C = motorCenter.getTachoCount();
        int speedNow = 800;
        int degreeCenter = 0;
        int angle = 360;
        motorCenter.setSpeed(speedNow);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);

        // 移動開始
        motorCenter.forward();

        try {
            while (degreeCenter < cum) {
                Thread.sleep(wait);
                degreeCenter = motorCenter.getTachoCount() - tacho_C;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorCenter.flt(true);
    }

    private void armClose() {
        // 初期化
        int tacho_C = motorCenter.getTachoCount();
        int speedNow = 800;
        int degreeCenter = 0;
        int angle = 360;
        motorCenter.setSpeed(speedNow);

        // 移動距離計算
        double distance = (angle * width * Math.PI) / 360;

        // 角度累計計算
        int cum = (int) ((distance / diameter / Math.PI) * 360);
        cum = -cum;

        // 移動開始
        motorCenter.backward();

        try {
            while (cum < degreeCenter) {
                Thread.sleep(wait);
                degreeCenter = motorCenter.getTachoCount() - tacho_C;
            }
        } catch (InterruptedException ignored) {

        }

        // 停止
        motorCenter.stop(true);
    }
}
