package info.keloud.leJOS;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class ColorSensor {
    float[] colorFloat;
    private SensorMode colorID;

    void initialize(){
        EV3ColorSensor color = new EV3ColorSensor(SensorPort.S1);
        colorID = color.getColorIDMode();
        colorFloat = new float[colorID.sampleSize()];
    }

    void update(){
        colorID.fetchSample(colorFloat, 0);
    }
}
