package info.keloud.leJOS;

import info.keloud.leJOS.informationManager.Scheduler;
import info.keloud.leJOS.motor.*;
import info.keloud.leJOS.motor.advanced.GrabBottle;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;

public class leJOS {
    // スレッド スケジューラー
    private static Scheduler scheduler;
    // モーター操作
    private static Arm arm;
    private static Forward forward;
    private static ForwardColor forwardColor;
    private static Backward backward;
    private static BackwardColor backwardColor;
    private static Turn turn;
    private static GrabBottle grabBottle;

    public static void main(String[] args) {
        // ディスプレイ案内開始
        LCD.clear();
        LCD.drawString("Init ColorSensor", 1, 6);
        LCD.refresh();
        // カラーセンサーの初期化
        ColorSensor colorSensor = new ColorSensor();
        // ディスプレイ案内更新
        LCD.clear();
        LCD.drawString("Init UltrasonicSensor", 1, 6);
        LCD.refresh();
        // 超音波センサーの初期化
        UltrasonicSensor ultrasonicSensor = new UltrasonicSensor();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Init GyroSensor", 1, 6);
        LCD.refresh();
        // ジャイロセンサーの初期化
        GyroSensor gyroSensor = new GyroSensor();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Init AbstractMotor", 1, 6);
        LCD.refresh();
        // モーターの初期化
        RegulatedMotor motorCenter = lejos.hardware.motor.Motor.A;
        motorCenter.resetTachoCount();
        RegulatedMotor motorLeft = lejos.hardware.motor.Motor.B;
        motorLeft.resetTachoCount();
        RegulatedMotor motorRight = lejos.hardware.motor.Motor.C;
        motorRight.resetTachoCount();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Init Thread", 1, 6);
        LCD.refresh();
        // スレッドオブジェクトの作成
        scheduler = new Scheduler(motorLeft, motorRight, motorCenter, ultrasonicSensor, colorSensor, gyroSensor);
        scheduler.start();
        // モーター操作の設定
        arm = new Arm(motorCenter);
        forward = new Forward(motorLeft, motorRight);
        forwardColor = new ForwardColor(motorLeft, motorRight, colorSensor);
        backward = new Backward(motorLeft, motorRight);
        backwardColor = new BackwardColor(motorLeft, motorRight, colorSensor);
        turn = new Turn(motorLeft, motorRight);
        grabBottle = new GrabBottle(motorLeft, motorRight, motorCenter, ultrasonicSensor, colorSensor, arm, forward);
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("End of initialization processing", 1, 6);
        LCD.refresh();
        // メニューを開く
        menu();
        // 終了処理
        scheduler.setOperationMode("All Complete");
        // Enterキーを押して次に進む
        Button.ENTER.waitForPress();
        scheduler.countStop();
    }

    private static void menu() {
        // Press Enter run()
        // Press Left runTest()
        // Press Right correctArm
        scheduler.setOperationMode("Select Mode Button");
        LCD.clear(5);
        LCD.drawString("E:run L:Test R:Correct", 1, 5);
        LCD.refresh();
        while (true) {
            switch (Button.getButtons()) {
                case Button.ID_LEFT:
                    runTest();
                    break;
                case Button.ID_RIGHT:
                    correctArm();
                case Button.ID_ENTER:
                    run();
                    break;
            }
        }
    }

    private static void runTest() {
        // 開始確認
        scheduler.setOperationMode("Press Enter to Start");
        Button.ENTER.waitForPress();
    }

    private static void correctArm() {
        //アームが開いている場合の内部データの修正
        arm.setState(true);
        arm.run("Close");
    }

    private static void run() {
        // 開始確認
        scheduler.setOperationMode("Press Enter to Start");
        Button.ENTER.waitForPress();
        //アームを開ける
        arm.run("Open");
        //ボトルを取得する
        grabBottle.setAngle(70);
        grabBottle.run();
        //速度(100)角度(-90度°)で回転
        turn.setSpeed(300);
        turn.setAngle(-90);
        turn.run();
        //速度(600)カラー(赤)で後進
        backwardColor.setSpeed(600);
        backwardColor.setColorId(0);
        backwardColor.run();
        //速度(300)走行距離(10cm)で後進
        backward.setSpeed(300);
        backward.setDistance(10);
        backward.run();
        //アームを開ける
        arm.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        // 2個目
        //速度(100)角度(90°)で回転
        turn.setAngle(90);
        turn.run();
        //速度(400)カラー(白)で前進
        forwardColor.setSpeed(400);
        forwardColor.setColorId(6);
        forwardColor.run();
        //ボトルを取得する
        grabBottle.run();
        //速度(600)カラー(赤)で後進
        backwardColor.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        //アームを開ける
        arm.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        // 3個目
        //速度(100)角度(90°)で回転
        turn.setAngle(90);
        turn.run();
        //速度(400)カラー(白)で前進
        forwardColor.run();
        //速度(400)走行距離(235m)で前進
        forward.setSpeed(400);
        forward.setDistance(25);
        forward.run();
        //ボトルを取得する
        grabBottle.run();
        //速度(600)カラー(赤)で後進
        backwardColor.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        //アームを開ける
        arm.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        // 4個目
        //速度(100)角度(90°)で回転
        turn.run();
        //速度(400)カラー(白)で前進
        forwardColor.run();
        //ボトルを取得する
        grabBottle.run();
        //速度(600)カラー(赤)で後進
        backwardColor.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        //アームを開ける
        arm.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        // 帰り
        //速度(100)角度(20°)で回転
        turn.setAngle(20);
        turn.run();
        //スピード(800)走行距離(100cm)で前進
        forward.setSpeed(800);
        forward.setDistance(100);
        forward.run();
        //速度(200)カラー(黒)で前進
        forwardColor.setSpeed(200);
        forwardColor.setColorId(7);
        forwardColor.run();
        //速度(100)角度(60°)で回転
        turn.setAngle(80);
        turn.run();
        //速度(200)カラー(黄)で前進
        forwardColor.setSpeed(200);
        forwardColor.setColorId(3);
        forwardColor.run();
        //アームを閉じる
        arm.run();
    }
}