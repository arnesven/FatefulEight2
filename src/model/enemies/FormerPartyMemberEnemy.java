package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.loot.CombatLoot;
import model.combat.loot.FormerPartyMemberLoot;
import view.sprites.Sprite;

public class FormerPartyMemberEnemy extends Enemy {

    private final GameCharacter basedOn;

    public FormerPartyMemberEnemy(GameCharacter gc) {
        super(getGroupForSpeed(gc), gc.getFullName());
        this.basedOn = gc;
        setCurrentHp(basedOn.getHP());
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
        if (getEnemyGroup() == 'A') {
            return 1;
        } else if (getEnemyGroup() == 'B') {
            return 3;
        }
        return 6;
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
        return (basedOn.getEquipment().getWeapon().getDamageTableAsString().length()+1) / 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new FormerPartyMemberLoot(basedOn);
    }

}
