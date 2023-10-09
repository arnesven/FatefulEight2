package view.sprites;

import view.MyColors;

public class RaftSprite extends LoopingSprite {
    public RaftSprite() {
        super("raft", "enemies.png", 0x60, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.BROWN);
        setColor3(MyColors.DARK_GREEN);
        setColor4(MyColors.LIGHT_BLUE);
        setFrames(2);
    }
}
