package model.states.dailyaction.shops;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.states.GameState;
import model.states.events.GeneralInteractionEvent;
import util.MyLists;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.SplitPartySubView;
import view.subviews.StealingSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class BreakIntoShopState extends GameState {
    private final ShoppingNode shoppingNode;

    public BreakIntoShopState(Model model, ShoppingNode shoppingNode) {
        super(model);
        this.shoppingNode = shoppingNode;
    }

    @Override
    public GameState run(Model model) {
        print("The shop is closed. Do you want to try to break in? (Y/N) ");
        if (!yesNoInput()) {
            return null;
        }
        List<GameCharacter> groupB = null;
        List<Item> shopInventory = shoppingNode.getInventory();
        if (model.getParty().size() > 1) {
            print("Which party members should participate in the burglary (group B)? ");
            List<GameCharacter> groupA = new ArrayList<>(model.getParty().getPartyMembers());
            groupB = new ArrayList<>();
            SplitPartySubView split = new SplitPartySubView(model.getSubView(), groupA, groupB);
            SubView oldSubView = model.getSubView();
            model.setSubView(split);
            model.getTutorial().burglary(model);
            waitForReturnSilently();
            model.setSubView(oldSubView);
            if (groupB.isEmpty()) {
                println("Burglary cancelled.");
                return null;
            }
            model.getParty().benchPartyMembers(groupA);
        } else {
            model.getTutorial().burglary(model);
            groupB = new ArrayList<>(model.getParty().getPartyMembers());
        }
        boolean result = model.getParty().doSoloLockpickCheck(model, this, shoppingNode.getShopSecurity());
        int weightLimit = MyLists.intAccumulate(groupB,
                character -> character.getRace().getCarryingCapacity()*1000 - character.getEquipment().getTotalWeight());
        int accumulatedWeight = 0;
        if (result) {
            leaderSay("Okay, we're inside. Now let's gather up the booty!");
            if (shopInventory.isEmpty()) {
                partyMemberSay(groupB.get(0), "What, there's nothing here? Aaw... what a waste of time. Let's get out of here.");
                model.getParty().unbenchAll();
                return null;
            }
            StealingSubView newSubView = new StealingSubView(shopInventory, weightLimit);
            CollapsingTransition.transition(model, newSubView);
            int bounty = 0;
            while (true) {
                waitForReturnSilently();
                if (newSubView.getTopIndex() == 0) {
                    break;
                }
                if (accumulatedWeight >= weightLimit) {
                    println("You cannot carry any more loot!");
                } else {
                    Item it = newSubView.getSelectedItem();
                    println("You stole " + it.getName() + ".");
                    GameStatistics.incrementItemsStolen(1);
                    shopInventory.remove(it);
                    it.addYourself(model.getParty().getInventory());
                    newSubView.removeItem(it);
                    bounty++;
                    accumulatedWeight += it.getWeight();
                    newSubView.setBountyAndWeight(bounty, accumulatedWeight);
                    if (shopInventory.isEmpty()) {
                        break;
                    }
                }
            }
            partyMemberSay(groupB.get(0), "Now let's try not to be spotted on our way out.");
            result = model.getParty().doCollectiveSkillCheck(model, this, Skill.Sneak, Math.max(1, bounty/2));
            if (!result) {
                printAlert("Your crime has been witnessed.");
                GeneralInteractionEvent.addToNotoriety(model, this, bounty * 10);
            }
            if (MyRandom.rollD10() < bounty - 1) {
                println("The " + shoppingNode.getName() + " has gone out of business!");
                shoppingNode.setOutOfBusiness(model);
            }
        } else {
            result = model.getParty().doCollectiveSkillCheck(model, this, Skill.Sneak, 2);
            if (!result) {
                printAlert("Your crime has been witnessed.");
                GeneralInteractionEvent.addToNotoriety(model, this, 10);
            }
        }
        model.getParty().unbenchAll();
        return null;
    }
}
