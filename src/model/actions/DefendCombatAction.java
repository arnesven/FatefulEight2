package model.actions;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.combat.Condition;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class DefendCombatAction extends CombatAction {
    public static final int DEFEND_SKILL_RANKS = 3;

    public DefendCombatAction() {
        super("Defend");
    }

    public static boolean isDefending(GameCharacter gameCharacter) {
        return gameCharacter.hasCondition(DefendCondition.class);
    }

    @Override
    public void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        model.getTutorial().defending(model);
        combat.println(performer.getFirstName() + " takes a defensive stance.");
        performer.addCondition(new DefendCondition());
    }

    public static boolean canDoDefendAbility(GameCharacter performer) {
        Skill[] skills = new Skill[]{Skill.Axes, Skill.Blades, Skill.BluntWeapons, Skill.Polearms};
        for (Skill s : skills) {
            if (performer.getRankForSkill(s) >= DefendCombatAction.DEFEND_SKILL_RANKS &&
                    performer.getEquipment().getWeapon().getSkill() == s) {
                return true;
            }
        }
        return false;
    }

    private static final Sprite SPRITE = CharSprite.make((char) (0xD1), MyColors.WHITE, MyColors.BLUE, MyColors.CYAN);

    private static class DefendCondition extends Condition {
        public DefendCondition() {
            super("Defend", "DEF");
            setDuration(2);
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
