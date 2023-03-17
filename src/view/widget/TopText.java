package view.widget;

import model.Model;
import view.BorderFrame;
import view.MyColors;
import view.sprites.CharSprite;

public class TopText {
    private static final CharSprite FOOD_ICON_SPRITE = CharSprite.make(2, MyColors.PEACH, MyColors.WHITE, MyColors.BLACK);
    private static final CharSprite GOLD_ICON_SPRITE = CharSprite.make(0, MyColors.TAN, MyColors.LIGHT_YELLOW, MyColors.BLACK);
    private static final CharSprite REP_ICON_SPRITE = CharSprite.make(3, MyColors.LIGHT_GRAY, MyColors.CYAN, MyColors.BLACK);

    public void drawYourself(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), String.format("DAY %d", model.getDay()), 0, 0, MyColors.CYAN);
        model.getScreenHandler().put(11, 0, GOLD_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%4d", model.getParty().getGold()), 7, 0, MyColors.LIGHT_YELLOW);
        model.getScreenHandler().put(16, 0, FOOD_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%3d", model.getParty().getFood()), 13, 0, MyColors.PEACH);
        model.getScreenHandler().put(20, 0, REP_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", model.getParty().getReputation()), 18, 0, MyColors.WHITE);
        drawKeyTexts(model);
    }

    protected void drawKeyTexts(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "ESC=MENU \\=LOG F1=HELP", 58, 0, MyColors.WHITE);
    }
}
