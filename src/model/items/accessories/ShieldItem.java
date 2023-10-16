package model.items.accessories;

import model.characters.GameCharacter;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.Sprite;

public abstract class ShieldItem extends Accessory {

    private final boolean heavyArmor;
    private final int block;
    private Sprite[] SHIELD_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x70, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.PINK, MyColors.BEIGE)
    );

    public ShieldItem(String name, int cost, boolean isHeavy, int blockValue) {
        super(name, cost);
        this.heavyArmor = isHeavy;
        this.block = blockValue;
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

    public int getBlockChance() {
        return block;
    }

    @Override
    public String getExtraText() {
        return "Block " + getBlockChance();
    }

    @Override
    public boolean isOffHandItem() {
        return true;
    }

    @Override
    public int getWeight() {
        return 3000;
    }
}
