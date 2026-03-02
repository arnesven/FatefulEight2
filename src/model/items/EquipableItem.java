package model.items;

import model.characters.GameCharacter;
import view.sprites.AvatarItemSprite;

public abstract class EquipableItem extends Item {
    public EquipableItem(String name, int cost) {
        super(name, cost);
    }

    public abstract void equipYourself(GameCharacter gc);

    @Override
    public boolean canBeUsedFromMenu() {
        return true;
    }
}
