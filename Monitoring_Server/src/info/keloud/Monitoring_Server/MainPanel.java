package info.keloud.Monitoring_Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MainPanel extends JPanel implements ActionListener {
    private JTextField informationTextField;
    private JTextField acquiredValueTextField;

    MainPanel() {
        // System.out.println("info.keloud.Monitoring_Server.MainPanel.info.keloud.Monitoring_Server.MainPanel");

        //セットレイアウトパネル
        setLayout(new BorderLayout());

        //ステータスパネル
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(7, 2));

        // 状態表示
        JLabel informationLabel = new JLabel("現在の状態");
        statusPanel.add(informationLabel);
        informationTextField = new JTextField();
        statusPanel.add(informationTextField);

        // アームモーター累計角度
        JLabel accumulationMotorCenterLabel = new JLabel("アームモーター累計角度");
        statusPanel.add(accumulationMotorCenterLabel);
        JTextField accumulationMotorCenterTextField = new JTextField();
        statusPanel.add(accumulationMotorCenterTextField);

        // 左モーター累積角度
        JLabel accumulationMotorLeftLabel = new JLabel("左モーター累計角度");
        statusPanel.add(accumulationMotorLeftLabel);
        JTextField accumulationMotorLeftTextField = new JTextField();
        statusPanel.add(accumulationMotorLeftTextField);

        // 右モーター累積角度
        JLabel accumulationMotorRightLabel = new JLabel("右モーター累計角度");
        statusPanel.add(accumulationMotorRightLabel);
        JTextField accumulationMotorRightTextField = new JTextField();
        statusPanel.add(accumulationMotorRightTextField);

        // カラーID
        JLabel colorLabel = new JLabel("カラーID / カラー名");
        statusPanel.add(colorLabel);

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 2));

        JTextField colorIntTextField = new JTextField();
        colorPanel.add(colorIntTextField);
        JTextField colorStringTextField = new JTextField();
        colorPanel.add(colorStringTextField);

        statusPanel.add(colorPanel);

        // 超音波センサー距離
        JLabel USonicLabel = new JLabel("超音波センサー距離");
        statusPanel.add(USonicLabel);
        JTextField USonicTextField = new JTextField();
        statusPanel.add(USonicTextField);

        // ジャイロセンサー角度
        JLabel gyroLabel = new JLabel("ジャイロセンサー角度");
        statusPanel.add(gyroLabel);
        JTextField gyroTextField = new JTextField();
        statusPanel.add(gyroTextField);

        add("Center", statusPanel);

        // 上部表示パネル
        JPanel acquiredValuePanel = new JPanel();
        acquiredValuePanel.setLayout(new GridLayout(1, 2));

        // 取得した値
        JLabel acquiredValueLabel = new JLabel("Row Data");
        acquiredValuePanel.add(acquiredValueLabel);
        acquiredValueTextField = new JTextField();
        acquiredValuePanel.add(acquiredValueTextField);

        add("North", acquiredValuePanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("info.keloud.Monitoring_Server.MainPanel.actionPerformed");
        Object object = e.getSource();
    }

    void refreshTextField(String bufferedString) {
        // System.out.println("info.keloud.Monitoring_Server.MainPanel.refresh");
        acquiredValueTextField.setText(bufferedString);
        repaint();
    }
}
