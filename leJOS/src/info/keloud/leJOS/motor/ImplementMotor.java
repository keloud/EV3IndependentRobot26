package info.keloud.leJOS.motor;

interface ImplementMotor {
    void run();

    String getOperationMode();

    void setSpeed(int speed);

    void setDistance(double distance);

    void setAngle(double angle);

    void setColorId(int colorId);
}
