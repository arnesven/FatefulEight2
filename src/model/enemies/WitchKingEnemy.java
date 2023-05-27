package model.enemies;

import model.Model;
import model.characters.WitchKingAppearance;
import model.classes.Classes;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.races.Race;
import view.sprites.Sprite;

public class WitchKingEnemy extends Enemy {
    private static final Sprite SPRITE = Classes.WITCH_KING.getAvatar(Race.WITCH_KING, new WitchKingAppearance());

    public WitchKingEnemy(char enemyGroup) {
        super(enemyGroup, "Witch King");
    }

    @Override
    public int getMaxHP() {
        return 10;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamageReduction() {
        return 2;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
