package info.keloud.leJOS;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class GyroSensor {
    float[] gyroFloat;
    private SampleProvider gyroProvider;

    void initialize(){
        EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
        gyroProvider = gyro.getAngleMode();
        gyroFloat = new float[gyroProvider.sampleSize()];
    }

    void update(){
        gyroProvider.fetchSample(gyroFloat, 0);
    }
}
