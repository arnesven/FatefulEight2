package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class ColdEvent extends DailyEventState {
    public ColdEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The cold environment is affecting the party.");

        List<GameCharacter> gcs =
                model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Endurance, 8);
        if (gcs.isEmpty()) {
            return;
        }
        partyMemberSay(MyRandom.sample(gcs), MyRandom.sample(
                List.of("Brrr... I'm so cold!", "My pants, they're like ice.",
                        "I'm shaking so much that I feel like I'm dancing.",
                        "Can we please go someplace warmer?")));

        println("The frigid cold has the some of the party members shivering to their " +
                "teeth. They must consume more food to stay warm.");
        model.getParty().addToFood(-gcs.size());
    }
}
