package info.keloud.leJOS.motor;

interface ImplementMotor {
    void run();

    void setSpeed(int speed);

    void setDistance(double distance);

    void setAngle(double angle);

    void setColorId(int colorId);

    String getOperationMode();
}
