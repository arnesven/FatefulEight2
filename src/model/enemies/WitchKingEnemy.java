package model.enemies;

import model.Model;
import model.characters.special.WitchKingAppearance;
import model.classes.Classes;
import model.combat.loot.CombatLoot;
import model.combat.loot.NoCombatLoot;
import model.enemies.behaviors.MagicMeleeAttackBehavior;
import model.races.Race;
import view.sprites.Sprite;

public class WitchKingEnemy extends HumanoidEnemy {
    private static final Sprite SPRITE = Classes.WITCH_KING.getAvatar(Race.WITCH_KING, new WitchKingAppearance());

    public WitchKingEnemy(char enemyGroup) {
        super(enemyGroup, "Witch King");
        setAttackBehavior(new MagicMeleeAttackBehavior());
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
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
