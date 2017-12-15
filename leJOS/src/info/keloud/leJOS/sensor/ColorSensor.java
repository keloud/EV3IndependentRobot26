package info.keloud.leJOS.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class ColorSensor {
    private float[] colorFloat;
    private SensorMode colorProvider;

    public ColorSensor() {
        EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
        colorProvider = color.getColorIDMode();
        colorFloat = new float[colorProvider.sampleSize()];
    }

    private void updateValue() {
        colorProvider.fetchSample(colorFloat, 0);
    }

    public float getValue() {
        updateValue();
        return colorFloat[0];
    }
}
