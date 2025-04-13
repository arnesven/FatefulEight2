package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.PoisonCondition;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class AntidotePotion extends Potion {

    private static final Sprite SPRITE = new ItemSprite(15, 6, MyColors.WHITE, MyColors.LIGHT_GREEN);

    public AntidotePotion() {
        super("Antidote", 10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", Cures Poison for one party member.";
    }

    @Override
    public Item copy() {
        return new AntidotePotion();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.removeCondition(PoisonCondition.class);
        return gc.getName() + " is no longer poisoned!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return target.hasCondition(PoisonCondition.class);
    }
}
