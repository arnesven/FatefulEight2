package model.items.spells;

import model.Model;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.conditions.Condition;
import model.enemies.BeastEnemy;
import model.enemies.Enemy;
import model.items.Item;
import model.states.CombatEvent;
import model.states.GameState;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.GreenSpellSprite;
import view.sprites.Sprite;

public class HarmonizeSpell extends CombatSpell {
    public static final String SPELL_NAME = "Harmonize";
    private static final Sprite SPRITE = new GreenSpellSprite(2, true);

    public HarmonizeSpell() {
        super(SPELL_NAME, 20, MyColors.GREEN, 9, 2, true);
    }

    public static String getMagicExpertTips() {
        return "A master of the spell Harmonize can calm down beasts faster than a novice.";
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getDescription() {
        return "A spell for calming bests.";
    }

    @Override
    public Item copy() {
        return new HarmonizeSpell();
    }

    @Override
    public boolean canBeCastOn(Model model, Combatant target) {
        return target instanceof Enemy;
    }

    @Override
    public void applyCombatEffect(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        if (!(target instanceof BeastEnemy)) {
            combat.println("The spell had no effect on " + target.getName() + "...");
            return;
        }

        BeastEnemy beast = (BeastEnemy) target;
        if (beast.reduceAggressiveness()) {
            for (int i = 0; i < getMasteryLevel(performer); ++i) {
                beast.reduceAggressiveness();
            }
            combat.println(beast.getName() + " seems calmer.");
        } else {
            combat.println(beast.getName() + " doesn't seem much calmer.");
        }
        beast.addCondition(new RetreatIfCalmAtEndOfRoundCondition());
    }

    @Override
    public Integer[] getThresholds() {
        return new Integer[]{20, 50, 100, 200};
    }

    @Override
    public boolean masteriesEnabled() {
        return true;
    }

    private static final Sprite HMZ_SPRITE = CharSprite.make((char)(0xD5), MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.CYAN);

    private static class RetreatIfCalmAtEndOfRoundCondition extends Condition {
        public RetreatIfCalmAtEndOfRoundCondition() {
            super("Harmonize", "HMZ");
        }

        @Override
        protected boolean noCombatTurn() {
            return false;
        }

        @Override
        public Sprite getSymbol() {
            return HMZ_SPRITE;
        }

        @Override
        public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
            if (state instanceof CombatEvent) {
                if (comb instanceof BeastEnemy) {
                    if (((BeastEnemy) comb).getAggressiveness() == BeastEnemy.DOCILE) {
                        state.println(comb.getName() + " has retreated from combat.");
                        ((CombatEvent) state).retreatEnemy(comb);
                    }
                }
            }
        }

        @Override
        public ConditionHelpDialog getHelpView(GameView view) {
            return new ConditionHelpDialog(view, this,
                    "A condition indicating that this combatant has been pacified and may retreat at the end of the combat round.");
        }
    }
}
