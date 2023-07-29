package model.items.accessories;

import model.items.Item;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class KiteShield extends PointedShield {

    private final ItemSprite sprite;

    public KiteShield() {
        super("Kite Shield", 28, false);
        this.sprite = new ItemSprite(2, 3, MyColors.GRAY, MyColors.BEIGE);
        sprite.setColor4(MyRandom.sample(List.of(MyColors.GREEN, MyColors.RED, MyColors.BLUE)));
    }

    @Override
    public int getAP() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new KiteShield();
    }
}
