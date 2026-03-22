package model.enemies;

import model.Model;
import model.characters.preset.LonnieLiebgott;
import model.characters.preset.SebastianSmith;
import model.classes.Classes;
import model.combat.loot.CombatLoot;
import model.combat.loot.NoCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class BlackKnightEnemy extends HumanoidEnemy {
    private static Sprite avatar = Classes.BKN.getAvatar(Race.SOUTHERN_HUMAN,
            new SebastianSmith());

    public BlackKnightEnemy(char enemyGroup) {
        super(enemyGroup, "Black Knight");
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return avatar;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
