package model.items;

import model.characters.GameCharacter;
import view.sprites.AvatarItemSprite;

public abstract class EquipableItem extends Item {
    public EquipableItem(String name, int cost) {
        super(name, cost);
    }

    public abstract void equipYourself(GameCharacter gc);

    protected static AvatarItemSprite[] makeShiftedSpriteSet(AvatarItemSprite template) {
        AvatarItemSprite[] sprites = new AvatarItemSprite[4];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = template.copy();
            sprites[i].shiftUpPx(i - 1);
        }
        return sprites;
    }

    @Override
    public boolean canBeUsedFromMenu() {
        return true;
    }
}
