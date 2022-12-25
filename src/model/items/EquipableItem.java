package model.items;

import model.characters.GameCharacter;

public abstract class EquipableItem extends Item {
    public EquipableItem(String name, int cost) {
        super(name, cost);
    }

    public abstract void equipYourself(GameCharacter gc);
}
