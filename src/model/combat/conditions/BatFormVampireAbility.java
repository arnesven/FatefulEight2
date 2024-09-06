package model.combat.conditions;

import model.Model;
import model.actions.CombatAction;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.combat.abilities.SpecialAbilityCombatAction;
import model.states.CombatEvent;
import util.MyRandom;
import view.GameView;
import view.help.HelpDialog;
import view.help.SubChapterHelpDialog;
import view.sprites.SmokePuffAnimation;

import java.util.List;

public class BatFormVampireAbility extends VampireAbility {
    public static int ROUNDS = 5;

    public BatFormVampireAbility() {
        super("Bat Form", 0x92,
                "Transform into a bat. Useful for entering houses when " +
                        "you are on the prowl or for escaping more easily from combat.");
    }

    public static boolean canDoAbility(GameCharacter performer) {
        if (performer.hasCondition(VampirismCondition.class)) {
            VampirismCondition vampCond = (VampirismCondition) performer.getCondition(VampirismCondition.class);
            return vampCond.hasBatAbility();
        }
        return false;
    }

    public static SpecialAbilityCombatAction makeCombatAbility() {
        return new SpecialAbilityCombatAction("Bat Form", false, false) {
            @Override
            public boolean possessesAbility(Model model, GameCharacter performer) {
                return canDoAbility(performer);
            }

            @Override
            protected boolean meetsOtherRequirements(Model model, GameCharacter performer, Combatant target) {
                return true;
            }

            @Override
            public HelpDialog getHelpChapter(Model model) {
                return BatFormVampireAbility.getHelpChapter(model.getView());
            }

            @Override
            protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                combat.partyMemberSay(performer, MyRandom.sample(getVerbalExpressions()));
                combat.println(target.getName() + " turns into a bat.");
                combat.addSpecialEffect(performer, new SmokePuffAnimation());
                performer.addCondition(new BatFormCondition());
            }
        };
    }

    public static HelpDialog getHelpChapter(GameView view) {
        return new SubChapterHelpDialog(view, "Bat Form",
                "Bat Form is a vampire ability which transforms you into a bat for " + ROUNDS + " rounds. " + BatFormCondition.DETAILS);
    }

    public static List<String> getVerbalExpressions() {
        return List.of("Bat form!", "Bat!", "Dark Wings!", "Abracadabra!", "Shazam!", "Here I go!",
                "Flap flap flap!", "Zippit!");
    }

    @Override
    public HelpDialog makeHelpChapter(GameView view) {
        return getHelpChapter(view);
    }
}
