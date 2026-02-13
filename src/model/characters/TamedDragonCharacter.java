package model.characters;

import model.characters.preset.LonnieLiebgott;
import model.classes.Classes;
import model.classes.special.DragonClass;
import model.enemies.DragonEnemy;
import model.races.Race;
import view.sprites.Sprite;

public class TamedDragonCharacter extends GameCharacter {
    private final DragonEnemy dragon;

    public TamedDragonCharacter(GameCharacter master, DragonEnemy dragon) {
        super(master.getFirstName() + "'s Dragon", "", Race.NORTHERN_HUMAN,
                new DragonClass(dragon), new LonnieLiebgott(),
                dragon.getTamedEquipment());
        this.dragon = dragon;
    }

    public Sprite getInventorySprite() {
        return dragon.getInventorySprite();
    }

    public Sprite getFlyingSprite() {
        return dragon.getAvatar();
    }

}
