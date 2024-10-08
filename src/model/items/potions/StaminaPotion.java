package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;
import model.items.Item;
import model.items.Prevalence;
import model.items.StaminaRecoveryItem;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class StaminaPotion extends Potion implements StaminaRecoveryItem {

    private static final Sprite SPRITE = new ItemSprite(13, 7, MyColors.WHITE, MyColors.DARK_BLUE);

    public StaminaPotion() {
        super("Stamina Potion", 10);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", restores all SP for one party member.";
    }

    @Override
    public Item copy() {
        return new StaminaPotion();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        if (gc.hasCondition(VampirismCondition.class)) {
            return "Stamina Potion had no effect on " + gc.getName() + " (vampirism).";
        }
        gc.addToSP(gc.getMaxHP()-gc.getSP());
        return gc.getName() + " recovers all SP!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return !target.isDead() && target.getSP() < target.getMaxSP();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
