package model.items.accessories;

import model.characters.GameCharacter;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;

public abstract class PointedShield extends ShieldItem {

    private static final Sprite[] SHIELD_SPRITES = makeShiftedSpriteSet(new AvatarItemSprite(0x74, MyColors.RED, MyColors.DARK_RED, MyColors.PINK, MyColors.BEIGE));

    public PointedShield(String name, int cost, boolean isHeavy) {
        super(name, cost, isHeavy, 2);
    }

    @Override
    public Sprite getOnAvatarSprite(GameCharacter gameCharacter) {
        return SHIELD_SPRITES[gameCharacter.getCharClass().getWeaponShift(gameCharacter) + 1];
    }
}
