import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MainPanel extends JPanel implements ActionListener {
    String bufferedString;
    String accumulationMotorCenterString;

    MainPanel() {
        System.out.println("MainPanel.MainPanel");

        //セットレイアウトパネル
        setLayout(new GridLayout(6, 2));

        // アームモーター累計角度
        JLabel accumulationMotorCenterLabel = new JLabel("アームモーター累計角度");
        add(accumulationMotorCenterLabel);
        JTextField accumulationMotorCenterTextField = new JTextField();
        add(accumulationMotorCenterTextField);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("MainPanel.actionPerformed");
        Object object = e.getSource();
    }

    void refreshTextField(String bufferedString) {
        System.out.println("MainPanel.refresh");
        this.bufferedString = bufferedString;
        repaint();
    }
}
