package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class VampirismPotion extends Potion {

    private static final Sprite SPRITE = new ItemSprite(15, 6, MyColors.WHITE, MyColors.DARK_RED);

    public VampirismPotion() {
        super("Vampire's Blood", 10);
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addCondition(new VampirismCondition(0, model.getDay()));
        return gc.getName() + " has been infected by Vampirism!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return !target.hasCondition(VampirismCondition.class);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return "Applies Vampirism Condition.";
    }

    @Override
    public Item copy() {
        return new VampirismPotion();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }
}
