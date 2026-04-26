package view.sprites;

import view.MyColors;

public class SpriteQuestMarker extends LoopingSprite {
    public SpriteQuestMarker(MyColors fillColor) {
        super("mqpquestmarker", "world_foreground.png", 0x95, 32, 32);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.WHITE);
        setColor3(fillColor);
        setFrames(2);
        setDelay(32);
    }
}
