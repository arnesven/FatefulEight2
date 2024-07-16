package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.quests.QuestEdge;
import model.states.GameState;
import view.MyColors;

import java.util.List;

class ClimbingScene extends FeedingSubScene {
    private final int windowLevel;

    public ClimbingScene(int col, int row, int windowLevel, GameCharacter vampire) {
        super(col, row, vampire);
        this.windowLevel = windowLevel;
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.LIGHT_GREEN;
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
        state.print(vampire.getFirstName() + " climbs up the facade of the house... ");
        SkillCheckResult result = vampire.testSkill(model, Skill.Acrobatics, 6 + windowLevel * 2);
        state.println("Acrobatics " + result.asString() + ".");
        if (result.isSuccessful()) {
            state.println(" and nimbly slips in through the window.");
            return getSuccessEdge();
        }
        state.println("But lost " + GameState.hisOrHer(vampire.getGender()) + " foothold and falls down!");
        int hpBefore = vampire.getHP();
        int healthLoss = Math.min(vampire.getHP() - 1, 3);
        vampire.addToHP(-healthLoss);
        state.println(vampire.getName() + " loses " + healthLoss + " HP.");
        model.getParty().partyMemberSay(model, vampire, List.of("Ouch.", "That hurt.", "Darn it!#",
                "Ouch, my back!", "Ow... my butt."));
        return getFailEdge();
    }
}
