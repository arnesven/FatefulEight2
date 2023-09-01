package view.sprites;

import view.MyColors;

public class TrackEffectSprite extends LoopingSprite {
    public TrackEffectSprite(int num, MyColors color, MyColors color2) {
        super("trackeffect" + num, "riding.png", num, 32, 16);
        setFrames(4);
        setColor1(color);
        setColor2(color2);
    }
}
