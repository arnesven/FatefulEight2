package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.FixedAvatarItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class DaiKatana extends BladedWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(10, 1);

    private static final AvatarItemSprite[] AVATAR_SPRITES = makeShiftedSpriteSet(
            new FixedAvatarItemSprite(0xA1, MyColors.GOLD, MyColors.LIGHT_GRAY, MyColors.DARK_BLUE, MyColors.LIGHT_GRAY));

    public DaiKatana() {
        super("Dai-Katana", 26, new int[]{10, 11, 11, 11, 12}, true, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DaiKatana();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }

    @Override
    protected AvatarItemSprite getOnAvatarSprite(int index) {
        return AVATAR_SPRITES[index];
    }
}
