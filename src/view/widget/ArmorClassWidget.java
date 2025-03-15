package view.widget;

import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;

public class ArmorClassWidget {


    public static void drawYourself(ScreenHandler screenHandler, int x, int y, boolean isHeavy) {
        printLabel(screenHandler, x, y);
        printArmorClass(screenHandler, x+12, y, isHeavy);
    }

    private static void printLabel(ScreenHandler screenHandler, int x, int y) {
        BorderFrame.drawString(screenHandler, "Armor Class ", x, y, MyColors.WHITE, MyColors.BLUE);
    }

    private static void printArmorClass(ScreenHandler screenHandler, int x, int y, boolean canUseHeavyArmor) {
        BorderFrame.drawString(screenHandler, canUseHeavyArmor ? "HEAVY" : "LIGHT",
                x, y, MyColors.WHITE, canUseHeavyArmor ? MyColors.BLACK : MyColors.BLUE);
    }
}
