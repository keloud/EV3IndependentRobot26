import javax.swing.*;

class MainFrame extends JFrame {
    MainPanel mainPanel;

    MainFrame() {
        System.out.println("MainFrame.MainFrame");

        this.setSize(1024, 768);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("EV3 information Server");
        setVisible(true);
        mainPanel = new MainPanel();
        add(mainPanel);
    }
}