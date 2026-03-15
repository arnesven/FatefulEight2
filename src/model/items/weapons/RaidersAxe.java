package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RaidersAxe extends AxeWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(2, 11);
    private static final AvatarItemSprite AXE_SPRITES = new AvatarItemSprite(0x43, MyColors.GOLD, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.TRANSPARENT);

    public RaidersAxe() {
        super("Raider's Axe", 24, new int[]{5, 8, 8, 11}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RaidersAxe();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(3, 15);
    }

    @Override
    public AvatarItemSprite getOnAvatarSprite() {
        return AXE_SPRITES;
    }
}
