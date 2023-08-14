package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import view.sprites.Sprite;

public class TournamentEnemy extends Enemy {
    private GameCharacter innerChar = null;

    public TournamentEnemy(GameCharacter basedOnChar) {
        super('A', basedOnChar.getName());
        this.innerChar = basedOnChar;
        setCurrentHp(getMaxHP());
    }

    @Override
    public int getMaxHP() {
        if (innerChar == null) {
            return 1;
        }
        return innerChar.getMaxHP();
    }

    @Override
    public int getSpeed() {
        return innerChar.getSpeed();
    }

    @Override
    protected Sprite getSprite() {
        return innerChar.getAvatarSprite();
    }

    @Override
    public int getDamage() {
        return innerChar.getEquipment().getWeapon().getDamageTable().length - 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }
}
