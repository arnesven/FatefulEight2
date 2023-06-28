package model.items.accessories;

import model.characters.GameCharacter;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;

public abstract class ShieldItem extends Accessory {

    private final boolean heavyArmor;
    private Sprite[] SHIELD_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x70, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.PINK, MyColors.BEIGE)
    );

    public ShieldItem(String name, int cost, boolean isHeavy) {
        super(name, cost);
        this.heavyArmor = isHeavy;
    }

    @Override
    public boolean isHeavy() {
        return heavyArmor;
    }

    @Override
    public String getSound() {
        return "wood";
    }

    public Sprite getOnAvatarSprite(GameCharacter gameCharacter) {
        int index = gameCharacter.getCharClass().getWeaponShift(gameCharacter) + 1;
        return SHIELD_SPRITES[index];
    }
}
