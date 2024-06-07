package view.sprites;

import model.races.Race;
import view.MyColors;

public class SoldierSprite extends LoopingSprite {
    public SoldierSprite(MyColors armorColor, MyColors skinColor) {
        super("soldier", "enemies.png", 0x0C, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(armorColor);
        setColor3(skinColor);
        setColor4(MyColors.LIGHT_GRAY);
        setFrames(4);
    }

    public SoldierSprite(MyColors armorColor) {
        this(armorColor, Race.NORTHERN_HUMAN.getColor());
    }
}
