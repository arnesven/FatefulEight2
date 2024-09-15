package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.*;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.combat.conditions.Condition;
import model.combat.conditions.TimedParalysisCondition;
import model.combat.conditions.TransfiguredCondition;
import model.combat.conditions.WeakenCondition;
import model.enemies.Enemy;
import model.items.spells.TransfigurationSpell;
import model.items.weapons.StaffWeapon;
import model.items.weapons.WandWeapon;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.help.HelpDialog;
import view.help.TutorialCurseAbility;
import view.sprites.CharSprite;
import view.sprites.DamageValueEffect;
import view.sprites.DownArrowAnimation;
import view.sprites.Sprite;

public class CurseCombatAction extends SpecialAbilityCombatAction {
    public static final Skill SKILL_TO_USE = Skill.MagicBlack;
    public static final int DIFFICULTY = 7;
    public static final int REQUIRED_RANKS = 2;

    public CurseCombatAction() {
        super("Curse", true, false);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialCurseAbility(model.getView());
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().curseAbility(model);
        SkillCheckResult result = performer.testSkill(model, SKILL_TO_USE);
        combat.println(performer.getFirstName() + " attempts Curse on " + target.getName() + ", " +
                SKILL_TO_USE.getName() + " " + result.asString() + ".");
        if (result.getModifiedRoll() < DIFFICULTY) {
            combat.println("But it failed.");
        } else {
            int roll = MyRandom.rollD10();
            combat.addSpecialEffect(target, new DownArrowAnimation());
            if (roll < 4 && !target.hasCondition(WeakenCondition.class)) {
                combat.println(target.getName() + " gets hit by the Curse of Weaken.");
                int turns = result.getModifiedRoll() - DIFFICULTY + 1;
                target.addCondition(new WeakenCondition(turns));
            } else if (roll < 7 && !target.hasCondition(PainCondition.class)) {
                combat.println(target.getName() + " gets hit by the Curse of Pain.");
                int turns = (result.getModifiedRoll() - DIFFICULTY + 2) / 2;
                target.addCondition(new PainCondition(turns));
            } else if (roll < 10 && !target.hasCondition(TimedParalysisCondition.class)) {
                combat.println(target.getName() + " gets hit by the Curse of Terror.");
                int turns = (result.getModifiedRoll() - DIFFICULTY + 2) / 2;
                target.addCondition(new TimedParalysisCondition(turns));
            } else if (!target.hasCondition(TransfiguredCondition.class)) {
                combat.println(target.getName() + " gets hit by the Curse of Transfiguration.");
                new TransfigurationSpell().applyCombatEffect(model, combat, performer, target);
            } else {
                combat.println("But it failed.");
            }
        }
    }

    private static final Sprite SPRITE = CharSprite.make((char)(0xC0), MyColors.RED, MyColors.YELLOW, MyColors.CYAN);

    public Condition getPainCondition() {
        return new PainCondition(3);
    }

    @Override
    public boolean possessesAbility(Model model, GameCharacter performer) {
        return performer.getUnmodifiedRankForSkill(SKILL_TO_USE) >= REQUIRED_RANKS;
    }

    @Override
    protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
        return target instanceof Enemy &&
                (performer.getEquipment().getWeapon().isOfType(StaffWeapon.class) ||
                        performer.getEquipment().getWeapon().isOfType(WandWeapon.class));
    }

    private static class PainCondition extends Condition {

        public PainCondition(int turns) {
            super("Pain", "PAN");
            setDuration(turns + 1);
        }

        @Override
        protected boolean noCombatTurn() {
            return false;
        }

        @Override
        public Sprite getSymbol() {
            return SPRITE;
        }

        @Override
        public void endOfCombatRoundTrigger(Model model, GameState state, Combatant comb) {
            super.endOfCombatRoundTrigger(model, state, comb);
            state.println(comb.getName() + " takes 1 damage from the pain.");
            comb.addToHP(-1);
            if (state instanceof CombatEvent) {
                ((CombatEvent) state).addFloatyDamage(comb, 1, DamageValueEffect.MAGICAL_DAMAGE);
            }
            if (comb.isDead()) {
                state.println(comb.getName() + " has died from the pain!");
            }
        }

        @Override
        public ConditionHelpDialog getHelpView(GameView view) {
            return new ConditionHelpDialog(view, this, "A condition indicating that this " +
                    "combatant is currently suffering from magically induced pain and will take damage each combat round.");
        }
    }
}
