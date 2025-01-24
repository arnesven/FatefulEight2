package model.tasks;

import model.Model;
import model.Summon;
import model.characters.PersonalityTrait;
import model.classes.Skill;
import model.headquarters.Headquarters;
import model.map.UrbanLocation;
import model.tasks.SummonTask;

import java.util.ArrayList;
import java.util.List;

public class RestoreMansionTask extends SummonTask {
    private static final int MATERIALS_AND_GOLD_COST = 75;
    private final Summon summon;
    private final UrbanLocation location;

    public RestoreMansionTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        portraitSay("A much loved noble who lived in this town recently passed away.");
        leaderSay("My condolences.");
        portraitSay("Thank you. He was a close friend of mine. In his will, " +
                "he requested that his mansion should be given to me.");
        leaderSay("How nice for you. What's the problem?");
        portraitSay("The trouble is, he was quite old and had neglected to maintain the mansion for " +
                "many years. By now it is almost completely dilapidated. Restoring it will be quite costly. I can't afford it myself and I dare not " +
                "tap into the town's communal coffers, they are already stretched thin as it is.");
        leaderSay("Why not just tear it down?");
        portraitSay("Yes, that is one option. It would be a shame though. It was once a glorious residence, and a shining jewel of our town. " +
                "Since you seem like a capable group of good repute, I was thinking I could make a deal with you. " +
                "If you secure the resources needed for the restoration project I will gift you the residence, " +
                "provided you take care of it from then onward. Are you interested?");
        leaderSay("Perhaps. What quantities are we talking about?");
        portraitSay("I'm afraid it would be quite a lot. I would say " + MATERIALS_AND_GOLD_COST
                + " materials for restoring the structure and " +
                "as much gold for the work and other expenses.");
        boolean said = randomSayIfPersonality(PersonalityTrait.stingy, new ArrayList<>(), "What exactly are other expenses?");
        if (said) {
            portraitSay("There are numerous my friend! Hiring fees, permit costs, window-tax...");
            randomSayIfPersonality(PersonalityTrait.stingy, new ArrayList<>(), "Oh come on...");
        }
        println("How would you like to help the " + location.getLordTitle() + "?");
        int chosen = multipleOptionArrowMenu(model, 24, 24, List.of("Demolish mansion", "Pay materials and gold", "Not now"));
        if (chosen == 0) {
            leaderSay("Sounds like the cost would be far greater than the gain. But we will tear the building down for you.");
            portraitSay("Fair enough. Can you start work at once?");
            leaderSay("Of course!");
            portraitSay("Splendid. Follow me.");
            setCurrentTerrainSubview(model);
            println("The " + location.getLordTitle() + " leads you to the old mansion. It is indeed in a very bade state. " +
                    "Tearing it down should not be too difficult.");
            leaderSay("We'll get right to work then.");
            portraitSay("I'll be sad to see this place be torn apart. But I guess it's time.");
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, 8);
            if (success) {
                println("You spend the day hauling off old timber, plaster, planks, shingles and old furniture. " +
                        "When the job is done there is only an empty lot remaining.");
                portraitSay("Remarkable, it's gone. Well, here. For a job well done!");
                println("The party received 20 gold.");
                model.getParty().addToGold(20);
            } else {
                println("Despite your best efforts you have not been able to completely clear " +
                        "the building away. The " + location.getLordTitle() + " is not particularly impressed.");
                portraitSay("Well, you did some of the work at least. I shall have to hire another work force to complete " +
                        "the work.");
            }
            summon.increaseStep();
        } else if (chosen == 1) {
            leaderSay("That sounds like a good deal.");
            if (hasTheMoney(model)) {
                Headquarters mansion = new Headquarters(location, Headquarters.MAJESTIC_SIZE);
                if (model.getParty().getHeadquarters() != null) {
                    leaderSay("I have the materials and gold but...");
                    if (offerTransfer(model, mansion, location, "get the mansion")) {
                        getMansion(model, mansion);
                    } else {
                        leaderSay("I don't think I want to get rid of our other house.");
                        portraitSay("Alright. Will you sell me those materials for 200 gold?");
                        print("Sell 75 materials for 200 gold to the " + location.getLordTitle() + "? (Y/N) ");
                        if (yesNoInput()) {
                            leaderSay("That seems fair.");
                            println("You hand over 75 materials and receive 200 gold.");
                            model.getParty().addToGold(200);
                            model.getParty().getInventory().addToMaterials(-MATERIALS_AND_GOLD_COST);
                            summon.increaseStep();
                        } else {
                            leaderSay("Sorry. No deal.");
                            portraitSay("That's unfortunate.");
                        }
                    }
                } else {
                    getMansion(model, mansion);
                }
            } else {
                leaderSay("But we don't have the money right now. We'll have to come back later.");
                portraitSay("Oh, okay. There's no rush really.");
            }
        } else {
            leaderSay("I think we'll come back later.");
            portraitSay("Oh, okay. There's no rush really.");
        }
    }

    private void getMansion(Model model, Headquarters mansion) {
        leaderSay("Here are the materials and gold you asked for.");
        println("You hand over " + MATERIALS_AND_GOLD_COST + " materials and " + MATERIALS_AND_GOLD_COST + " gold");
        model.getParty().getInventory().addToMaterials(-MATERIALS_AND_GOLD_COST);
        model.getParty().addToGold(-MATERIALS_AND_GOLD_COST);
        portraitSay("Wonderful! I'll make all the arrangements then. Here are the keys to your new property!");
        model.getParty().setHeadquarters(model, this, mansion);
        summon.increaseStep();
    }

    private boolean hasTheMoney(Model model) {
        return model.getParty().getInventory().getMaterials() >= MATERIALS_AND_GOLD_COST && 
                model.getParty().getGold() >= MATERIALS_AND_GOLD_COST;
    }

    @Override
    public String getJournalDescription() {
        return heOrSheCap(location.getLordGender()) + " needs help restoring a run-down mansion to its former glory.";
    }
}
