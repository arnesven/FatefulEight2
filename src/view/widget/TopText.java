package view.widget;

import model.Model;
import model.states.DailyEventState;
import view.BorderFrame;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class TopText {
    private static final CharSprite FOOD_ICON_SPRITE = CharSprite.make(2, MyColors.PEACH, MyColors.WHITE, MyColors.BLACK);
    public static final CharSprite GOLD_ICON_SPRITE = CharSprite.make(0, MyColors.TAN, MyColors.LIGHT_YELLOW, MyColors.BLACK);
    public static final CharSprite REP_ICON_SPRITE = CharSprite.make(3, MyColors.LIGHT_GRAY, MyColors.CYAN, MyColors.BLACK);
    private static final CharSprite INGREDIENTS_ICON_SPRITE = CharSprite.make(0x12, MyColors.WHITE, MyColors.LIGHT_GREEN, MyColors.BLACK);
    private static final CharSprite MATERIALS_ICON_SPRITE = CharSprite.make(0x13, MyColors.LIGHT_GRAY, MyColors.WHITE, MyColors.BLACK);
    public static final CharSprite ALIGNMENT_ICON_SPRITE = makeAlignmentSprite();

    public void drawYourself(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), String.format("DAY %d", model.getDay()), 0, 0, MyColors.CYAN);
        model.getScreenHandler().put(11, 0, GOLD_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%4d", model.getParty().getGold()), 7, 0, MyColors.LIGHT_YELLOW);
        model.getScreenHandler().put(16, 0, FOOD_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%3d", model.getParty().getFood()), 13, 0, MyColors.PEACH);
        model.getScreenHandler().put(21, 0, INGREDIENTS_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%3d", model.getParty().getInventory().getIngredients()), 18, 0, MyColors.LIGHT_GREEN);
        model.getScreenHandler().put(26, 0, MATERIALS_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%3d", model.getParty().getInventory().getMaterials()), 23, 0, MyColors.LIGHT_GRAY);
        model.getScreenHandler().put(31, 0, ALIGNMENT_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", DailyEventState.getPartyAlignment(model)), 29, 0, MyColors.WHITE);
        model.getScreenHandler().put(36, 0, REP_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%2d",  model.getParty().getReputation()), 34, 0, MyColors.WHITE);

        drawKeyTexts(model);
    }

    protected void drawKeyTexts(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "ESC=MENU \\=LOG F1=HELP", 58, 0, MyColors.WHITE);
    }

    private static CharSprite makeAlignmentSprite() {
        CharSprite spr = CharSprite.make(0x14, MyColors.GRAY, MyColors.WHITE, MyColors.BLACK);
        spr.setColor4(MyColors.GREEN);
        return spr;
    }
}
