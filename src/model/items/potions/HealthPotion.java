package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class HealthPotion extends Potion {
    private static final Sprite SPRITE = new ItemSprite(13, 7, MyColors.WHITE, MyColors.DARK_RED);

    public HealthPotion() {
        super("Health Potion", 10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return "Restores 5 HP of one party member.";
    }

    @Override
    public Item copy() {
        return new HealthPotion();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        int hpBefore = gc.getHP();
        gc.addToHP(5);
        return gc.getName() + " recovers " + (gc.getHP() - hpBefore) + " health!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return !target.isDead() && target.getHP() < target.getMaxHP();
    }
}
