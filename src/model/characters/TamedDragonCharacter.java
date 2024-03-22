package model.characters;

import model.characters.preset.LonnieLiebgott;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.special.DragonClass;
import model.enemies.DragonEnemy;
import model.items.Equipment;
import model.items.Item;
import model.items.weapons.NaturalWeapon;
import model.items.weapons.RepeatingCrossbow;
import model.items.weapons.Weapon;
import model.races.Race;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;

public class TamedDragonCharacter extends GameCharacter {
    private final DragonEnemy dragon;

    public TamedDragonCharacter(GameCharacter master, DragonEnemy dragon) {
        super(master.getFirstName() + "'s Dragon", "", Race.NORTHERN_HUMAN,
                new DragonClass(dragon), new LonnieLiebgott(),
                Classes.NO_OTHER_CLASSES, new Equipment(new DragonWeapon()));
        this.dragon = dragon;
    }

    public Sprite getInventorySprite() {
        return dragon.getInventorySprite();
    }

    public Sprite getFlyingSprite() {
        return dragon.getAvatar();
    }

    private static class DragonWeapon extends NaturalWeapon {
        public DragonWeapon() {
            super("Dragon Weapon", 0, Skill.Bows, new int[]{7, 9, 11});
        }

        @Override
        public int getWeight() {
            return 0;
        }

        @Override
        public int getNumberOfAttacks() {
            return 2;
        }

        @Override
        public Item copy() {
            return new DragonWeapon();
        }
    }
}
