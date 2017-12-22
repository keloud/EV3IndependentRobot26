package info.keloud.leJOS;

import info.keloud.leJOS.manager.Scheduler;
import info.keloud.leJOS.motor.CenterMotor;
import info.keloud.leJOS.motor.LeftMotor;
import info.keloud.leJOS.motor.RightMotor;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import info.keloud.leJOS.utils.*;
import info.keloud.leJOS.utils.advanced.GrabBottle3;
import info.keloud.leJOS.utils.advanced.SearchGyro;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class leJOS {
    // スレッド スケジューラー
    private static Scheduler scheduler;
    // ユーティリティ
    private static Arm arm;
    private static Forward forward;
    private static ForwardWithColor forwardWithColor;
    private static ForwardWithSonar forwardWithSonar;
    private static Backward backward;
    private static BackwardWithColor backwardWithColor;
    private static TurnWithGyro2 turn;
    private static GrabBottle3 grabBottle;
    private static SearchGyro searchGyro;
    // センサー
    private static GyroSensor gyroSensor;
    // 最大速度
    private static float maxSpeed;

    public static void main(String[] args) {
        // ディスプレイ案内開始
        LCD.clear();
        LCD.drawString("Init ColorSensor", 1, 5);
        LCD.refresh();
        // カラーセンサーの初期化
        ColorSensor colorSensor = new ColorSensor();
        // ディスプレイ案内更新
        LCD.clear(5);
        LCD.drawString("Init UltrasonicSensor", 1, 5);
        LCD.refresh();
        // 超音波センサーの初期化
        UltrasonicSensor ultrasonicSensor = new UltrasonicSensor();
        // ディスプレイ案内の更新
        LCD.clear(5);
        LCD.drawString("Init GyroSensor", 1, 5);
        LCD.refresh();
        // ジャイロセンサーの初期化
        gyroSensor = new GyroSensor();
        // ディスプレイ案内の更新
        LCD.clear(5);
        LCD.drawString("Init Motor", 1, 5);
        LCD.refresh();
        // モーターの初期化
        CenterMotor centerMotor = new CenterMotor();
        LeftMotor leftMotor = new LeftMotor();
        RightMotor rightMotor = new RightMotor();
        maxSpeed = leftMotor.getMaxSpeed();
        // ディスプレイ案内の更新
        LCD.clear(5);
        LCD.drawString("Init Thread", 1, 5);
        LCD.refresh();
        // スレッドオブジェクトの作成
        scheduler = new Scheduler(centerMotor, leftMotor, rightMotor, ultrasonicSensor, colorSensor, gyroSensor);
        setOperationMode("Initializing");
        // ユーティリティ
        arm = new Arm(centerMotor);
        forward = new Forward(leftMotor, rightMotor);
        forwardWithColor = new ForwardWithColor(leftMotor, rightMotor, colorSensor);
        forwardWithSonar = new ForwardWithSonar(leftMotor, rightMotor, ultrasonicSensor);
        backward = new Backward(leftMotor, rightMotor);
        backwardWithColor = new BackwardWithColor(leftMotor, rightMotor, colorSensor);
        turn = new TurnWithGyro2(leftMotor, rightMotor, gyroSensor);
        searchGyro = new SearchGyro(leftMotor, rightMotor, ultrasonicSensor, gyroSensor, turn);
        grabBottle = new GrabBottle3(centerMotor, leftMotor, rightMotor, ultrasonicSensor, colorSensor, arm, searchGyro);
        // ディスプレイ案内の更新
        LCD.clear(5);
        LCD.drawString("End of initialization processing", 1, 5);
        LCD.refresh();
        // メニューを開く
        menu();
        // 終了処理
        setOperationMode("All Complete");
        // Enterキーを押して次に進む
        Button.ENTER.waitForPress();
        scheduler.countStop();
    }

    private static void menu() {
        // Press Enter run()
        // Press Left runTest()
        // Press Right correctArm()
        setOperationMode("Waiting for operation");
        LCD.clear(5);
        LCD.drawString("E:run L:Test R:Correct", 1, 5);
        LCD.refresh();
        switch (Button.waitForAnyPress()) {
            case Button.ID_LEFT:
                runTest();
                menu();
                break;
            case Button.ID_RIGHT:
                correctArm();
                menu();
                break;
            case Button.ID_DOWN:
                initGyro();
                menu();
                break;
            case Button.ID_ENTER:
                run();
                break;
            default:
                break;
        }
    }

    public static void stop() {
        System.exit(0);
    }

    private static void run() {
        initGyro();
        // 開始確認
        setOperationMode("Waiting for operation");
        LCD.clear(5);
        LCD.drawString("Press Enter to Start", 1, 5);
        LCD.refresh();
        Button.ENTER.waitForPress();
        scheduler.start();
        LCD.clear(5);
        LCD.drawString("EV3 running", 1, 5);
        LCD.refresh();
        //アームを開ける
        arm.run(true);
        //速度(800)で手前距離(7cm)で前進
        forwardWithSonar.run(800, 7);
        //速度(100)で走行距離(10cm)で前進
        forward.run(100, 8);
        //アームを閉じる
        arm.run("CLOSE");
        //ボトルを取得する
        //grabBottle.run(60, false);
        //速度(100)角度(-90度°)で回転
        turn.run(120, -90);
        //速度(600)カラー(赤)で後進
        backwardWithColor.run(700, 0);
        //速度(300)走行距離(10cm)で後進
        backward.run(300, 10);
        //アームを開ける
        arm.run(true);
        //速度(300)走行距離(10cm)で後進
        backward.run(300, 10);
        // 2個目
        //速度(100)角度(90°)で回転
        turn.run(120, 90);
        //速度(400)カラー(白)で前進
        forwardWithColor.run(700, 6);
        //速度(400)距離(10cm)で前進
        forward.run(400, 20);
        //ボトルを取得する
        grabBottle.run(60, false);
        //速度(600)カラー(赤)で後進
        backwardWithColor.run(700, 0);
        //速度(300)走行距離(10cm)で後進
        backward.run(300, 10);
        //アームを開ける
        arm.run(true);
        //速度(300)走行距離(10cm)で後進
        backward.run(300, 10);
        // 3個目
        //速度(100)角度(90°)で回転
        turn.run(120, 90);
        //速度(400)カラー(白)で前進
        forwardWithColor.run(600, 6);
        //速度(400)走行距離(30cm)で前進
        forward.run(600, 40);
        //ボトルを取得する
        grabBottle.run(60, false);
        //速度(600)カラー(赤)で後進
        backwardWithColor.run(700, 0);
        //速度(300)走行距離(10cm)で後進
        backward.run(300, 10);
        //アームを開ける
        arm.run(true);
        //速度(300)走行距離(10cm)で後進
        backward.run(300, 10);
        // 4個目
        //速度(100)角度(90°)で回転
        turn.run(120, 90);
        //速度(400)カラー(白)で前進
        forwardWithColor.run(600, 6);
        //速度(400)距離(10cm)で前進
        forward.run(400, 20);
        //ボトルを取得する
        grabBottle.run(60, false);
        /*
        //速度(600)カラー(赤)で後進
        backwardWithColor.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        //アームを開ける
        arm.run();
        //速度(300)走行距離(10cm)で後進
        backward.run();
        */
        // 帰り
        //速度(100)角度(-90°)で回転
        turn.run(120, -90);
        //速度(600)カラー(黒)で後進
        backwardWithColor.run(700, 7);
        //速度(100)角度(180°)で回転
        turn.run(120, 180);
        //アームを開く
        arm.run("OPEN");
        //速度(100)距離(15cm)で後進
        backward.run(100, 15);
        /*
        //速度(100)角度(20°)で回転
        turn.setAngle(20);
        turn.run();
        //スピード(700)走行距離(100cm)で前進
        forward.setSpeed(700);
        forward.setDistance(100);
        forward.run();
        //速度(200)カラー(黒)で前進
        forwardWithColor.setSpeed(200);
        forwardWithColor.setColorId(7);
        forwardWithColor.run();
        //速度(100)角度(60°)で回転
        turn.setAngle(80);
        turn.run();
        //速度(200)カラー(黄)で前進
        forwardWithColor.setSpeed(200);
        forwardWithColor.setColorId(3);
        forwardWithColor.run();
        //アームを閉じる
        arm.run();
        */
    }

    private static void runTest() {
        initGyro();
        // 開始確認
        setOperationMode("Waiting for operation");
        LCD.clear(5);
        LCD.drawString("Press Enter to Start", 1, 5);
        LCD.refresh();
        Button.ENTER.waitForPress();
        scheduler.start();
        LCD.clear(5);
        LCD.drawString("EV3 running", 1, 5);
        LCD.refresh();

        //回転テスト
        Button.ENTER.waitForPress();
        turn.run(800, -720);
        Button.ENTER.waitForPress();
        turn.run(800, 720);
    }

    private static void correctArm() {
        setOperationMode("Adjusting arm");
        //アームが開いている場合の内部データの修正
        arm.setState(true);
        arm.run(false);
    }

    public static void setOperationMode(String operationMode) {
        scheduler.setOperationMode(operationMode);
    }

    private static void initGyro() {
        setOperationMode("Initialize Gyro");
        gyroSensor.initGyro();
    }
}