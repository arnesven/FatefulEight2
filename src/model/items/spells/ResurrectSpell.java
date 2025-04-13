package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.CastingFullRoundSpellCondition;
import model.items.Item;
import model.items.Prevalence;
import model.items.potions.RevivingElixir;
import model.states.CombatEvent;
import model.states.GameState;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.ShinyRingEffect;
import view.sprites.Sprite;
import view.sprites.WhiteSpellSprite;

public class ResurrectSpell extends CombatSpell implements FullRoundSpell {

    private static final Sprite SPRITE = new WhiteSpellSprite(7, true);

    public ResurrectSpell() {
        super("Resurrect", 75, MyColors.WHITE, 10, 4, false); // Diff 10, cost 4
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
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
            if (state instanceof CombatEvent) {
                ((CombatEvent) state).addSpecialEffect(target, new ShinyRingEffect());
                model.getLog().waitForAnimationToFinish();
            }
            target.addToHP(target.getMaxHP() / 2);
        }
        performer.removeCondition(CastingFullRoundSpellCondition.class);
    }

    @Override
    public int getCastTime() {
        return 1;
    }

    public static boolean useDuringEvent(Model model, GameState event, GameCharacter gc, ResurrectSpell resSpell) {
        event.print("Do you want to use " + resSpell.getName() + " to resurrect " + gc.getName() + "? (Y/N) ");
        if (!event.yesNoInput()) {
            return false;
        }
        GameCharacter caster = null;
        do {
            event.println("Who should cast Resurrect?");
            caster = model.getParty().partyMemberInput(model, event, model.getParty().getPartyMember(0));
            if (caster.isDead()) {
                event.println(caster.getFirstName() + " cannot cast resurrect, " +
                        GameState.heOrShe(caster.getGender()) + "'s dead!");
            } else {
                break;
            }
        } while (true);
        if (!resSpell.castYourself(model, event, caster)) {
            return false;
        }
        resSpell.castingComplete(model, event, caster, gc);
        return true;
    }
}
