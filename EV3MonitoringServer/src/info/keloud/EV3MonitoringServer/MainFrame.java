package info.keloud.EV3MonitoringServer;

import javax.swing.*;

class MainFrame extends JFrame {
    private info.keloud.EV3MonitoringServer.MainPanel mainPanel;

    MainFrame() {
        // System.out.println("info.keloud.EV3MonitoringServer.MainFrame.info.keloud.EV3MonitoringServer.MainFrame");

        this.setSize(1024, 768);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("EV3 Monitoring Server");
        mainPanel = new info.keloud.EV3MonitoringServer.MainPanel();
        add(mainPanel);
        setVisible(true);
    }

    void updatePanel(String bufferedString) {
        // System.out.println("info.keloud.EV3MonitoringServer.MainFrame.updatePanel");
        mainPanel.refreshTextField(bufferedString);
    }
}