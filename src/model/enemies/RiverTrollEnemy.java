package model.enemies;

import view.MyColors;
import view.sprites.RiverTrollSprite;

public class RiverTrollEnemy extends TrollEnemy {
    private final RiverTrollSprite sprite;

    public RiverTrollEnemy(char a, String type, int spriteNum, MyColors spriteColor3) {
        super(a);
        setName("River Troll " + type);
        this.sprite = new RiverTrollSprite(spriteNum, spriteColor3);
        this.setCurrentHp(getMaxHP());
    }

    @Override
    public final RiverTrollSprite getSprite() {
        return sprite;
    }

    @Override
    public int getMaxHP() {
        return 18;
    }
}
