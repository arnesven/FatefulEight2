package view.sprites;

import view.MyColors;

public class ParticleSprite extends LoopingSprite {
    public ParticleSprite(int num, MyColors color) {
        super("particle" + num, "ritual.png", num, 8, 8);
        setColor1(color);
        setColor2(MyColors.WHITE);
        setFrames(4);
    }
}
