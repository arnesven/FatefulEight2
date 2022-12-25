package view.sprites;

import model.races.Race;
import view.MyColors;

public class SoldierSprite extends LoopingSprite {
    public SoldierSprite(MyColors armorColor) {
        super("soldier", "enemies.png", 0x0C, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(armorColor);
        setColor3(Race.NORTHERN_HUMAN.getColor());
        setColor4(MyColors.LIGHT_GRAY);
        setFrames(4);
    }
}
