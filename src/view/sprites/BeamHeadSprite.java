package view.sprites;

import view.MyColors;

public class BeamHeadSprite extends LoopingSprite {
    public BeamHeadSprite(MyColors color, int rotation, int i) {
        super("strengthtwoormorebeamhead", "gauge.png", 0x20 + 0x10*i, 32, 32);
        setFrames(4);
        setColor1(color);
        setColor2(MyColors.WHITE);
        setRotation(rotation);
    }
}
