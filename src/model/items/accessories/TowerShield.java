package model.items.accessories;

import model.characters.GameCharacter;
import model.items.Item;
import view.MyColors;
import view.sprites.AvatarItemSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TowerShield extends ShieldItem {

    private static final Sprite SPRITE = new ItemSprite(5, 3);
    private static final Sprite[] SHIELD_SPRITES = makeShiftedSpriteSet(
            new AvatarItemSprite(0x60, MyColors.BROWN, MyColors.GRAY, MyColors.PINK, MyColors.BEIGE));

    public TowerShield() {
        super("Tower Shield", 34, true, 3);
    }

    @Override
    public int getAP() {
        return 3;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TowerShield();
    }

    @Override
    public Sprite getOnAvatarSprite(GameCharacter gameCharacter) {
        return SHIELD_SPRITES[gameCharacter.getCharClass().getWeaponShift(gameCharacter) + 1];
    }
}
