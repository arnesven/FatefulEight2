package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.enemies.ServantEnemy;
import model.items.Equipment;
import model.items.clothing.FancyJerkin;
import model.items.weapons.*;
import model.races.Race;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class JesterEvent extends DarkDeedsEvent {
    private final String fullName;
    private final String shortName;
    private Race race;
    private AdvancedAppearance portrait;

    public JesterEvent(Model model, String fullName, String shortName) {
        super(model, "Talk to", MyRandom.randInt(5, 50));
        this.fullName = fullName;
        this.shortName = shortName;
        this.race = Race.ALL;
    }

    public JesterEvent(Model model) {
        this(model, "Court Jester", "jester");
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        println("The party encounters a " + fullName.toLowerCase() + ".");
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.BRD, race);
        showExplicitPortrait(model, portrait, fullName);
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        println("The " + fullName.toLowerCase() + " is not only a funny fellow, but has the " +
                "voice of an angel. He sings a lovely ballad of a long " +
                "forgotten kingdom and the romance between an elf prince " +
                "and a human princess. The party feels much refreshed " +
                "when going to bed tonight.");
        println("Each character recovers 1 SP.");
        boolean didSay = randomSayIfPersonality(PersonalityTrait.encouraging, new ArrayList<>(),
                "That was magical! You should play for kings and queens!");
        didSay = didSay || randomSayIfPersonality(PersonalityTrait.critical, new ArrayList<>(),
                "A bit derivative, if you ask me.");
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(1));
        print("The " + shortName + " is kind enough to offer to train you in the ways of being a Bard, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.BRD);
        change.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, fullName);
        return true;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a jester at the castle. I entertain the nobility. Wanna hear a joke?";
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        Weapon weapon = MyRandom.sample(List.of(new MorningStar(), new Warhammer(), new Club(), new Scepter(), new Flail()));
        GameCharacter gc = new GameCharacter(fullName, "", race, Classes.BRD, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(weapon, new FancyJerkin(), model.getItemDeck().getRandomJewelry()));
        gc.setLevel(MyRandom.randInt(2, 6));
        return gc;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        List<Enemy> companions = new ArrayList<>();
        for (int i = MyRandom.randInt(4); i > 0; --i) {
            companions.add(new ServantEnemy(PortraitSubView.makeRandomPortrait(Classes.None)));
        }
        for (int i = MyRandom.randInt(2); i > 0; --i) {
            companions.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.NOB),
                    Classes.NOB, model.getItemDeck().getRandomWeapon()));
        }
        companions.addAll(makeBodyGuards(MyRandom.randInt(3), 'C'));
        return companions;
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }
}
