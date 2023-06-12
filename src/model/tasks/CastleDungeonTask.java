package model.tasks;

import model.Model;
import model.Summon;
import model.map.UrbanLocation;
import model.states.ExploreRuinsState;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;

public class CastleDungeonTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public CastleDungeonTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        println(location.getLordName() + ": \"I'm afraid an evil presence has settled in the depths of my dungeons. " +
                "My relative went down there to check what was going on and I haven't seen him for, uh, weeks." +
                "I've been looking for some strong, brave adventuring types to root it out. Can you help me?\"");
        print("Do you wish to descend into the dungeon now? (Y/N) ");
        if (yesNoInput()) {
            SubView sub = model.getSubView();
            ExploreRuinsState explore = new ExploreRuinsState(model, location.getPlaceName(), "Dungeon");
            explore.run(model);
            if (explore.getDungeon().isCompleted()) {
                CollapsingTransition.transition(model, sub);
                summon.increaseStep();
                println(location.getLordName() + ": \"There you are, I was beginning to think you found the same fate as my relative. " +
                        "Thank you for dealing with my little 'problem'.");
            }
        } else {
            println(location.getLordName() + ": \"I understand, you need more time to prepare. But please hurry. " +
                    "Something is eating the staff, and good help is so hard to find.\"");
        }
    }

    @Override
    public String getJournalDescription() {
        return heOrSheCap(location.getLordGender()) + " needs help clearing " +
                hisOrHer(location.getLordGender()) + " dungeon from an evil presence.";
    }
}
