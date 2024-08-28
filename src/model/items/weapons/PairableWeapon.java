package model.items.weapons;

import view.sprites.Sprite;

public interface PairableWeapon {
    Sprite makePairSprite();
    default boolean pairingAllowed() { return true; }
}
