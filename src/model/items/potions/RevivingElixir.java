package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RevivingElixir extends Potion {

    private static final Sprite SPRITE = new ItemSprite(14, 6, MyColors.WHITE, MyColors.LIGHT_BLUE);

    public RevivingElixir() {
        super("Reviving Elixir", 40);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", Revives a fallen party member during combat.";
    }

    @Override
    public Item copy() {
        return new RevivingElixir();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addToHP(2);
        return gc.getName() + " has come back to life!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return target.isDead();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
