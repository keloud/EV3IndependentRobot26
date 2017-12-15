package info.keloud.leJOS.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class GyroSensor {
    private float[] gyroFloat;
    private SampleProvider gyroProvider;

    public GyroSensor() {
        EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
        gyroProvider = gyro.getAngleMode();
        gyroFloat = new float[gyroProvider.sampleSize()];
    }

    private void updateValue() {
        gyroProvider.fetchSample(gyroFloat, 0);
    }

    public float getValue() {
        updateValue();
        return gyroFloat[0];
    }
}