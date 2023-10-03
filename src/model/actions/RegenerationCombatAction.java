package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.combat.Condition;
import model.items.weapons.StaffWeapon;
import model.items.weapons.WandWeapon;
import model.states.CombatEvent;
import model.states.GameState;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.DamageValueEffect;
import view.sprites.Sprite;

public class RegenerationCombatAction extends CombatAction {
    public static final int REQUIRED_RANKS = 2;
    public static final Skill SKILL_TO_USE = Skill.MagicGreen;
    public static final int DIFFICULTY = 7;

    public RegenerationCombatAction() {
        super("Regenerate", false);
    }
    
    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().regenerate(model);
        SkillCheckResult result = performer.testSkill(SKILL_TO_USE);
        combat.println(performer.getFirstName() + " attempts Regenerate on " + target.getName() + ", " +
                SKILL_TO_USE.getName() + " " + result.asString() + ".");
        if (result.getModifiedRoll() < DIFFICULTY || target.hasCondition(RegenerationCondition.class)) {
            combat.println("But it failed.");
        } else {
            int turns = result.getModifiedRoll() - DIFFICULTY + 1;
            combat.println(target.getName() + " starts regenerating health.");
            target.addCondition(new RegenerationCondition(turns));
        }
    }

    public static boolean canDoAbility(GameCharacter performer) {
        return performer.getRankForSkill(SKILL_TO_USE) >= REQUIRED_RANKS &&
                (performer.getEquipment().getWeapon().isOfType(StaffWeapon.class) ||
                        performer.getEquipment().getWeapon().isOfType(WandWeapon.class));
    }

    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.LIGHT_GREEN, MyColors.BLACK, MyColors.GREEN);

    private static class RegenerationCondition extends Condition {
        public RegenerationCondition(int duration) {
            super("Regeneration", "RGN");
            setDuration(duration+1);
        }

        @Override
        protected boolean noCombatTurn() {
            return false;
        }

        @Override
        public Sprite getSymbol() {
            return CONDITION_SPRITE;
        }

        @Override
        public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
            if (comb.getHP() < comb.getMaxHP()) {
                state.println(comb.getName() + " regenerates 1 Health Point.");
                comb.addToHP(1);
                if (state instanceof CombatEvent) {
                    ((CombatEvent) state).addFloatyDamage(comb, 1, DamageValueEffect.HEALING);
                }
            }
        }
    }
}
