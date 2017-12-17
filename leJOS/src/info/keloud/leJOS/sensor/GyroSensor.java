package info.keloud.leJOS.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class GyroSensor {
    private float[] gyroValue;
    private SampleProvider gyroProvider;

    public GyroSensor() {
        EV3GyroSensor gyro = new EV3GyroSensor(SensorPort.S3);
        gyroProvider = gyro.getAngleMode();
        gyroValue = new float[gyroProvider.sampleSize()];
    }

    private void updateValue() {
        gyroProvider.fetchSample(gyroValue, 0);
    }

    public float getValue() {
        updateValue();
        return gyroValue[0];
    }
}