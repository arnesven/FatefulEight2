package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.combat.Condition;
import model.states.CombatEvent;
import model.states.GameState;
import util.MyRandom;
import view.MyColors;
import view.help.HelpDialog;
import view.help.TutorialSneakAttack;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class SneakAttackCombatAction extends CombatAction {
    private Combatant target;
    private int sneakValue;
    private GameCharacter performer;
    private boolean takeAnotherAction;

    public SneakAttackCombatAction() {
        super("Sneak Attack", true);
        takeAnotherAction = false;
    }

    public static void cancel(GameState state, GameCharacter randomTarget) {
        state.println(randomTarget.getFirstName() + " sneak attack was cancelled.");
        randomTarget.removeCondition(SneakAttackCondition.class);
    }

    @Override
    public HelpDialog getHelpChapter(Model model) {
        return new TutorialSneakAttack(model.getView());
    }

    @Override
    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().sneakAttack(model);
        if (combat.isEligibleForSneakAttack(performer)) {
            this.performer = performer;
            SkillCheckResult result = performer.testSkill(Skill.Sneak);
            this.sneakValue = result.getModifiedRoll();
            this.target = target;
            combat.addSneakAttacker(performer, this);
            performer.addCondition(new SneakAttackCondition());
            combat.println(performer.getFirstName() + " prepares to perform a sneak attack, Sneaking " + result.asString() + ".");
        } else {
            combat.println(performer.getFirstName() + " has already been spotted by the enemy and cannot perform a sneak attack.");
            this.takeAnotherAction = true;
        }
    }

    @Override
    public boolean takeAnotherAction() {
        return takeAnotherAction;
    }

    public void resolveSneakAttack(Model model, CombatEvent combatEvent) {
        if (!performer.isDead() && !combatEvent.getEnemies().isEmpty()) {
            combatEvent.print(performer.getFirstName() + "'s sneak attack is resolved. ");
            if (target.isDead()) {
                combatEvent.print(" But " + target.getName() + " is already defeated, ");
                target = MyRandom.sample(combatEvent.getEnemies());
                combatEvent.println("so " + target.getName() + " is targeted instead.");
            }
            performer.doOneAttack(combatEvent, target, true, 0, 10);
            performer.removeCondition(SneakAttackCondition.class);
        }
    }

    private static final Sprite SPRITE = CharSprite.make((char)(0xD7), MyColors.BROWN, MyColors.YELLOW, MyColors.CYAN);

    public int getSneakValue() {
        return sneakValue;
    }

    private static class SneakAttackCondition extends Condition {
        public SneakAttackCondition() {
            super("Sneak Attack", "SNK");
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
        public boolean removeAtEndOfCombat() {
            return true;
        }
    }
}
