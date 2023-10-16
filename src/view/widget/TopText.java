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
    public static final CharSprite HORSES_ICON_SPRITE = CharSprite.make(0x15, MyColors.BEIGE, MyColors.BLACK, MyColors.BLACK);
    public static final CharSprite NOTORIETY_SPRITE = CharSprite.make(0x16, MyColors.RED, MyColors.BLACK, MyColors.BLACK);

    public void drawYourself(Model model) {
        int col = 0;
        col = addDay(model, col);
        col = addGold(model, col);
        col = addFood(model, col);
        col = addHorses(model, col);
        col = addAlighment(model, col);
        col = addNotoriety(model, col);
        col = addReputation(model, col);

        //col = addIngredients(model, col);
        //col = addMaterials(model, col); // TODO: Make top bar customizable by settings

        drawKeyTexts(model);
    }

    private int addNotoriety(Model model, int col) {
        model.getScreenHandler().put(col+3, 0, NOTORIETY_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%3d", model.getParty().getNotoriety()),
                col, 0, MyColors.RED);
        return col + 5;
    }

    private int addMaterials(Model model, int col) {
        model.getScreenHandler().put(col+3, 0, MATERIALS_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%3d", model.getParty().getInventory().getMaterials()),
                col, 0, MyColors.LIGHT_GRAY);
        return col + 5;
    }

    private int addIngredients(Model model, int col) {
        model.getScreenHandler().put(col+3, 0, INGREDIENTS_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(),
                String.format("%3d", model.getParty().getInventory().getIngredients()),
                col, 0, MyColors.LIGHT_GREEN);
        return col + 5;
    }

    private int addReputation(Model model, int col) {
        model.getScreenHandler().put(col+3, 0, REP_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%3d",  model.getParty().getReputation()), col, 0, MyColors.WHITE);
        return col + 5;
    }

    private int addHorses(Model model, int col) {
        model.getScreenHandler().put(col+4, 0, HORSES_ICON_SPRITE);
        MyColors horsesColor = MyColors.LIGHT_RED;
        if (model.getParty().getHorseHandler().canRide(model.getParty().getPartyMembers())){
            horsesColor = MyColors.LIGHT_GREEN;
        }
        BorderFrame.drawString(model.getScreenHandler(), String.format("%2d/%1d",  model.getParty().getHorseHandler().getFullBloods(),
                model.getParty().getHorseHandler().getPonies()), col, 0, horsesColor);
        return col + 6;
    }

    private int addAlighment(Model model, int col) {
        BorderFrame.drawString(model.getScreenHandler(), String.format("%3d", DailyEventState.getPartyAlignment(model)), col, 0, MyColors.WHITE);
        model.getScreenHandler().put(col+3, 0, ALIGNMENT_ICON_SPRITE);
        return col + 5;
    }

    private int addFood(Model model, int col) {
        model.getScreenHandler().put(col+3, 0, FOOD_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%3d", model.getParty().getFood()), col, 0, MyColors.PEACH);
        return col + 5;
    }

    private int addGold(Model model, int col) {
        model.getScreenHandler().put(col + 4, 0, GOLD_ICON_SPRITE);
        BorderFrame.drawString(model.getScreenHandler(), String.format("%4d", model.getParty().getGold()), col, 0, MyColors.LIGHT_YELLOW);
        return col + 6;
    }

    private int addDay(Model model, int col) {
        BorderFrame.drawString(model.getScreenHandler(), String.format("DAY %d", model.getDay()), 0, 0, MyColors.CYAN);
        return col + 8;
    }

    protected void drawKeyTexts(Model model) {
        BorderFrame.drawString(model.getScreenHandler(), "ESC=MENU F1=HELP F2=LOG", 57, 0, MyColors.WHITE);
    }

    private static CharSprite makeAlignmentSprite() {
        CharSprite spr = CharSprite.make(0x14, MyColors.GRAY, MyColors.WHITE, MyColors.BLACK);
        spr.setColor4(MyColors.GREEN);
        return spr;
    }
}
