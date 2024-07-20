package model.states.feeding;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.quests.QuestEdge;
import model.states.GameState;
import util.MyStrings;
import view.MyColors;

class StakeOutSubScene extends FeedingSubScene {
    private final VampireFeedingHouse house;

    public StakeOutSubScene(int col, int row, GameCharacter vampire, VampireFeedingHouse house) {
        super(col, row, vampire);
        this.house = house;
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state, GameCharacter vampire) {
        state.print(vampire.getFirstName() + " stakes out the house for a few minutes. ");
        SkillCheckResult result = vampire.testSkill(model, Skill.Perception);
        state.println("Perception " + result.asString() + ".");
        if (result.getModifiedRoll() < 6 || result.getUnmodifiedRoll() == 1) {
            state.println("But " + heOrShe(vampire.getGender()) + " discerns nothing.");
        } else {
            String dwellers = "are " + MyStrings.numberWord(house.getDwellers()) + " people";
            if (house.getDwellers() == 1) {
                dwellers = "is only one person";
            }
            state.print("And " + hisOrHer(vampire.getGender()) + " vampiric senses tell " +
                    himOrHer(vampire.getGender()) + " that there " + dwellers + " living there");
            if (result.getModifiedRoll() >= 10) {
                if (house.getDwellers() == 1) {
                    if (house.getSleeping() == 1) {
                        state.println(", and he or she is asleep.");
                    } else {
                        state.println(", and he or she is awake.");
                    }
                } else {
                    String numberWord = MyStrings.numberWord(house.getSleeping()).replace("zero", "none");
                    if (house.getDwellers() == house.getSleeping()) {
                        numberWord = "all";
                    }
                    state.println(", " + numberWord +
                            " of them " + (house.getSleeping() == 1 ? "is" : "are") + " asleep.");
                }
                house.setSleepInfoGiven(true);
            } else {
                state.println(".");
            }
        }
        return super.getSuccessEdge();
    }
}
