package model.states.events;

import model.Model;
import model.RecruitableCharacter;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.horses.Steed;
import model.items.Equipment;
import model.races.Race;
import model.states.DailyEventState;
import model.states.RecruitState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class FarmersHorseRaceEvent extends DailyEventState {
    public FarmersHorseRaceEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "A farmer with a horse";
    }

    @Override
    protected void doEvent(Model model) {
        println("The party comes upon a little farm. You hear the familiar neighing of horses.");
        boolean gender = MyRandom.flipCoin();
        String boyOrGirl = gender?"Girl":"Boy";
        println("As you a approach a young farmer " + boyOrGirl.toLowerCase() + " spots you.");
        Race race = MyRandom.sample(Race.getAllRaces());
        CharacterAppearance farmer = PortraitSubView.makeRandomPortrait(Classes.FARMER, race, gender);
        showExplicitPortrait(model, farmer, "Farmer " + boyOrGirl);
        portraitSay("Hey you! You like horses? You wanna race?");
        boolean borrowHorse = false;
        if (model.getParty().getHorseHandler().isEmpty()) {
            leaderSay("I don't have a horse.");
            portraitSay("That's alright, we have plenty. I'll lend you one. Just don't tell pops.");
            borrowHorse = true;
        }
        print("Do you want to race the " + boyOrGirl.toLowerCase() + "? (Y/N) ");
        if (yesNoInput()) {
            GameCharacter chosen = model.getParty().getPartyMember(0);
            if (model.getParty().size() > 1) {
                print("Which party member is going to ride?");
                chosen = model.getParty().partyMemberInput(model, this, model.getParty().getPartyMember(0));
            }
            portraitSay("Swell! Let's ride like the wind.");
            println("The " + boyOrGirl.toLowerCase() + " throws off " + hisOrHer(gender) + " hat and jumps up on a horse.");
            model.getLog().waitForAnimationToFinish();
            Horse horse = borrowHorse ? HorseHandler.generateHorse() : model.getParty().getHorseHandler().get(0);
            HorseRacingEvent raceEvent = new HorseRacingEvent(model, chosen, horse);
            GameCharacter farmerCharacter = new GameCharacter("Farmer " + boyOrGirl, "", race, Classes.None, farmer,
                    new CharacterClass[]{Classes.MAR, Classes.WIT, Classes.ART, Classes.THF});
            raceEvent.addNPC(farmerCharacter);
            raceEvent.setLaps(1);
            raceEvent.doEvent(model);
            setCurrentTerrainSubview(model);
            showExplicitPortrait(model, farmer, "Farmer " + boyOrGirl);
            if (raceEvent.didWin()) {
                portraitSay("Rats! We'll I should've know I'd be beaten by a grownup.");
                leaderSay("Don't beat yourself up kid. You'll be great one day.");
                portraitSay("I'm never gonna get out of here...");
                leaderSay("Sure you are. You've got potential to be a great adventurer.");
                portraitSay("You really mean it?");
                leaderSay("Yeah. Anybody can do it.");
                portraitSay("Can I become an adventurer right now? Can I come with you?");
                print("Let the kid join your party? (Y/N) ");
                if (yesNoInput()) {
                    farmerCharacter.setLevel(0);
                    farmerCharacter.setClass(Classes.None);
                    RecruitState recruitState = new RecruitState(model, RecruitableCharacter.makeOneNamedRecruitable(farmerCharacter));
                    recruitState.run(model);
                    if (model.getParty().getPartyMembers().contains(farmerCharacter)) {
                        leaderSay("Welcome to the party kid.");
                        Horse farmersHorse;
                        do {
                            farmersHorse = HorseHandler.generateHorse();
                        } while (farmersHorse instanceof Steed &&
                                (farmer.getRace().id() == Race.HALFLING.id() || farmer.getRace().id() == Race.DWARF.id()));
                        println("The farmer " + boyOrGirl.toLowerCase() + " has brought a horse to the party, it is a " + farmersHorse.getName() + ".");
                        model.getParty().getHorseHandler().addHorse(farmersHorse);
                    } else {
                        leaderSay("On second thought... you probably need to stay with your pops for another year or two.");
                    }
                } else {
                    leaderSay("Sorry kid. You're still too young.");
                    portraitSay("Bah... I knew you were nothing but talk. Smell you later.");
                    leaderSay("Bye...");
                }
            } else {
                portraitSay("Hah, I knew I could win. I just love riding!");
                leaderSay("Well done kid.");
                portraitSay("Thanks. Hey, why don't you stay for dinner. I'm sure I can convince pops.");
                leaderSay("Hehe, why not.");
                new GuestEvent(model).doEvent(model);
            }
        } else {
            leaderSay("I'm afraid we don't have time. We need to be on our way.");
            portraitSay("That's a shame, I'd prepared a nice track...");
            leaderSay("Maybe some other time.");
            portraitSay("Okay. Bye!");
        }

    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit farm with horse track", "I went by a farm recently which has a prepared horse track. It's nearby");
    }
}
