package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.items.Item;
import model.items.Prevalence;
import model.tasks.AssassinationDestinationTask;
import util.MyPair;
import util.MyRandom;

import java.util.List;

public class ThiefAssassinationEndingEvent extends AssassinationEndingEvent {
    public ThiefAssassinationEndingEvent(Model model) {
        super(model, false,3);
    }

    @Override
    protected Ending enterHomePartTwo(Model model, AssassinationDestinationTask task, String place) {
        MyPair<SkillCheckResult, GameCharacter> result = doPassiveSkillCheck(Skill.Perception, 10);
        if (result.first.isSuccessful()) {
            println(result.second.getFirstName() + " spots a trap right inside the door (Perception " + result.first.asString() + ").");
            leaderSay("This " + getRaceName(task) + " has taken some precautions against intruders. Let's just step around this trap.");
            return sneakAroundInside(model, task, place);
        }
        println("As soon you step into the " + place + ", something snaps and many projectiles are launched at you!");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.addToHP(-2);
            if (gc.isDead()) {
                characterDies(model, this, gc, " has been killed by a trap!", true);
            } else {
                partyMemberSay(gc, MyRandom.sample(List.of("Ouch!", "Ooof", "Ai, that hurt.", "I'm hit!")));
            }
        }

        println("You thoroughly search the " + place + " but " + task.getWrit().getName() + " is nowhere to be found.");
        boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Search, 9);
        if (success) {
            println("However, you do find a trove of what appears to be stolen loot in the house!");
            for (Prevalence prev : List.of(Prevalence.common, Prevalence.uncommon, Prevalence.rare)) {
                for (Item it : model.getItemDeck().draw(4, prev)) {
                    println("You found " + it.getName() + ".");
                    it.addYourself(model.getParty().getInventory());
                }
            }
            super.findSpareChange(model);
        }
        leaderSay("Darn it. We must have missed " + himOrHer(task.getWrit().getGender()) + ".");
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, heOrSheCap(task.getWrit().getGender()) +
                    " probably bolted out of here when the trap was sprung.");
        }
        return Ending.failure;
    }
}
