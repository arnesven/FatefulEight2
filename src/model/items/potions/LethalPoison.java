package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.PoisonCondition;
import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LethalPoison extends Potion { // TODO: Extends throwable potion

    private static final Sprite SPRITE = new ItemSprite(13, 6, MyColors.WHITE, MyColors.DARK_PURPLE);

    public LethalPoison() {
        super("Lethal Poison", 48);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return "A very potent and deadly poison";
    }

    @Override
    public Item copy() {
        return new LethalPoison();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addToHP(-gc.getHP() + 1);
        gc.addCondition(new PoisonCondition());
        return gc.getName() + " drank the deadly poison!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return true;
    }
}
