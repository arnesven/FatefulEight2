package model.enemies;

import model.characters.GameCharacter;
import model.characters.LonnieLiebgott;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.weapons.Dagger;

public class ServantEnemy extends FormerPartyMemberEnemy {
    public ServantEnemy(CharacterAppearance app) {
        super(new GameCharacter("Servant", "", app.getRace(), Classes.None, new LonnieLiebgott(),
                new CharacterClass[]{Classes.None, Classes.None, Classes.None, Classes.None},
                new Equipment(new Dagger())));
    }
}
