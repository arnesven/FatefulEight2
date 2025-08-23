package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.combat.conditions.ParalysisCondition;
import model.combat.conditions.TimedParalysisCondition;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class AntiParalysisPotion extends Potion {

    private static final Sprite SPRITE = new ItemSprite(15, 6, MyColors.WHITE, MyColors.YELLOW);

    public AntiParalysisPotion() {
        super("Anti-Paralysis Potion", 16);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", Cures Paralysis for one party member.";
    }

    @Override
    public Item copy() {
        return new AntiParalysisPotion();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.removeCondition(ParalysisCondition.class);
        gc.removeCondition(TimedParalysisCondition.class);
        return gc.getName() + "'s paralysis was cured!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return target.hasCondition(ParalysisCondition.class);
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
