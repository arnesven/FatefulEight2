package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;
import util.MyRandom;

public class FoodStandsEvent extends DailyEventState {
    public FoodStandsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().getGold() >= 2) {
            print("There are food stands here. The vendors are selling some kind of " +
                    "fried bread and sugary treats. Do you pay 2 gold for your entire party? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-2);
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    int increase = MyRandom.randInt(0, 2);
                    gc.addToAttitude(model.getParty().getLeader(), increase);
                    print(gc.getFirstName() + " ");
                    switch (increase) {
                        case 2:
                            println("appreciates the snacks a lot!");
                            break;
                        case 1:
                            println("appreciates the snacks.");
                            break;
                        default:
                            println("doesn't really appreciate the snacks.");
                    }
                }
                showPartyAttitudesSubView(model);
            }
        } else {
            println("There are food stands here, but you cannot afford to buy any snacks right now.");
        }
    }
}