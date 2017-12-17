package info.keloud.EV3MonitoringServer;

import javax.swing.*;
import java.awt.*;

class MainPanel extends JPanel {
    private JTextField acquiredValueTextField, operationModeTextField, accumulationMotorLeftTextField, accumulationMotorRightTextField, accumulationMotorCenterTextField, colorIntTextField, colorStringTextField, ultrasonicTextField, gyroTextField;


    MainPanel() {
        // System.out.println("info.keloud.EV3MonitoringServer.MainPanel.info.keloud.EV3MonitoringServer.MainPanel");

        //セットレイアウトパネル
        setLayout(new BorderLayout());

        //ステータスパネル
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(7, 2));

        // 状態表示
        JLabel informationLabel = new JLabel("現在の状態");
        statusPanel.add(informationLabel);
        operationModeTextField = new JTextField("Data is not received.");
        statusPanel.add(operationModeTextField);

        // 左モーター累積角度
        JLabel accumulationMotorLeftLabel = new JLabel("左モーター累計角度");
        statusPanel.add(accumulationMotorLeftLabel);
        accumulationMotorLeftTextField = new JTextField("Data is not received.");
        statusPanel.add(accumulationMotorLeftTextField);

        // 右モーター累積角度
        JLabel accumulationMotorRightLabel = new JLabel("右モーター累計角度");
        statusPanel.add(accumulationMotorRightLabel);
        accumulationMotorRightTextField = new JTextField("Data is not received.");
        statusPanel.add(accumulationMotorRightTextField);

        // アームモーター累計角度
        JLabel accumulationMotorCenterLabel = new JLabel("アームモーター累計角度");
        statusPanel.add(accumulationMotorCenterLabel);
        accumulationMotorCenterTextField = new JTextField("Data is not received.");
        statusPanel.add(accumulationMotorCenterTextField);

        // カラーID
        JLabel colorLabel = new JLabel("カラーID / カラー名");
        statusPanel.add(colorLabel);

        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new GridLayout(1, 2));

        colorIntTextField = new JTextField("Data is not received.");
        colorPanel.add(colorIntTextField);
        colorStringTextField = new JTextField("Data is not received.");
        colorPanel.add(colorStringTextField);

        statusPanel.add(colorPanel);

        // 超音波センサー距離
        JLabel ultrasonicLabel = new JLabel("超音波センサー距離");
        statusPanel.add(ultrasonicLabel);
        ultrasonicTextField = new JTextField("Data is not received.");
        statusPanel.add(ultrasonicTextField);

        // ジャイロセンサー角度
        JLabel gyroLabel = new JLabel("ジャイロセンサー角度");
        statusPanel.add(gyroLabel);
        gyroTextField = new JTextField("Data is not received.");
        statusPanel.add(gyroTextField);

        add("Center", statusPanel);

        // 上部表示パネル
        JPanel acquiredValuePanel = new JPanel();
        acquiredValuePanel.setLayout(new GridLayout(1, 2));

        // 取得した値
        JLabel acquiredValueLabel = new JLabel("Row Data");
        acquiredValuePanel.add(acquiredValueLabel);
        acquiredValueTextField = new JTextField("Data is not received.");
        acquiredValuePanel.add(acquiredValueTextField);

        add("North", acquiredValuePanel);
    }

    void refreshTextField(String bufferedString) {
        // System.out.println("MainPanel.refreshTextField");
        try {
            acquiredValueTextField.setText(bufferedString);
            String[] bufferedStrings = bufferedString.split(",", 7);
            // System.out.println(bufferedStrings[0] + " " + bufferedStrings[1] + " " + bufferedStrings[2] + " " + bufferedStrings[3] + " " + bufferedStrings[4] + " " + bufferedStrings[5] + " " + bufferedStrings[6] + " ");
            if (bufferedStrings[0] != null) {
                operationModeTextField.setText(bufferedStrings[0]);
            } else {
                operationModeTextField.setText("NullPointerException");
            }
            if (bufferedStrings[1] != null) {
                accumulationMotorLeftTextField.setText(bufferedStrings[1]);
            } else {
                accumulationMotorRightTextField.setText("NullPointerException");
            }
            if (bufferedStrings[2] != null) {
                accumulationMotorRightTextField.setText(bufferedStrings[2]);
            } else {
                accumulationMotorRightTextField.setText("NullPointerException");
            }
            if (bufferedStrings[3] != null) {
                accumulationMotorCenterTextField.setText(bufferedStrings[3]);
            } else {
                accumulationMotorCenterTextField.setText("NullPointerException");
            }
            if (bufferedStrings[4] != null) {
                colorIntTextField.setText(bufferedStrings[4]);
                try {
                    colorInt2String(Integer.parseInt(bufferedStrings[4].charAt(8) + ""));
                } catch (NumberFormatException ne) {
                    colorStringTextField.setText("Could not convert value.");
                }
            } else {
                colorIntTextField.setText("NullPointerException");
            }
            if (bufferedStrings[5] != null) {
                ultrasonicTextField.setText(bufferedStrings[5]);
            } else {
                ultrasonicTextField.setText("NullPointerException");
            }
            if (bufferedStrings[6] != null) {
                gyroTextField.setText(bufferedStrings[6]);
            } else {
                gyroTextField.setText("NullPointerException");
            }
        } catch (ArrayIndexOutOfBoundsException ae) {
            // System.out.println("ArrayIndexOutOfBoundsException");
            operationModeTextField.setText("The number of symbols of the received value is different.");
            accumulationMotorLeftTextField.setText("");
            accumulationMotorRightTextField.setText("");
            accumulationMotorCenterTextField.setText("");
            colorIntTextField.setText("");
            colorStringTextField.setText("");
            ultrasonicTextField.setText("");
            gyroTextField.setText("");
        }
        repaint();
    }

    private void colorInt2String(int colorInt) {
        switch (colorInt) {
            case 1:
                colorStringTextField.setText("BLACK");
                break;
            case 2:
                colorStringTextField.setText("BLUE");
                break;
            case 3:
                colorStringTextField.setText("GREEN");
                break;
            case 4:
                colorStringTextField.setText("YELLOW");
                break;
            case 5:
                colorStringTextField.setText("RED");
                break;
            case 6:
                colorStringTextField.setText("WHITE");
                break;
            case 7:
                colorStringTextField.setText("BROWN");
                break;
            default:
                colorStringTextField.setText("NONE");
        }
    }
}
