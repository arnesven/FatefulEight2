package view.sprites;

import view.MyColors;

public class QuestCursorSprite extends LoopingSprite {
    public QuestCursorSprite() {
        super("questcursor", "quest.png", 0, 32, 32);
        setFrames(2);
        setColor1(MyColors.WHITE);
        setColor2(MyColors.WHITE);
        setColor3(MyColors.WHITE);
    }
}
