package model.items.accessories;

import model.items.Item;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TowerShield extends ShieldItem {

    private static final Sprite SPRITE = new ItemSprite(5, 3);

    public TowerShield() {
        super("Tower Shield", 28, true);
    }

    @Override
    public int getAP() {
        return 3;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TowerShield();
    }
}
