package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.Accessory;
import model.items.accessories.ChainGloves;
import model.items.accessories.LargeShield;
import model.items.accessories.LeatherGloves;
import model.items.clothing.LeatherArmor;
import model.items.weapons.*;
import model.map.WorldBuilder;
import model.map.WorldType;
import model.races.Race;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyRandom;
import util.MyStrings;
import view.subviews.PortraitSubView;

import java.util.List;

public class FedUpAdventurerEvent extends GeneralInteractionEvent {

    private static final String GOT_THIS_EVENT = "MetFedUpAdventurer";
    private final CharacterClass cls;
    private final GameCharacter victim;

    public FedUpAdventurerEvent(Model model) {
        super(model, "Talk to", MyRandom.randInt(15, 45), true);
        cls = MyRandom.sample(List.of(Classes.CAP, Classes.MAR, Classes.BBN, Classes.MIN, Classes.PAL));
        CharacterAppearance app = PortraitSubView.makeRandomPortrait(cls);
        Weapon w;
        Accessory a = new LargeShield();
        if (cls.id() == Classes.MAR.id()) {
            w = new HuntersBow();
            a = new ChainGloves();
        } else if (cls.id() == Classes.MIN.id()) {
            w = new BattleAxe();
        } else {
            w = new Broadsword();
        }
        victim = new GameCharacter("Disillusioned", "Adventurer", app.getRace(), cls, app,
                new Equipment(w, new LeatherArmor(), a));
    }

    public static DailyEventState generateEvent(Model model) {
        if (!model.getSettings().getMiscFlags().containsKey(GOT_THIS_EVENT) &&
                WorldBuilder.isInStartingArea(model) && model.getDay() < 10 && MyRandom.rollD10() == 10) {
            return new FedUpAdventurerEvent(model);
        }
        return null;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        String raceName = victim.getRace().getBasicName();
        println("You encounter " + MyStrings.aOrAn(raceName) + " " + raceName + ". " + heOrShe(victim.getGender()) +
                "looks rather haggard, as if " + heOrShe(victim.getGender()) + " been traveling in " +
                "rough conditions or for a long time.");
        println("The " + raceName + " looks at the party tiredly.");
        return false;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        leaderSay("Are you alright?");
        waitForReturn();
        return false;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm an adventurer. Or, I was. It's time to get off the road and settle down.";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        return victim;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return List.of();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_TO_DEATH;
    }
}
