package model.items;

import model.Model;
import model.characters.GameCharacter;
import model.items.potions.Potion;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RejuvenationPotion extends Potion {

    private static final Sprite SPRITE = new ItemSprite(13, 7, MyColors.WHITE, MyColors.DARK_PURPLE);

    public RejuvenationPotion() {
        super("Rejuvenation Potion", 10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", restores 3 HP and 1 SP.";
    }

    @Override
    public Item copy() {
        return new RejuvenationPotion();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addToHP(3);
        gc.addToSP(1);
        return gc.getName() + " recovered HP and SP!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return target.getHP() < target.getMaxHP() || target.getSP() < target.getMaxSP();
    }
}
