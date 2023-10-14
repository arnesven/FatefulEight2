package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.MiklosAutumntoft;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.*;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.ScaleArmor;
import model.items.weapons.Warhammer;
import model.races.Race;
import model.states.CombatEvent;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class PaladinEvent extends DailyEventState {
    private boolean fled = false;

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
        print("Investigate the screams? (Y/N) ");
        boolean gender = MyRandom.randInt(2) == 0;
        if (yesNoInput()) {
            List<Enemy> enemies = List.of(new GoblinAxeWielder('A'), new TrollEnemy('A'),
                    new WolfEnemy('A'), new BearEnemy('A'), new WildBoarEnemy('A'));
            Enemy monster = MyRandom.sample(enemies);
            println("You follow the noise and come to a spot where a " + monster.getName() + " is about to pounce " +
                    "at a young " + (gender?"girl":"boy") + ".");
            print("Do you intervene? (Y/N) ");
            if (yesNoInput()) {
                Race race = Race.allRaces[MyRandom.randInt(Race.allRaces.length)];
                println("You are about to spring into combat when a tall fellow, clad in armor, rushes in to aid you!");
                CombatEvent combat = new CombatEvent(model, List.of(monster));
                GameCharacter paladin = new GameCharacter("Paladin", "", race, Classes.PAL, new MiklosAutumntoft(),
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
                    showRandomPortrait(model, Classes.PAL, race, "Paladin");
                    println("The paladin helps the " + (gender?"girl":"boy") + " up and dusts " + himOrHer(gender) +
                            " off. Then he faces you.");
                    portraitSay("Good teamwork friend! We who can must protect those in need.");
                    changeClass(model);
                }
            } else {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                        List.of("Better " + himOrHer(gender) + " than us",
                        "Can't stop the course of nature.",
                                heOrSheCap(gender) + " probably had it coming..."));
            }
        } else {
            leaderSay("Probably just the wind...");
        }
    }

    public void changeClass(Model model) {
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.PAL);
        println("The paladin offers to train you in the ways of being a paladin, ");
        changeClassEvent.areYouInterested(model);
        println("You part ways with the paladin.");
    }
}
