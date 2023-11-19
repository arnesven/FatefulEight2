package sprites;

import view.MyColors;
import view.sprites.RunOnceAnimationSprite;

public class CombatSpeechBubble extends RunOnceAnimationSprite {
    public CombatSpeechBubble() {
        super("combatspeechbubble", "combat.png", 8, 6, 32, 32, 8, MyColors.BLACK);
        setColor2(MyColors.WHITE);
        setAnimationDelay(8);
    }
}
