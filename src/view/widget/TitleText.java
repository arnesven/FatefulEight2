package view.widget;

import model.Model;
import view.BorderFrame;
import view.MyColors;

public class TitleText {

    private String text;

    public void drawYourself(Model model) {
        BorderFrame.drawCentered(model.getScreenHandler(), this.text, BorderFrame.TITLE_TEXT_HEIGHT, MyColors.WHITE);
    }

    public void setText(String text) {
        this.text = text;
    }
}
