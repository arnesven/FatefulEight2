package view.subviews;

import model.Model;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite32x32;

public class RiverCombatTheme extends GrassCombatTheme {

    private static final RiverBankSprite upperBank = new RiverBankSprite(0x7D);
    private static final RiverBankSprite lowerBank = new RiverBankSprite(0x8D);
    private static final Sprite32x32 brideUpper = new BridgeSprite(0x1E);
    private static final Sprite32x32 brideLower = new BridgeSprite(0x2E);



    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        super.drawBackground(model, xOffset, yOffset);
        for (int i = 0; i < 8; ++i) {
            model.getScreenHandler().put(xOffset + i*4, yOffset + 4 * 4, upperBank);
            model.getScreenHandler().put(xOffset + i*4, yOffset + 5 * 4, lowerBank);
        }
        model.getScreenHandler().put(xOffset + 3 * 4, yOffset + 4 * 4, brideUpper);
        model.getScreenHandler().put(xOffset + 3 * 4, yOffset + 5 * 4, brideLower);

    }

    private static class RiverBankSprite extends LoopingSprite {
        public RiverBankSprite(int num) {
            super("banksprite", "combat.png", num, 32, 32);
            setFrames(3);
            setColor1(MyColors.GREEN);
            setColor2(MyColors.CYAN);
            setColor3(MyColors.BEIGE);
            setColor4(MyColors.BLUE);
        }
    }

    private static class BridgeSprite extends Sprite32x32 {
        public BridgeSprite(int num) {
            super("bridgesprite" + num, "combat.png", num,
                    MyColors.GREEN, MyColors.BROWN, MyColors.BEIGE, MyColors.BLUE);
        }
    }
}
