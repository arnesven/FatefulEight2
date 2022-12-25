package view.sprites;

import model.Model;
import view.MyColors;

public class CharacterSelectCursor extends LoopingSprite {
    public static CharacterSelectCursor LEFT_ARROW  = new CharacterSelectCursor(0x40);
    public static CharacterSelectCursor RIGHT_ARROW = new CharacterSelectCursor(0x50);

    public CharacterSelectCursor(int num) {
        super("charselectcursor", "combat.png", num, 32);
        setFrames(2);
        setColor1(MyColors.BLACK);
        setColor2(MyColors.WHITE);
        setDelay(12);
    }
}
