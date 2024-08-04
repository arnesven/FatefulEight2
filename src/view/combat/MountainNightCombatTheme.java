package view.combat;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.Random;

public class MountainNightCombatTheme extends MountainCombatTheme {
    private Random random;
    private Sprite mountUL;
    private Sprite mountUR;
    private Sprite mountLL;
    private Sprite mountLR;

    public MountainNightCombatTheme(MyColors groundColor) {
        super(groundColor, MyColors.GRAY, MyColors.WHITE);
        mountUL = new Sprite32x32("mountul", "combat.png", 0x70, MyColors.DARK_GRAY, groundColor, MyColors.GRAY, MyColors.DARK_BLUE);
        mountUR = new Sprite32x32("mountur", "combat.png", 0x71, MyColors.DARK_GRAY, groundColor, MyColors.GRAY, MyColors.DARK_BLUE);
        mountLL = new Sprite32x32("mountll", "combat.png", 0x80, MyColors.DARK_GRAY, groundColor, MyColors.GRAY, MyColors.DARK_BLUE);
        mountLR = new Sprite32x32("mountlr", "combat.png", 0x81, MyColors.DARK_GRAY, groundColor, MyColors.GRAY, MyColors.DARK_BLUE);
    }

    public MountainNightCombatTheme() {
        this(MyColors.TAN);
    }

    @Override
    public void drawBackground(Model model, int xOffset, int yOffset) {
        for (int i = 0; i < 4; ++i) {
            model.getScreenHandler().put(xOffset + i*8, yOffset, mountUL);
            model.getScreenHandler().put(xOffset + i*8+4, yOffset, mountUR);
            model.getScreenHandler().put(xOffset + i*8, yOffset+4, mountLL);
            model.getScreenHandler().put(xOffset + i*8+4, yOffset+4, mountLR);
        }
        drawGround(model, xOffset, yOffset);
    }
}
