package info.keloud.leJOS;

import info.keloud.leJOS.InformationHandler.Monitoring;
import info.keloud.leJOS.motor.Advanced.CatchBottle;
import info.keloud.leJOS.motor.*;
import info.keloud.leJOS.sensor.ColorSensor;
import info.keloud.leJOS.sensor.GyroSensor;
import info.keloud.leJOS.sensor.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;

public class Motor {
    // Parent
    private leJOS leJOS;
    // Behavior mode information
    private Monitoring monitoring;

    //ソフトウェア部
    // 基本部
    private Forward forward;
    private ForwardColor forwardColor;
    private ForwardSonar forwardSonar;
    private Backward backward;
    private BackwardColor backwardColor;
    private Turn turn;
    private TurnGyro turnGyro;
    private Arm arm;
    // 応用部
    private CatchBottle catchBottle;

    Motor(leJOS parent) {
        // 初期化
        this.leJOS = parent;
        this.monitoring = parent.monitoring;
        RegulatedMotor motorCenter = leJOS.motorCenter;
        RegulatedMotor motorLeft = leJOS.motorLeft;
        RegulatedMotor motorRight = leJOS.motorRight;
        ColorSensor colorSensor = leJOS.colorSensor;
        UltrasonicSensor ultrasonicSensor = leJOS.ultrasonicSensor;
        GyroSensor gyroSensor = leJOS.gyroSensor;

        // 基本部
        forward = new Forward(monitoring, motorLeft, motorRight);
        forwardColor = new ForwardColor(monitoring, motorLeft, motorRight, colorSensor);
        forwardSonar = new ForwardSonar(monitoring, motorLeft, motorLeft, ultrasonicSensor);
        backward = new Backward(monitoring, motorLeft, motorRight);
        backwardColor = new BackwardColor(monitoring, motorLeft, motorRight, colorSensor);
        turn = new Turn(monitoring, motorLeft, motorRight);
        turnGyro = new TurnGyro(monitoring, motorLeft, motorRight, gyroSensor);
        arm = new Arm(monitoring, motorCenter);

        // 応用部
        catchBottle = new CatchBottle(monitoring, motorLeft, motorRight, motorCenter, ultrasonicSensor, colorSensor, arm, forward);
    }

    void run() {
        //アームが開いている場合の内部データの修正
        arm.setState(true);
        arm.run("Close");
        //アームを開ける
        arm.run("Open");
        //ボトルを取得する
        catchBottle.setAngle(70);
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
        //速度(400)走行距離(235m)で前進
        forward.setSpeed(400);
        forward.setDistance(25);
        forward.run();
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
