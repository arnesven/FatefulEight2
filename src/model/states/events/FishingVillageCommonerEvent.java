package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import util.MyRandom;
import util.MyStrings;
import view.subviews.PortraitSubView;

import java.util.List;

public class FishingVillageCommonerEvent extends GeneralInteractionEvent {
    private final AdvancedAppearance portrait;

    public FishingVillageCommonerEvent(Model model) {
        super(model, "Talk to", 2, true);
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.None);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("The party encounters one of the people who inhabit this village.");
        showExplicitPortrait(model, portrait, "Commoner");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        if (MyRandom.flipCoin()) {
            println("The commoner ignores you.");
        } else {
            portraitSay(MyRandom.sample(List.of("What do you want?", "Can I help you?",
                    "Did you want something?")));
        }
        leaderSay(iOrWeCap() + " just need some information.");
        portraitSay("Okay, but make it quick. I've got chores to do.");
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a commoner. I do a little of this and a little of that.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return new GameCharacter("Commoner", "", portrait.getRace(), Classes.None, portrait);
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
