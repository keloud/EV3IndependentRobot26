package info.keloud.leJOS.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class UltrasonicSensor {
    public float[] ultrasonicFloat;
    private SampleProvider ultrasonicProvider;

    public void initialize() {
        EV3UltrasonicSensor ultrasonic = new EV3UltrasonicSensor(SensorPort.S2);
        ultrasonicProvider = ultrasonic.getDistanceMode();
        ultrasonicFloat = new float[ultrasonicProvider.sampleSize()];
    }

    public void update() {
        ultrasonicProvider.fetchSample(ultrasonicFloat, 0);
    }
}
