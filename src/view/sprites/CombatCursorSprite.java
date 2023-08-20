package view.sprites;

import view.MyColors;

public class CombatCursorSprite extends LoopingSprite {
    public static final Sprite DEFAULT_CURSOR = new CombatCursorSprite(MyColors.WHITE);

    public CombatCursorSprite(MyColors col) {
        super("combatcursor" + col.toString(), "combat.png", 0x22, 32);
        setFrames(6);
        setColor1(MyColors.BLACK);
        setColor2(col);
        setDelay(4);
    }
}
