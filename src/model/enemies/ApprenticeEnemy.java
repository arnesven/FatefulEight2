package model.enemies;

import model.characters.GameCharacter;
import model.characters.LonnieLiebgott;
import model.characters.appearance.AdvancedAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.weapons.Warhammer;

public class ApprenticeEnemy extends FormerPartyMemberEnemy{
    public ApprenticeEnemy(AdvancedAppearance app) {
        super(new GameCharacter("Apprentice", "", app.getRace(), Classes.None, new LonnieLiebgott(),
                new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                new Equipment(new Warhammer())));
    }
}
