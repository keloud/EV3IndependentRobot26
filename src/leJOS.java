import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

class leJOS {
    /* モーター*/
    RegulatedMotor motorCenter;
    RegulatedMotor motorLeft;
    RegulatedMotor motorRight;
    /* 累積モーター角度*/
    int accumulationCenter, accumulationLeft, accumulationRight;
    float[] colorFloat;
    float[] ultrasonicFloat;
    float[] gyroFloat;
    private SensorMode colorID;
    private SampleProvider ultrasonicProvider;
    private SampleProvider gyroProvider;

    leJOS() {
        /* 初期化処理*/
        // ディスプレイ案内開始
        LCD.clear();
        LCD.drawString("Initializing", 1, 6);
        LCD.refresh();
        // カラーセンサーの初期化
        EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
        colorID = color.getColorIDMode();
        colorFloat = new float[colorID.sampleSize()];
        // ディスプレイ案内更新
        LCD.clear();
        LCD.drawString("Initializing.", 1, 6);
        LCD.refresh();
        // 超音波センサーの初期化
        EV3UltrasonicSensor ultrasonic = new EV3UltrasonicSensor(SensorPort.S2);
        ultrasonicProvider = ultrasonic.getDistanceMode();
        ultrasonicFloat = new float[ultrasonicProvider.sampleSize()];
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Initializing..", 1, 6);
        LCD.refresh();
        // ジャイロセンサーの初期化
        EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
        gyroProvider = gyro.getAngleMode();
        gyroFloat = new float[gyroProvider.sampleSize()];
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
        // スレッド起動
        Scheduler scheduler = new Scheduler(this);
        scheduler.start();
        //初期値取得
        for (int i = 0; i < 10000; i++) {
            sensorUpdate();
        }
        // ディスプレイ案内の更新
        LCD.clear();
        LCD.drawString("Initializing.....", 1, 6);
        LCD.refresh();
    /* オブジェクト化*/
        //Menu menu = new Menu();
        Move move = new Move(this);
        Search search = new Search(this);
        /* 開始確認*/
        LCD.clear(6);
        LCD.drawString("Press Enter", 1, 6);
        LCD.refresh();
        Button.ENTER.waitForPress();
        /* メイン処理*/
        LCD.clear(6);
        LCD.drawString("Running", 1, 6);
        LCD.refresh();
        //初期位置からペットボトルを検索する
        move.forwardDefault(800, 30);

        /* 終了処理*/
        LCD.clear(6);
        LCD.drawString("All Complete", 1, 6);
        LCD.refresh();
        Button.ENTER.waitForPress();
        scheduler.countStop();
    }

    void sensorUpdate() {
        colorID.fetchSample(colorFloat, 0);
        ultrasonicProvider.fetchSample(ultrasonicFloat, 0);
        gyroProvider.fetchSample(gyroFloat, 0);
        LCD.clear(0);
        LCD.drawString(String.valueOf((float) ((int) (Battery.getVoltage() * 10 + 0.5) / 10.0)), 15, 0);
        LCD.clear(1);
        LCD.drawString("C:" + accumulationCenter + " L:" + accumulationLeft + " R:" + accumulationRight, 1, 1);
        LCD.clear(2);
        LCD.drawString("ColorN:" + colorFloat[0], 1, 2);
        LCD.clear(3);
        LCD.drawString("Sonic:" + ultrasonicFloat[0], 1, 3);
        LCD.clear(4);
        LCD.drawString("Gyro:" + gyroFloat[0] + " ℃", 1, 4);
        LCD.refresh();
    }
}
