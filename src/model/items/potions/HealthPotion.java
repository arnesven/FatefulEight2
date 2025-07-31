package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class HealthPotion extends Potion {
    private static final Sprite SPRITE = new ItemSprite(13, 7, MyColors.WHITE, MyColors.DARK_RED);
    private static final Sprite[] higherTierSprites = new ItemSprite[]{
            new ItemSprite(10, 6, MyColors.WHITE, MyColors.DARK_RED),
            new ItemSprite(11, 6, MyColors.WHITE, MyColors.DARK_RED),
            new ItemSprite(12, 6, MyColors.WHITE, MyColors.DARK_RED),
            new ItemSprite(13, 6, MyColors.WHITE, MyColors.DARK_RED),
    };

    private int healingAmount = 5;
    private final Sprite sprite;

    public HealthPotion() {
        super("Health Potion", 10);
        sprite = SPRITE;
    }

    protected HealthPotion(int tier) {
        super(getPotionPrefixForHigherTier(tier) + " Health Potion", ((tier*2)+1)*10);
        healingAmount = 5 + tier*3;
        sprite = getHigherTierSprite(tier);
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public String getShoppingDetails() {
        return ", Restores " + healingAmount + " HP of one party member.";
    }

    @Override
    public Item copy() {
        return new HealthPotion();
    } // TODO: Fix so that tier is passed, for now alchemy cannot make these

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        int hpBefore = gc.getHP();
        gc.addToHP(healingAmount);
        return gc.getName() + " recovers " + (gc.getHP() - hpBefore) + " health!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return !target.isDead() && target.getHP() < target.getMaxHP();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public boolean supportsHigherTier() {
        return true;
    }

    @Override
    public Item makeHigherTierCopy(int tier) {
        return new HealthPotion(tier);
    }

    private Sprite getHigherTierSprite(int tier) {
        if (tier > 4) {
            tier = 4;
        }
        return higherTierSprites[tier-1];
    }

    @Override
    public int getWeight() {
        return 250 + healingAmount * 10;
    }
}
