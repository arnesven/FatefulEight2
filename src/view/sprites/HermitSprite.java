package view.sprites;

import model.races.Race;
import view.MyColors;

public class HermitSprite extends LoopingSprite {
    public HermitSprite(Race race) {
        super("hermit", "enemies.png", 0x70, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.TAN);
        setColor3(race.getColor());
        setColor4(MyColors.LIGHT_GRAY);
        setFrames(4);
    }

    public HermitSprite() {
        this(Race.HIGH_ELF);
    }
}
