package view.sprites;

import view.MyColors;

public class SkeletonEnemySprite extends LoopingSprite {
    public SkeletonEnemySprite() {
        super("skeletonenemy", "enemies.png", 0x18, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.WHITE);
        setColor3(MyColors.RED);
        setColor4(MyColors.RED);
        setFrames(4);
    }
}
