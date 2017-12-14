import javax.swing.*;

class MainFrame extends JFrame {
    private MainPanel mainPanel;

    MainFrame() {
        System.out.println("MainFrame.MainFrame");

        this.setSize(1024, 768);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("EV3 information Server");
        setVisible(true);
        mainPanel = new MainPanel();
        add(mainPanel);
    }

    void updatePanel(String bufferedString) {
        //System.out.println("MainFrame.updatePanel");
        mainPanel.refreshTextField(bufferedString);
    }
}