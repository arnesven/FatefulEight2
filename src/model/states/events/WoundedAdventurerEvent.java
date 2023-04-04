package model.states.events;

import model.Model;
import model.characters.GameCharacter;
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
    protected void doEvent(Model model) {
        boolean gender = MyRandom.randInt(2) == 0;
        List<GameCharacter> list = new ArrayList<>();
        list.add(MyRandom.sample(model.getAvailableCharactersByGender(gender)));
        list.get(0).setLevel(3);
        list.get(0).setRandomStartingClass();
        list.get(0).setEquipment(new Equipment(model.getItemDeck().getRandomWeapon(), model.getItemDeck().getRandomApparel(), null));

        showSilhouettePortrait(model, "Wounded Adventurer");
        println("The party finds an adventurer sitting on the ground. " +
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
        println("Wounded Adventurer: \"Water, please...\"");
        String still = "";
        do {
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
                new EveningState(model, false, false).run(model);
                println("You spend the day trying to help the wounded adventurer recover.");
                if (MyRandom.randInt(2) == 0) {
                    println("The wounded adventurer recovered!");
                    println("Wounded Adventurer: \"Thank you for helping me. I'll gladly offer my services!\"");
                    RecruitState recruitState = new RecruitState(model, list);
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
