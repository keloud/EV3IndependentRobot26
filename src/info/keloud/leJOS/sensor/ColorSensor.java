package info.keloud.leJOS.sensor;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class ColorSensor {
    private float[] colorValue;
    private SensorMode colorProvider;

    public ColorSensor() {
        EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
        colorProvider = color.getColorIDMode();
        colorValue = new float[colorProvider.sampleSize()];
    }

    private void updateValue() {
        colorProvider.fetchSample(colorValue, 0);
    }

    // NONE, BLACK, BLUE, GREEN, YELLOW, RED, WHITE, BROWN
    public float getValue() {
        updateValue();
        return colorValue[0];
    }
}
