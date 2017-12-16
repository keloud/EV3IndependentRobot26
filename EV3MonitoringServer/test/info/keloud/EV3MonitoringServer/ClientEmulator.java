package info.keloud.EV3MonitoringServer;

public class ClientEmulator {
    public static void main(String[] args) {
        new ServerMain();
        ServerMain.main(null);
        new RandomSender();
    }
}
