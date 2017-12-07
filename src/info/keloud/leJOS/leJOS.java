package info.keloud.leJOS;

import info.keloud.leJOS.motor.Advanced.CatchBottle;
import info.keloud.leJOS.motor.*;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;

class leJOS {
    // Cumulative rotation of the motor
    int accumulationMotorCenter, accumulationMotorLeft, accumulationMotorRight;
    // RegulatedMotor
    RegulatedMotor motorCenter;
    RegulatedMotor motorLeft;
    RegulatedMotor motorRight;
    // ColorSensor
    private ColorSensor colorSensor;
    // UltrasonicSensor
    private UltrasonicSensor ultrasonicSensor;
    // GyroSensor
    private GyroSensor gyroSensor;

    leJOS() {
        /* 初期化処理*/
        // ディスプレイ案内開始
        LCD.clear();
        LCD.drawString("Init ColorSensor", 1, 6);
        LCD.refresh();
        // カラーセンサーの初期化
        colorSensor = new ColorSensor();
        // ディスプレイ案内更新
        LCD.clear();
        LCD.drawString("Init UltrasonicSensor", 1, 6);
        LCD.refresh();
        // 超音波センサーの初期化
        ultrasonicSensor = new UltrasonicSensor();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Init GyroSensor", 1, 6);
        LCD.refresh();
        // ジャイロセンサーの初期化
        gyroSensor = new GyroSensor();
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Init Motor", 1, 6);
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
        LCD.drawString("Init Thread", 1, 6);
        LCD.refresh();
        // センサーの初期化を促す
        for (int i = 0; i < 300; i++) {
            sensorUpdate();
        }
        // スレッド起動
        Scheduler scheduler = new Scheduler(this);
        scheduler.start();
         /* オブジェクト化*/
        Forward forward = new Forward(motorLeft, motorRight);
        ForwardColor forwardColor = new ForwardColor(motorLeft, motorRight, colorSensor);
        Backward backward = new Backward(motorLeft, motorRight);
        BackwardColor backwardColor = new BackwardColor(motorLeft, motorRight, colorSensor);
        Turn turn = new Turn(motorLeft, motorRight);
        Arm arm = new Arm(motorCenter);
        CatchBottle catchBottle = new CatchBottle(motorLeft, motorRight, motorCenter, ultrasonicSensor, colorSensor, arm, forward);
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("End of initialization processing", 1, 6);
        LCD.refresh();
        /* 開始確認*/
        LCD.clear(6);
        LCD.drawString("Press Enter", 1, 6);
        LCD.refresh();
        Button.ENTER.waitForPress();
        /* メイン処理*/
        LCD.clear(6);
        LCD.drawString("Running", 1, 6);
        LCD.refresh();
        //修正
        arm.setState(true);
        arm.run("Close");
        //アームを開ける
        arm.run("Open");
        //ボトルを取得する
        catchBottle.setAngle(60);
        catchBottle.run();
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
        catchBottle.run();
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
        //ボトルを取得する
        catchBottle.run();
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
        catchBottle.run();
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
        turn.setAngle(60);
        turn.run();
        //速度(200)カラー(黄)で前進
        forwardColor.setSpeed(200);
        forwardColor.setColorId(2);
        forwardColor.run();
        //アームを閉じる
        arm.run();

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
        LCD.drawString("ColorId:" + colorSensor.colorFloat[0], 1, 2);
        LCD.clear(3);
        LCD.drawString("USonic:" + ultrasonicSensor.ultrasonicFloat[0], 1, 3);
        LCD.clear(4);
        LCD.drawString("Gyro:" + gyroSensor.gyroFloat[0] + " ℃", 1, 4);
        LCD.refresh();
    }
}
