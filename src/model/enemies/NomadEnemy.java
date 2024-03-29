package model.enemies;

import model.Model;
import model.characters.preset.LonnieLiebgott;
import model.classes.Classes;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class NomadEnemy extends HumanoidEnemy {
    private static final Sprite SPRITE = Classes.AMZ.getAvatar(Race.NORTHERN_HUMAN, new LonnieLiebgott());

    public NomadEnemy(char enemyGroup, String name) {
        super(enemyGroup, name);
    }

    public NomadEnemy(char enemyGroup) {
        this(enemyGroup, "Nomad");
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }
}
