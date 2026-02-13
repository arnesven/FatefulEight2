package model.enemies;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.accessories.EmeraldRing;
import model.items.accessories.HeavyRing;
import model.items.accessories.Tiara;
import model.items.clothing.BreastPlate;
import model.items.clothing.OutlawArmor;
import model.items.weapons.Weapon;
import util.MyRandom;

import java.util.List;

public class BountyEnemy extends FormerPartyMemberEnemy {

    public BountyEnemy(CharacterAppearance app, CharacterClass cls, String firstName, String lastName, int level, Weapon weapon) {
        super(makeCharacter(firstName, lastName, cls, app, weapon, level));
    }

    private static GameCharacter makeCharacter(String firstName, String lastName, CharacterClass cls, CharacterAppearance app, Weapon weapon, int level) {
        GameCharacter gc = new GameCharacter(firstName, lastName, app.getRace(), cls, app);
        gc.setLevel(level);
        Equipment eq = new Equipment(weapon);

        if (level > 4) {
            eq.setClothing(new BreastPlate()); // DR = 2
        } else if (level > 2) {
            eq.setClothing(new OutlawArmor()); // DR = 1
        }

        eq.setAccessory(MyRandom.sample(List.of(new HeavyRing(), new Tiara(), new EmeraldRing())));
        gc.setEquipment(eq);
        return gc;
    }
}
