import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;

public class Menu {
    private String[] menuData = {"move All", "move Arm Only"};

    int select() {
        int buttonId;
        int selectId = 0;
        boolean end_flg = false;
        LCD.clear();
        LCD.drawString("App_Select", 1, 2);
        LCD.drawString("> 1." + menuData[0], 1, 3);
        LCD.drawString("  2." + menuData[1], 1, 4);
        while (end_flg == false) {
            buttonId = Button.getButtons();
            if (buttonId == 1) {
                selectId = 0;
                LCD.clear(3);
                LCD.clear(4);
                LCD.drawString("> 1." + menuData[0], 1, 3);
                LCD.drawString("  2." + menuData[1], 1, 4);
            }
            if (buttonId == 4) {
                selectId = 1;
                LCD.clear(3);
                LCD.clear(4);
                LCD.drawString("> 1." + menuData[0], 1, 3);
                LCD.drawString("  2." + menuData[1], 1, 4);
            }
            if (buttonId == 2) {
                end_flg = true;
            }
        }
        LCD.drawString("Start?", 1, 2);
        Button.ENTER.waitForPress();
        return selectId;
    }
}
