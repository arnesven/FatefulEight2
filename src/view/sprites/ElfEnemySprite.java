package view.sprites;

import view.MyColors;

public class ElfEnemySprite extends LoopingSprite {
    public ElfEnemySprite(MyColors skinColor) {
        super("elfenemy", "enemies.png", 0x08, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.DARK_GREEN);
        setColor3(skinColor);
        setColor4(MyColors.BROWN);
        setFrames(4);
    }
}
