package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class QuicksandEvent extends DailyEventState {
    private static final String REASON = " has sunken into the quicksand and perished!";

    public QuicksandEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() == 1) {
            singlePartyMemberEvent(model);
            return;
        }

        model.getParty().randomPartyMemberSay(model, List.of("Lets just stay here a moment and catch our breath."));
        List<GameCharacter> sinkers = new ArrayList<>();
        sinkers.add(MyRandom.sample(model.getParty().getPartyMembers()));
        List<GameCharacter> nonSinkers = new ArrayList<>();
        nonSinkers.addAll(model.getParty().getPartyMembers());
        nonSinkers.removeAll(sinkers);
        model.getParty().partyMemberSay(model, MyRandom.sample(nonSinkers), "Uhm, " + sinkers.get(0).getFirstName() + ", you're sinking...");
        model.getParty().partyMemberSay(model, sinkers.get(0), "Oh no! It's quicksand. Help me!");

        List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Logic, 4);
        if (!failers.isEmpty()) {
            GameCharacter fool = MyRandom.sample(failers);
            if (sinkers.contains(fool)) {
                model.getParty().partyMemberSay(model, MyRandom.sample(nonSinkers), "Don't struggle!");
                characterDies(model, this, fool, REASON, false);
                return;
            } else {
                model.getParty().partyMemberSay(model, fool, "Don't worry, I'm coming!");
                model.getParty().partyMemberSay(model, sinkers.get(0), "And now you're stuck too...");
                sinkers.add(fool);
                nonSinkers.remove(fool);
            }
        }

        if (nonSinkers.isEmpty()) {
            for (GameCharacter gc : sinkers) {
                if (model.getParty().doSkillCheckWithReRoll(model, this, gc, Skill.Acrobatics, 7, 10, 0).isSuccessful()) {
                    println(gc.getName() + " manages to crawl out of the quicksand pit.");
                } else {
                    characterDies(model, this, gc, REASON, false);
                }
            }
        } else {
            model.getParty().partyMemberSay(model, nonSinkers.get(0), "Don't move. If you struggle you'll only sink faster!");
            model.getParty().partyMemberSay(model, sinkers.get(0), "Well, find something to pull me out with then!");
            boolean foundBranch = model.getParty().doCollaborativeSkillCheckExcluding(model, this, Skill.Search, 7, sinkers);
            if (foundBranch) {
                model.getParty().partyMemberSay(model, nonSinkers.get(0), "Here, a branch. Grab onto it!");
                model.getParty().partyMemberSay(model, sinkers.get(0), "Phew... that was close!");
            } else {
                model.getParty().partyMemberSay(model, nonSinkers.get(0), "Aaah, aah, there's nothing to use!#");
                MyLists.forEach(sinkers, (GameCharacter gc) ->
                    characterDies(model, this, gc, REASON, false));
            }
        }
    }

    private void singlePartyMemberEvent(Model model) {
        println("You stop for a moment to catch your breath. Suddenly you realize that you " +
                "are slowly sinking down into the sand. Oh no! It's quicksand!");
        boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Acrobatics, 7);
        if (result) {
            println("You manage to scramble out of the quicksand pit.");
            model.getParty().randomPartyMemberSay(model, List.of("Wow, that could have ended badly."));
        } else {
            characterDies(model, this, model.getParty().getPartyMember(0),
                    REASON, false);
        }
    }
}
