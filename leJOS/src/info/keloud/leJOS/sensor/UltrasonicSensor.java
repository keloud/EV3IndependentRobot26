package info.keloud.leJOS.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class UltrasonicSensor {
    private float[] ultrasonicValue;
    private SampleProvider ultrasonicProvider;

    public UltrasonicSensor() {
        EV3UltrasonicSensor ultrasonic = new EV3UltrasonicSensor(SensorPort.S2);
        ultrasonicProvider = ultrasonic.getDistanceMode();
        ultrasonicValue = new float[ultrasonicProvider.sampleSize()];
    }

    private void updateValue() {
        ultrasonicProvider.fetchSample(ultrasonicValue, 0);
    }

    public float getValue() {
        updateValue();
        return ultrasonicValue[0];
    }
}
