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

    private static final MyColors POTION_COLOR = MyColors.DARK_BLUE;
    private static final Sprite SPRITE = new ItemSprite(13, 7, MyColors.WHITE, POTION_COLOR);
    private static final Sprite[] higherTierSprites = new ItemSprite[]{
            new ItemSprite(10, 6, MyColors.WHITE, POTION_COLOR),
            new ItemSprite(11, 6, MyColors.WHITE, POTION_COLOR),
            new ItemSprite(12, 6, MyColors.WHITE, POTION_COLOR),
            new ItemSprite(13, 6, MyColors.WHITE, POTION_COLOR),
    };
    private final int tier;
    private final int restoreAmount;
    private final Sprite sprite;

    public StaminaPotion() {
        super("Stamina Potion", 10);
        tier = 0;
        restoreAmount = 2;
        this.sprite = SPRITE;
    }

    private StaminaPotion(int tier) {
        super(getPotionPrefixForHigherTier(tier) + " Stamina Potion", 10 * (tier*2 + 1));
        restoreAmount = tier + 2;
        this.sprite = getHigherTierSprite(tier);
        this.tier = tier;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", restores " + (restoreAmount) + " SP for one party member.";
    }

    @Override
    public boolean supportsHigherTier() {
        return true;
    }

    @Override
    public Item makeHigherTierCopy(int tier) {
        return new StaminaPotion(tier);
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
        return gc.getName() + " recovers " + restoreAmount + " SP!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return !target.isDead() && target.getSP() < target.getMaxSP();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    private Sprite getHigherTierSprite(int tier) {
        if (tier > 4) {
            tier = 4;
        }
        return higherTierSprites[tier-1];
    }
}
