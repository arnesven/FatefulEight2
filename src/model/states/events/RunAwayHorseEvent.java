package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.SoldierEnemy;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class RunAwayHorseEvent extends DailyEventState {
    public RunAwayHorseEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You spot a horse up ahead.");
        leaderSay("A rider?");
        if (model.getParty().size() > 1) {
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()), "Not a rider, just a horse.");
        }
        println("The horse has a saddle and reins and looks like it has been ridden recently.");
        leaderSay("Where's your master, huh?");
        println("You look around, but there is nobody here except your party.");
        Horse horse = HorseHandler.generateHorse();
        print("The horse is a " + horse.getType() + ", do you keep it? (Y/N) ");
        if (yesNoInput()) {
            if (MyRandom.randInt(3) == 0) {
                println("You are about to get up on the horse when you hear an angry voice.");
                showRandomPortrait(model, Classes.CAP, "Knight");
                portraitSay("Stop! Horse Thief!");
                boolean gender = MyRandom.flipCoin();
                println("A " + (gender?"woman":"man") + " steps out of the brush and comes rushing toward you.");
                portraitSay("You think you can just take someone's horse? What's the matter with you?");
                boolean result = model.getParty().doSoloSkillCheck(model, this, Skill.Persuade, 8);
                if (result) {
                    println("You manage to calm the knight down and convince " + himOrHer(gender) +
                            " that you honestly thought the horse was a stray.");
                    portraitSay("Hmph, well, even knights have to step down from their horses when nature calls...");
                    println("The knights mounts " + hisOrHer(gender) + " horse and rides away.");
                } else {
                    println("Your attempts to calm down the knight have just made " + himOrHer(gender) + " angrier. " +
                            heOrSheCap(gender) + " attacks you!");
                    runCombat(List.of(new SoldierEnemy('A')));
                    if (!haveFledCombat()) {
                        println("The knight lay slain on the ground. Unfortunately, " + hisOrHer(gender) +
                                " mount has run away while you were fighting.");
                    }
                }
            } else {
                model.getParty().getHorseHandler().addHorse(horse);
                println("The party got a " + horse.getName() + ".");
            }
        } else {
            leaderSay("Maybe your master is just relieving " + himOrHer(MyRandom.flipCoin()) + "self in the bushes nearby.");
            println("You leave the horse grazing by the side of the road.");
        }
    }
}
