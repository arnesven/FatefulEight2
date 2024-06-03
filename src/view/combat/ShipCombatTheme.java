package view.combat;

import model.Model;
import model.map.SeaHex;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.LoopingSprite;
import view.sprites.Sprite32x32;

public class ShipCombatTheme extends CombatTheme {

    private static final SeaHex seaHex = new SeaHex(0);
    private static final Sprite32x32 DECK_SPRITE = new Sprite32x32("decksprite", "combat.png", 0x53,
           MyColors.DARK_GRAY, MyColors.BROWN, MyColors.GREEN, MyColors.CYAN);
    private static final LoopingSprite DECK_LL = new LoopingDeckSprite("deck_ul", 0x48);
    private static final LoopingSprite DECK_LR = new LoopingDeckSprite("deck_ul", 0x58);
    private static final LoopingSprite DECK_UL = new LoopingDeckSprite("deck_ul", 0x4B);
    private static final LoopingSprite DECK_UR = new LoopingDeckSprite("deck_ul", 0x5B);
    private static final Sprite32x32 DECK_TOP_LEFT = new Sprite32x32("decktopleft", "combat.png", 0x4E,
            MyColors.TAN, MyColors.BROWN, MyColors.CYAN, MyColors.CYAN);
    private static final Sprite32x32 DECK_TOP_RIGHT = new Sprite32x32("decktopright", "combat.png", 0x4F,
            MyColors.TAN, MyColors.BROWN, MyColors.CYAN, MyColors.CYAN);
    private static final Sprite32x32 DECK_LEFT = new Sprite32x32("deckleft", "combat.png", 0x5E,
            MyColors.TAN, MyColors.BROWN, MyColors.CYAN, MyColors.LIGHT_BLUE);
    private static final Sprite32x32 DECK_RIGHT = new Sprite32x32("deckright", "combat.png", 0x5F,
            MyColors.TAN, MyColors.BROWN, MyColors.CYAN, MyColors.LIGHT_BLUE);

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 8; ++x) {
                if (y + x < 2 || x - y > 5) {
                    seaHex.drawYourself(model.getScreenHandler(), xOffset + x * 4, yOffset + y * 4, 0);
                } else {
                    model.getScreenHandler().put(xOffset + x * 4, yOffset + y * 4, DECK_SPRITE);
                }
            }
        }

        model.getScreenHandler().put(xOffset + 0*4, yOffset + 2*4, DECK_LL);
        model.getScreenHandler().put(xOffset + 1*4, yOffset + 1*4, DECK_UL);
        model.getScreenHandler().put(xOffset + 2*4, yOffset + 0*4, DECK_UL);
        model.getScreenHandler().put(xOffset + 3*4, yOffset + 0*4, DECK_TOP_LEFT);
        model.getScreenHandler().put(xOffset + 4*4, yOffset + 0*4, DECK_TOP_RIGHT);
        model.getScreenHandler().put(xOffset + 5*4, yOffset + 0*4, DECK_UR);
        model.getScreenHandler().put(xOffset + 6*4, yOffset + 1*4, DECK_UR);
        model.getScreenHandler().put(xOffset + 7*4, yOffset + 2*4, DECK_LR);

        for (int y = 3; y < 9; ++y) {
            model.getScreenHandler().put(xOffset, yOffset + y*4, DECK_LEFT);
            for (int x = 1; x < 7; ++x) {
                model.getScreenHandler().put(xOffset + x*4, yOffset + y*4, DECK_SPRITE);
            }
            model.getScreenHandler().put(xOffset + 7*4, yOffset + y*4, DECK_RIGHT);
        }
    }

    private static class LoopingDeckSprite extends LoopingSprite {
        public LoopingDeckSprite(String name, int num) {
            super(name, "combat.png", num, 32);
            setColor1(MyColors.TAN);
            setColor2(MyColors.BROWN);
            setColor3(MyColors.CYAN);
            setColor4(MyColors.LIGHT_BLUE);
            setFrames(3);
        }
    }
}
