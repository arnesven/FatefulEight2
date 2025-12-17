package model.states.events;

import model.Model;
import model.RecruitInfo;
import model.RecruitableCharacter;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.*;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.ScaleArmor;
import model.items.weapons.Warhammer;
import model.races.Race;
import model.states.CombatEvent;
import model.states.DailyEventState;
import model.states.RecruitState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class PaladinEvent extends DailyEventState {
    private boolean fled = false;
    private AdvancedAppearance portrait;

    public PaladinEvent(Model model) {
        super(model);
    }

    @Override
    public boolean haveFledCombat() {
        return fled;
    }

    @Override
    protected void doEvent(Model model) {
        println("You suddenly hear distant screams. Faintly, you hear somebody calling for help.");
        model.getParty().randomPartyMemberSay(model, List.of("Sounds like somebody is in trouble."));
        randomSayIfPersonality(PersonalityTrait.benevolent, List.of(model.getParty().getLeader()),
                "We must act.");
        randomSayIfPersonality(PersonalityTrait.unkind, List.of(model.getParty().getLeader()),
                "Not really our problem though, is it?");
        print("Investigate the screams? (Y/N) ");
        boolean gender = MyRandom.randInt(2) == 0;
        if (yesNoInput()) {
            List<Enemy> enemies = List.of(new GoblinAxeWielder('A'), new TrollEnemy('A'),
                    new WolfEnemy('A'), new BearEnemy('A'), new WildBoarEnemy('A'));
            Enemy monster = MyRandom.sample(enemies);
            println("You follow the noise and come to a spot where a " + monster.getName() + " is about to pounce " +
                    "at a young " + (gender?"girl":"boy") + ".");
            boolean didSay = randomSayIfPersonality(PersonalityTrait.cold, List.of(model.getParty().getLeader()),
                    "Let's not interfere with nature...");
            if (didSay) {
                randomSayIfPersonality(PersonalityTrait.benevolent, new ArrayList<>(),
                        "You low life. Of course we must intervene!");
            }
            print("Do you intervene? (Y/N) ");
            if (yesNoInput()) {
                Race race = Race.randomRace();
                println("You are about to spring into combat when a tall fellow, clad in armor, rushes in to aid you!");
                CombatEvent combat = new CombatEvent(model, List.of(monster));
                this.portrait = PortraitSubView.makeRandomPortrait(Classes.PAL, race);
                GameCharacter paladin = new GameCharacter("Paladin", "", race, Classes.PAL, portrait,
                            Classes.NO_OTHER_CLASSES,
                            new Equipment(new Warhammer(), new ScaleArmor(), new SkullCap()));
                paladin.setLevel(3);
                combat.addAllies(List.of(paladin));
                combat.run(model);
                this.fled = combat.fled();
                if (fled) {
                    return;
                }
                setCurrentTerrainSubview(model);
                if (!paladin.isDead()) {
                    showExplicitPortrait(model, portrait, "Paladin");
                    println("The paladin helps the " + (gender?"girl":"boy") + " up and dusts " + himOrHer(gender) +
                            " off. Then he faces you.");
                    portraitSay("Good teamwork friend! We who can must protect those in need.");
                    if (getPartyAlignment(model) > 1 && model.getParty().getNotoriety() >= 0) {
                        portraitSay("I've been looking for a purpose for some time. You people seem to " +
                                "have the right attitude. Need an extra hand?");
                        GameCharacter paladin2 = new GameCharacter(randomFirstName(paladin.getGender()), randomLastName(),
                                paladin.getRace(), Classes.PAL, portrait, makeRandomClassSet(Classes.PAL), paladin.getEquipment());
                        paladin2.setLevel((int)Math.ceil(calculateAverageLevel(model)));
                        new RecruitState(model, RecruitableCharacter.makeOneRecruitable(paladin2, RecruitInfo.none));
                    } else {
                        changeClass(model);
                    }
                    println("You part ways with the paladin.");
                }
            } else {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                        List.of("Better " + himOrHer(gender) + " than us",
                        "Can't stop the course of nature.",
                                heOrSheCap(gender) + " probably had it coming..."));
                randomSayIfPersonality(PersonalityTrait.benevolent, List.of(model.getParty().getLeader()),
                        "I feel really bad about this.");
            }
        } else {
            leaderSay("Probably just the wind...");
        }
    }

    public void changeClass(Model model) {
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.PAL);
        println("The paladin offers to train you in the ways of being a paladin, ");
        changeClassEvent.areYouInterested(model);
        setCurrentTerrainSubview(model);
        showExplicitPortrait(model, portrait, "Paladin");
    }
}
