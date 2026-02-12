package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.PossessedCondition;
import model.enemies.FormerPartyMemberEnemy;
import model.enemies.HumanoidEnemy;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import sprites.CombatSpeechBubble;
import view.MyColors;
import view.sprites.GreenSpellSprite;
import view.sprites.Sprite;

import java.util.HashMap;
import java.util.Map;

public class VomitSpell extends CombatSpell {

    private static final Sprite SPRITE = new GreenSpellSprite(8, true);
    private static final int CASTS_REQUIRED_TO_VOMIT_PEARL = 3;
    private CombatEvent combatInstance = null;
    private Map<Combatant, Integer> castCounts;

    public VomitSpell() {
        super("Expunge", 16, MyColors.GREEN, 8, 1, false);
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return true;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (combat != combatInstance) {
            combatInstance = combat;
            castCounts = new HashMap<>();
        }
        if (target instanceof GameCharacter || target instanceof HumanoidEnemy) {
            combat.println(target.getName() + " becomes queasy.");
            if (target.hasCondition(PossessedCondition.class)) {
              if (!castCounts.containsKey(target)) {
                   castCounts.put(target, 1);
               } else {
                   castCounts.put(target, castCounts.get(target) + 1);
                   if (castCounts.get(target) == 2) {
                       combat.println(target.getName() + " bends forward, as if to throw up, but manages to hold it back.");
                       model.getLog().waitForAnimationToFinish();
                       combat.addSpecialEffect(target, new CombatSpeechBubble());
                       combat.printQuote(target.getName(), "Ugh...");
                   } else if (castCounts.get(target) == CASTS_REQUIRED_TO_VOMIT_PEARL) {
                       combat.println(target.getName() + " vomits out a crimson pearl!");
                       target.removeCondition(PossessedCondition.class);
                       model.getLog().waitForAnimationToFinish();
                       combat.addSpecialEffect(target, new CombatSpeechBubble());
                       combat.printQuote(target.getName(), "What's going on here! Stop this fighting at once!");
                       combat.setTimeLimit(0);
                   }
               }
            }
        } else {
            combat.println("The spell has no effect on " + target.getName() + ".");
        }
    }

    @Override
    public boolean canBeUsedWithMass() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Forces a humanoid target to vomit.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new VomitSpell();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.veryRare;
    }
}
