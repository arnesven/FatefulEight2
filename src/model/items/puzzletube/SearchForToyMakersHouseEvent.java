package model.items.puzzletube;

import model.Model;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.races.Race;
import model.states.DailyEventState;
import view.subviews.PortraitSubView;

public class SearchForToyMakersHouseEvent extends DailyEventState {
    private final MysteryOfTheTubesDestinationTask task;

    public SearchForToyMakersHouseEvent(Model model, MysteryOfTheTubesDestinationTask task) {
        super(model);
        this.task = task;
    }

    @Override
    protected void doEvent(Model model) {
        println("You begin searching the area for " + DwarvenPuzzleConstants.TOYMAKER_NAME + "'s house.");
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 8);
        if (!success) {
            println("You spend hours trying to find the house, but give up as the sun starts to set.");
            leaderSay("We're losing the light. Let's give it another go tomorrow.");
            return;
        }

        task.progressToHutFound();
        println("After little time spent search for the house of the toy maker you finally find it on " +
                "the side of a grassy hill.");
        leaderSay("Looks like somebody actually lives here.");
        println("You knock on the door, a dwarf opens the door.");
        model.getLog().waitForAnimationToFinish();
        CharacterAppearance son = PortraitSubView.makeRandomPortrait(Classes.None, Race.DWARF, false);
        showExplicitPortrait(model, son, "Dwarf");
        leaderSay("Mr " + DwarvenPuzzleConstants.TOYMAKER_LAST_NAME + "?");
        portraitSay("Yes, that's me. How can I help you?");
        leaderSay("Oh good. You see we've been tracking down your puzzle tubes...");
        portraitSay("Ah, you're talking about my father's work. I'm sorry, to say, I'm not " +
                DwarvenPuzzleConstants.TOYMAKER_NAME + ", I'm his son. I'm Jon.");
        leaderSay("I see. Where is your father? We would like to ask him about his puzzle tubes.");
        portraitSay("Unfortunately he went missing about a year ago. He went to his workshop one day and never came back.");
        leaderSay("That's terrible.");
        portraitSay("Yes. Me and the missus got everybody we knew to search for him, but we found nothing.");
        leaderSay("Did you search his workshop, any clues there?");
        portraitSay("No nothing. It looked like he just up and left in the middle of his work. No foul play either.");
        leaderSay("What a shame... Do you know anything about the puzzle tubes?");
        portraitSay("I'm sorry I don't really know anything about that. I was just a wee lad " +
                "when my father constructed them. I had forgotten all about them to tell you the truth.");
        leaderSay("Well... maybe we can check out the workshop. Perhaps you overlooked something?");
        portraitSay("That's okay with me. Here's the key. It's a bit of a hike from here, but I'll mark it on your map.");
        leaderSay("Thanks. See you around Jon.");
        portraitSay("Good luck!");
    }
}
