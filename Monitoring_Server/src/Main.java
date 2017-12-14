public class Main {
    public static void main(String[] args) {
        System.out.println("Main.main");
        MainFrame mainFrame = new MainFrame();
        Monitoring monitoring = new Monitoring(mainFrame);
        monitoring.start();
    }
}
