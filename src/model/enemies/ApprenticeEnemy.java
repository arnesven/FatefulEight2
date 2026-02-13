package model.enemies;

import model.characters.GameCharacter;
import model.characters.preset.LonnieLiebgott;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.items.Equipment;
import model.items.weapons.Weapon;

public class ApprenticeEnemy extends FormerPartyMemberEnemy{
    public ApprenticeEnemy(AdvancedAppearance app, Weapon weapon) {
        super(new GameCharacter("Apprentice", "", app.getRace(), Classes.None, new LonnieLiebgott(),
                new Equipment(weapon)));
    }
}
