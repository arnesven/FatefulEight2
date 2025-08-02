package model.combat.abilities;

import model.GameStatistics;
import model.Model;
import model.actions.BasicCombatAction;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.combat.Combatant;
import model.states.CombatEvent;
import sprites.CombatSpeechBubble;
import util.MyRandom;
import view.subviews.CombatSubView;

import java.util.ArrayList;
import java.util.List;

class FleeCombatAction extends BasicCombatAction {
    public FleeCombatAction() {
        super("Flee", false, false);
    }

    protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
        boolean fleeSuccess = false;
        if (model.getParty().size() > 1) {
            SkillCheckResult result = performer.testSkill(model, Skill.Leadership,
                    3 + model.getParty().size() - model.getParty().getBench().size());
            combat.println("Trying to escape from combat (Leadership " + result.asString() + ").");
            if (result.isSuccessful()) {
                combat.leaderSay(MyRandom.sample(List.of("Retreat!", "Fall back!", "Let's get out of here!",
                        "They're too strong, let's go!", "There's no point, let's get out of here.")));
                combat.addSpecialEffect(model.getParty().getLeader(), new CombatSpeechBubble());
                fleeSuccess = true;
            }
        } else {
            int d10 = MyRandom.rollD10();
            combat.print("Trying to escape from combat (D10 roll=" + d10 + ")");
            if (d10 >= 5) {
                combat.println(" >=5, SUCCESS.");
                combat.leaderSay("I'm out of here.");
                combat.addSpecialEffect(model.getParty().getLeader(), new CombatSpeechBubble());
                fleeSuccess = true;
            } else {
                combat.println(" <5 FAIL. Can't get away!");
            }
        }
        if (fleeSuccess) {
            GameStatistics.incrementCombatsFled();
            if (model.getSubView() instanceof CombatSubView) {
                ((CombatSubView) model.getSubView()).enableFleeingAnimation();
            }
            combat.setPartyFled(true);
            for (GameCharacter gc : new ArrayList<>(combat.getAllies())) {
                combat.removeAlly(gc);
            }
        }
    }
}
