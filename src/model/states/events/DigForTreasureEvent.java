package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.states.DailyEventState;
import model.states.GameState;
import model.tasks.DestinationTask;
import model.tasks.TreasureHuntTask;
import util.MyLists;
import util.MyRandom;

import java.util.List;

public class DigForTreasureEvent extends DailyEventState {
    private TreasureHuntTask task = null;
    private boolean found = false;

    public DigForTreasureEvent(Model model) {
        super(model);
        for (DestinationTask dt : model.getParty().getDestinationTasks()) {
            if (dt instanceof TreasureHuntTask) {
                if (dt.getPosition().equals(model.getParty().getPosition()) && !model.isInCaveSystem()) {
                    this.task = (TreasureHuntTask) dt;
                    this.found = true;
                    break;
                }
            }
        }
    }

    @Override
    protected void doEvent(Model model) {
        println("You spend the whole day searching and digging for buried treasure.");
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 8);
        if (!success) {
            println("Unfortunately your efforts yield no result.");
        } else {
            if (found) {
                leaderSay("There's something here... it's a chest. It's the treasure!");
                if (model.getParty().size() > 1) {
                    GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
                    partyMemberSay(other, "Hurrah!");
                }
                println("You open the rather large chest.");
                List<Item> items = model.getItemDeck().draw(4, 0.5);
                for (Item it : items) {
                    println("You found " + it.getName() + ".");
                }
                int gold = MyRandom.randInt(100, 300);
                println("The party finds " + gold + " gold.");
                model.getParty().addToGold(gold);
                task.complete(model);
            } else {
                println("You can confidently say that if there were any treasure here, you would have found it.");
                leaderSay("The treasure must be somewhere else then.");
            }
        }
        println("Each party member exhausts 1 stamina point.");
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(-1));
    }
}
