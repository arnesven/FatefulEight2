package view.widget;

import model.Model;
import view.BorderFrame;
import view.MyColors;

public class FullMapTopText extends TopText {

    @Override
    protected void drawKeyTexts(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "F3=info ESC=exit", 64, 0, MyColors.WHITE);
    }
}
