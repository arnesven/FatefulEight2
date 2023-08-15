package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.states.CombatEvent;
import model.states.GameState;
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
    public int getDamageReduction() {
        if (innerChar.getAP() < 3) {
            return 0;
        }
        if (innerChar.getAP() < 7) {
            return 1;
        }
        return 2;
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
        return innerChar.getEquipment().getWeapon().getDamageTable().length;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new NoCombatLoot();
    }

    @Override
    public void conditionsEndOfCombatRoundTrigger(Model model, GameState state) {
        super.conditionsEndOfCombatRoundTrigger(model, state);
        if (getHP() < 3) {
            state.println(getName() + ": \"I yield!\"");
            ((CombatEvent)state).retreatEnemy(this);
        }
    }
}
