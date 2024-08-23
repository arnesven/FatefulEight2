package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.CastingFullRoundSpellCondition;
import model.items.Item;
import model.states.CombatEvent;
import model.states.GameState;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.ShinyRingEffect;
import view.sprites.Sprite;

public class ResurrectSpell extends CombatSpell implements FullRoundSpell {

    private static final Sprite SPRITE = new ItemSprite(9, 8, MyColors.BROWN, MyColors.WHITE, MyColors.DARK_GRAY);

    public ResurrectSpell() {
        super("Resurrect", 75, MyColors.WHITE, 10, 4, false); // Diff 10, cost 4
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ResurrectSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof GameCharacter && target.isDead();
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (performer.hasCondition(CastingFullRoundSpellCondition.class)) {
            combat.println(performer.getFirstName() + " is already casting a spell! Resurrect failed.");
        } else {
            combat.println(performer.getFirstName() + " is attempting to bring " + target.getName() + " back to life!");
            performer.addCondition(new CastingFullRoundSpellCondition(this, performer, target, combat.getCurrentRound()));
        }
    }

    @Override
    public String getDescription() {
        return "Brings a character back to life. Takes a full combat round to cast.";
    }

    @Override
    public void castingComplete(Model model, GameState state, Combatant performer, Combatant target) {
        state.print(performer.getName() + "'s resurrect spell is resolved. ");
        if (!target.isDead()) {
            state.println(target.getName() + " is no longer dead. Resurrect failed.");
        } else {
            model.getLog().waitForAnimationToFinish();
            state.println(target.getName() + " comes back to life!");
            ((CombatEvent)state).addSpecialEffect(target, new ShinyRingEffect());
            model.getLog().waitForAnimationToFinish();
            target.addToHP(target.getMaxHP() / 2);
        }
        performer.removeCondition(CastingFullRoundSpellCondition.class);
    }

    @Override
    public int getCastTime() {
        return 1;
    }
}
