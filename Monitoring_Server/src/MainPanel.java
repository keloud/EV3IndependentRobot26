import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MainPanel extends JPanel implements ActionListener {

    MainPanel() {
        System.out.println("MainPanel.MainPanel");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("MainPanel.actionPerformed");
        Object object = e.getSource();
    }
}
