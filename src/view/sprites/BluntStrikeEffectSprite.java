package view.sprites;

import view.MyColors;

public class BluntStrikeEffectSprite extends RunOnceAnimationSprite {
    public BluntStrikeEffectSprite() {
        super("bluntstrike", "combat.png",
                8, 10, 32, 32, 5, MyColors.WHITE);
        setAnimationDelay(5);
    }
}
