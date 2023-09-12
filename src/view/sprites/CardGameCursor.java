package view.sprites;

import view.MyColors;


public class CardGameCursor extends LoopingSprite {
    public CardGameCursor() {
        super("cardcursor", "cardgame.png", 0x20, 16, 16);
        setColor1(MyColors.YELLOW);
        setFrames(2);
    }
}
