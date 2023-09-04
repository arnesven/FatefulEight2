package model.items.potions;

import model.Model;
import model.characters.GameCharacter;
import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RejuvenationPotion extends Potion {

    private static final Sprite SPRITE = new ItemSprite(13, 7, MyColors.WHITE, MyColors.DARK_PURPLE);
    private static final Sprite[] higherTierSprites = new ItemSprite[]{
            new ItemSprite(10, 6, MyColors.WHITE, MyColors.DARK_PURPLE),
            new ItemSprite(11, 6, MyColors.WHITE, MyColors.DARK_PURPLE),
            new ItemSprite(12, 6, MyColors.WHITE, MyColors.DARK_PURPLE),
            new ItemSprite(13, 6, MyColors.WHITE, MyColors.DARK_PURPLE),
    };
    private final Sprite sprite;
    private int healAmount = 3;

    public RejuvenationPotion() {
        super("Rejuvenation Potion", 10);
        this.sprite = SPRITE;
    }

    private RejuvenationPotion(int tier) {
        super(getHigherTierPrefix(tier) + " Rejuvenation Potion", 10 * (tier*2 + 1));
        healAmount = 3 + tier*2;
        this.sprite = getHigherTierSprite(tier);
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
        return new RejuvenationPotion();
    }

    @Override
    public String useYourself(Model model, GameCharacter gc) {
        gc.addToHP(healAmount);
        gc.addToSP(1);
        return gc.getName() + " recovered HP and SP!";
    }

    @Override
    public boolean canBeUsedOn(Model model, GameCharacter target) {
        return target.getHP() < target.getMaxHP() || target.getSP() < target.getMaxSP();
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
