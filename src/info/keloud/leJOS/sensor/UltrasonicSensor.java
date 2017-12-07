package info.keloud.leJOS.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class UltrasonicSensor {
    private float[] ultrasonicFloat;
    private SampleProvider ultrasonicProvider;

    public UltrasonicSensor() {
        EV3UltrasonicSensor ultrasonic = new EV3UltrasonicSensor(SensorPort.S2);
        ultrasonicProvider = ultrasonic.getDistanceMode();
        ultrasonicFloat = new float[ultrasonicProvider.sampleSize()];
    }

    private void update() {
        ultrasonicProvider.fetchSample(ultrasonicFloat, 0);
    }

    public float getValue() {
        update();
        return ultrasonicFloat[0];
    }
}
