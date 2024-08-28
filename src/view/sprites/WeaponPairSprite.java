package view.sprites;

import model.items.weapons.PairableWeapon;
import view.MyColors;

import java.util.List;

public class WeaponPairSprite extends Sprite32x32 {
    public WeaponPairSprite(String itemName, PairableWeapon weapon1, PairableWeapon weapon2) {
        super(itemName, "items.png", 0x0F, MyColors.GRAY,
                List.of(setFlip(weapon1.makePairSprite()), weapon2.makePairSprite(), TwoHandedItemSprite.ROMAN_TWO));
    }

    private static Sprite setFlip(Sprite sprite) {
        sprite.setFlipHorizontal(true);
        return sprite;
    }
}
