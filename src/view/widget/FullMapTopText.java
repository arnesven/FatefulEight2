package view.widget;

import model.Model;
import view.BorderFrame;
import view.MyColors;

public class FullMapTopText extends TopText {

    @Override
    protected void drawKeyTexts(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "ESC=exit map view", 63, 0, MyColors.WHITE);
    }
}
