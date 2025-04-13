package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.TransfiguredCondition;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.GreenSpellSprite;
import view.sprites.SmokeBallAnimation;
import view.sprites.Sprite;

public class TransfigurationSpell extends CombatSpell {
    private static final Sprite SPRITE = new GreenSpellSprite(0, true);

    public TransfigurationSpell() {
        super("Transfiguration", 28, MyColors.GREEN, 10, 3);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TransfigurationSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy && !target.hasCondition(TransfiguredCondition.class);
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        int newMax = Math.max(1, target.getMaxHP() - 3);
        int diff = 0;
        if (target.getHP() > newMax) {
            diff = target.getHP() - newMax;
            target.addToHP(-diff);
        }
        combat.addSpecialEffect(target, new SmokeBallAnimation());
        target.addCondition(new TransfiguredCondition(diff));
    }

    @Override
    public String getDescription() {
        return "Transforms the target into a harmless animal for 3 rounds.";
    }

}
