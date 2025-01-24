package model.items.weapons;

import model.items.Prevalence;
import model.items.imbuements.StunImbuement;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class CalixaberSword extends TwoHandedSword {
    protected static final AvatarItemSprite[] TWO_HANDED_SWORD_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x30, MyColors.GOLD, MyColors.WHITE, MyColors.BROWN, MyColors.BEIGE));

    private static final Sprite SPRITE = new TwoHandedItemSprite(14, 14, MyColors.DARK_RED, MyColors.WHITE, MyColors.GOLD);

    public CalixaberSword() {
        setImbuement(new StunImbuement());
    }

    @Override
    public String getName() {
        return "Calixaber";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.unique;
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public String getExtraText() {
        return ", 30% chance of stunning humanoid opponents for 1 turn.";
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return TWO_HANDED_SWORD_SPRITES[index];
    }
}
