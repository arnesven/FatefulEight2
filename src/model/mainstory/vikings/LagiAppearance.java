package model.mainstory.vikings;

import model.characters.appearance.*;
import model.races.BroadShoulders;
import model.races.Race;
import view.MyColors;
import view.sprites.Sprite8x8;

public class LagiAppearance extends AdvancedAppearance {
    public LagiAppearance() {
        super(Race.WOOD_ELF, false, MyColors.ORANGE, 0, 0,
                new ElfinEyes(), new FlameTipsHair(), new NoBeard());
        setShoulders(new BroadShoulders(false));
        setNeck(new SlenderNeck());
    }

    @Override
    protected void specialization() {
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 3; ++x) {
                Sprite8x8 sp = new Sprite8x8("ft1", "face.png", 0x235 + y * 0x10 + x);
                sp.setColor1(MyColors.LIGHT_YELLOW);
                sp.setColor2(MyColors.ORANGE);
                addSpriteOnTop(x+2, y, sp);
            }
        }
    }
}
