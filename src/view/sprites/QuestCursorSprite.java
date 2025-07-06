package view.sprites;

import view.MyColors;

public class QuestCursorSprite extends LoopingSprite {
    public QuestCursorSprite(MyColors color) {
        super("questcursor", "quest.png", 0, 32, 32);
        setFrames(2);
        setColor1(color);
        setColor2(color);
        setColor3(color);
    }

    public QuestCursorSprite() {
        this(MyColors.WHITE);
    }
}
