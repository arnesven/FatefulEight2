package model.enemies;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.conditions.Condition;
import model.combat.conditions.VampirismCondition;
import model.combat.loot.CombatLoot;
import model.combat.loot.FormerPartyMemberLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.enemies.behaviors.MagicRangedAttackBehavior;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.enemies.behaviors.RangedAttackBehavior;
import model.states.CombatEvent;
import util.MyLists;
import view.sprites.Sprite;

import java.util.List;

public class FormerPartyMemberEnemy extends Enemy {

    private final GameCharacter basedOn;
    private static final List<CharacterClass> CLASSES_WHO_DO_MAGIC_DAMAGE =
            List.of(Classes.WIT, Classes.WIZ, Classes.MAG, Classes.SOR,
                    Classes.ARCANIST, Classes.ENCHANTRESS,
                    Classes.WITCH_KING, Classes.MAGE);

    public FormerPartyMemberEnemy(GameCharacter gc) {
        super(getGroupForSpeed(gc), gc.getFullName(), attackBehaviorFromClass(gc));
        this.basedOn = gc;
        setCurrentHp(basedOn.getHP());
    }

    private static EnemyAttackBehavior attackBehaviorFromClass(GameCharacter gc) {
        if (MyLists.any(CLASSES_WHO_DO_MAGIC_DAMAGE,
                (CharacterClass cc) -> cc.getShortName().equals(gc.getCharClass().getShortName()))) {
            return new MagicRangedAttackBehavior();
        }
        if (gc.getEquipment().getWeapon().isRangedAttack()) {
            return new RangedAttackBehavior();
        }
        return new MeleeAttackBehavior();
    }

    private static char getGroupForSpeed(GameCharacter gc) {
        if (gc.getSpeed() < 2) {
            return 'A';
        }
        if (gc.getSpeed() < 5) {
            return 'B';
        }
        return 'C';
    }

    @Override
    public int getMaxHP() {
        if (basedOn == null) {
            return 1;
        }
        return basedOn.getMaxHP();
    }

    @Override
    public int getSpeed() {
        int bonus = MyLists.intAccumulate(getConditions(), (Condition::getSpeedBonus));
        if (getEnemyGroup() == 'A') {
            return 1 + bonus;
        } else if (getEnemyGroup() == 'B') {
            return 3 + bonus;
        }
        return 6 + bonus;
    }

    @Override
    public String getDeathSound() {
        return basedOn.getDeathSound();
    }

    @Override
    public int getDamageReduction() {
        int ap = basedOn.getAP();
        return (int)Math.ceil(ap / 4.0);
    }

    @Override
    protected Sprite getSprite() {
        return basedOn.getAvatarSprite();
    }

    @Override
    public int getDamage() {
        if (!getAttackBehavior().isPhysicalAttack() ||
                getAttackBehavior() instanceof RangedAttackBehavior) {
            return (basedOn.getEquipment().getWeapon().getDamageTableAsString().length()+1) / 3;
        }
        return (basedOn.getEquipment().getWeapon().getDamageTableAsString().length()+1) / 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new FormerPartyMemberLoot(basedOn);
    }

    @Override
    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) {
        super.doUponDeath(model, combatEvent, killer);
        if (basedOn.hasCondition(VampirismCondition.class)) {
            GameStatistics.incrementVampiresKilled();
        }
    }
}
