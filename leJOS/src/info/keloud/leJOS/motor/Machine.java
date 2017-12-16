package info.keloud.leJOS.motor;

// マシンの基本情報
// Vehicle information
interface Machine {
    // タイヤの直径
    // Diameter of tire(cm)
    float diameter = 5.6F;
    // ホイールの根元から反対のホイールの根元までの距離
    // Width of wheel
    float width = 9.2F;
    // Thread wait time
    int wait = 10;
}
