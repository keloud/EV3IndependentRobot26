package info.keloud.leJOS.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class ColorSensor {
    public float[] colorFloat;
    private SensorMode colorProvider;

    public void initialize() {
        EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
        colorProvider = color.getColorIDMode();
        colorFloat = new float[colorProvider.sampleSize()];
    }

    public void update() {
        colorProvider.fetchSample(colorFloat, 0);
    }
}
