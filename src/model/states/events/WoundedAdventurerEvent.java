package model.states.events;

import model.Model;
import model.RecruitInfo;
import model.RecruitableCharacter;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.items.Equipment;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.RecruitState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class WoundedAdventurerEvent extends DailyEventState {
    public WoundedAdventurerEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "somebody lying down, perhaps a dead body";
    }

    @Override
    protected void doEvent(Model model) {
        boolean gender = MyRandom.randInt(2) == 0;
        GameCharacter guy = MyRandom.sample(model.getAvailableCharactersByGender(gender));
        guy.setLevel(3);
        guy.setEquipment(new Equipment(model.getItemDeck().getRandomWeapon(), model.getItemDeck().getRandomApparel(), null));

        showSilhouettePortrait(model, "Wounded Adventurer");
        showEventCard("Wounded Adventurer", "The party finds an adventurer sitting on the ground. " +
                "With one hand on " + hisOrHer(gender) + " abdomen and the other clutching " +
                "a weapon, it is clear that something gruesome transpired " +
                "here.");
        if (model.getParty().size() > 1) {
            model.getParty().randomPartyMemberSay(model, List.of("Is " + heOrShe(gender) + " dead?"));
            model.getParty().randomPartyMemberSay(model,
                    List.of("No, " + heOrShe(gender) + "'s still breathing. Wait... I think " +
                            heOrShe(gender) + "'s trying to say something."));
        } else {
            println(heOrSheCap(gender) + " appears to be alive, but in bad shape.");
        }
        printQuote("Wounded Adventurer", "Water, please...");
        String still = "";
        do {
            if (still.equals("")) {
                boolean didSay = randomSayIfPersonality(PersonalityTrait.cold, List.of(model.getParty().getLeader()),
                        "Honestly, I think " + heOrShe(gender) + "'s done for. Let's just leave " + himOrHer(gender) + ".");
                if (didSay) {
                    randomSayIfPersonality(PersonalityTrait.benevolent, new ArrayList<>(),
                            "What? We can't do that!");
                }
                randomSayIfPersonality(PersonalityTrait.generous, List.of(model.getParty().getLeader()),
                        "We should at least leave some water and food.");
            }
            print("The wounded adventurer is " + still + "too weak to travel. Do you stay here tomorrow and help " +
                    himOrHer(gender) + " recover? (Y/N) ");
            still = "still ";
            if (yesNoInput()) {
                if (model.getParty().getFood() > 0) {
                    println("You give the adventurer food and water, and tend to " + hisOrHer(gender) + " wounds.");
                    model.getParty().addToFood(-1);
                } else {
                    println("You tend to the adventurer's wounds.");
                }
                new EveningState(model, false, false, false).run(model);
                setCurrentTerrainSubview(model);
                println("You spend the day trying to help the wounded adventurer recover.");
                if (MyRandom.randInt(2) == 0) {
                    println("The wounded adventurer recovered!");
                    printQuote("Wounded Adventurer", "Thank you for helping me. I'll gladly offer my services!");
                    model.getLog().waitForAnimationToFinish();
                    RecruitState recruitState = new RecruitState(model,
                            RecruitableCharacter.makeOneRecruitable(guy, RecruitInfo.none));
                    recruitState.run(model);
                    break;
                }
            } else {
                println("You leave the wounded adventurer to " + hisOrHer(gender) + " fate.");
                model.getParty().randomPartyMemberSay(model,
                        List.of("I don't feel good about this.",
                                "Poor fellow, probably won't make it.",
                                "Let's not end up like " + himOrHer(gender) + ", okay?"));
                break;
            }
        } while (true);
    }

}
