package view.widget;

import model.Model;
import view.BorderFrame;
import view.MyColors;

public class HelpViewTopText extends TopText {

    @Override
    protected void drawKeyTexts(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "F3=SEARCH ESC=EXIT", 62, 0, MyColors.WHITE);
    }
}
