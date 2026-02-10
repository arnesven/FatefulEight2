package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.special.GoodFishingPole;
import model.items.weapons.FishingPole;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

import static model.states.events.FishermanEvent.COST;

public class FishingVillageFisherEvent extends GeneralInteractionEvent {
    private AdvancedAppearance portrait;
    private String name;

    public FishingVillageFisherEvent(Model model) {
        super(model, "Talk to", 5, true);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        showEventCard("Fisherman", "The party encounters a fisherman on the dock.");
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.FARMER);
        this.name = "Fisher" + (portrait.getGender() ? "woman" : "man");
        showExplicitPortrait(model, portrait, name);
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        leaderSay("Good day to you sir. Are the fish biting?");
        portraitSay("Shhh! You'll scare the fish away.");
        randomSayIfPersonality(PersonalityTrait.mischievous, new ArrayList<>(),
                "OH, SORRY, WE DIDN'T REALIZE YOU WERE FISHING!");
        portraitSay("Oh well... I wasn't having much fishing luck anyway. " +
                "Guess I'm going home empty-handed again. Or... you wouldn't be interested in purchasing this " +
                "fine fishing pole? I'll let you have it for " + COST + " gold!");
        randomSayIfPersonality(PersonalityTrait.critical, new ArrayList<>(),
                "Oh come on, we're not that gullible.");
        if (model.getParty().getGold() >= COST) {
            print("Buy the fishing pole? (Y/N) ");
        }
        if (model.getParty().getGold() >= COST && yesNoInput()) {
            leaderSay("Fine, we'll take it.");
            portraitSay("Excellent. Here you go.");
            println("You lost " + COST + " gold");
            model.getParty().spendGold(COST);
            println("You got a fishing pole.");
            model.getParty().getInventory().add(new GoodFishingPole());
        } else {
            leaderSay("No thank you.");
            portraitSay("Well, it was worth a shot.");
        }
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a fisher. I live off what the sea can provide.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter(name, "", portrait.getRace(), Classes.None, portrait, Classes.NO_OTHER_CLASSES, new Equipment(new FishingPole()));
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return List.of();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.ALWAYS_ESCAPE;
    }
}
