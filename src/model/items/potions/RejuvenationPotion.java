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

public class RejuvenationPotion extends Potion implements StaminaRecoveryItem {

    private static final MyColors POTION_COLOR = MyColors.DARK_PURPLE;
    private static final Sprite SPRITE = new ItemSprite(13, 7, MyColors.WHITE, POTION_COLOR);
    private static final Sprite[] higherTierSprites = new ItemSprite[]{
            new ItemSprite(10, 6, MyColors.WHITE, POTION_COLOR),
            new ItemSprite(11, 6, MyColors.WHITE, POTION_COLOR),
            new ItemSprite(12, 6, MyColors.WHITE, POTION_COLOR),
            new ItemSprite(13, 6, MyColors.WHITE, POTION_COLOR),
    };
    private final Sprite sprite;
    private final int tier;
    private int healAmount = 3;

    public RejuvenationPotion() {
        super("Rejuvenation Potion", 6);
        this.sprite = SPRITE;
        tier = 0;
    }

    private RejuvenationPotion(int tier) {
        super(getPotionPrefixForHigherTier(tier) + " Rejuvenation Potion", 6 * (tier*2 + 1));
        healAmount = 3 + tier*2;
        this.sprite = getHigherTierSprite(tier);
        this.tier = tier;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public String getShoppingDetails() {
        return ", restores " + healAmount + " HP and 1 SP.";
    }

    @Override
    public Item copy() {
        if (tier == 0) {
            return new RejuvenationPotion();
        }
        return new RejuvenationPotion(tier);
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addToHP(healAmount);
        if (!gc.hasCondition(VampirismCondition.class)) {
            gc.addToSP(1);
        } else {
            return gc.getName() + " only recovered HP (vampirism).";
        }
        return gc.getName() + " recovered HP and SP!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return !target.isDead() && (target.getHP() < target.getMaxHP() || target.getSP() < target.getMaxSP());
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
        return new RejuvenationPotion(tier);
    }

    private Sprite getHigherTierSprite(int tier) {
        if (tier > 4) {
            tier = 4;
        }
        return higherTierSprites[tier-1];
    }
}
