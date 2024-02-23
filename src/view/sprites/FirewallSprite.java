package view.sprites;

import view.MyColors;

public class FirewallSprite extends LoopingSprite {
    public FirewallSprite() {
        super("firewall", "combat.png", 0x88, 32, 32);
        setFrames(5);
        setColor2(MyColors.YELLOW);
        setColor3(MyColors.RED);
    }
}
