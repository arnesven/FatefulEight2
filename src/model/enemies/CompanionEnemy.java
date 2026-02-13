package model.enemies;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.weapons.ShortSword;
import model.items.weapons.Weapon;

public class CompanionEnemy extends FormerPartyMemberEnemy {
    public CompanionEnemy(CharacterAppearance app, CharacterClass charClass, Weapon weapon) {
        super(new GameCharacter("Companion", "", app.getRace(), charClass, app, new Equipment((Weapon)weapon.copy())));
    }
}
