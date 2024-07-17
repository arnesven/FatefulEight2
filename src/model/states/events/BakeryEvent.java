package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.races.AllRaces;
import model.states.DailyEventState;
import view.subviews.SplitPartySubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class BakeryEvent extends DailyEventState {
    public BakeryEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit bakery", "The bakery in this town makes the loveliest pastries");
    }

    @Override
    protected void doEvent(Model model) {
        println("The party passes by a bakery.");
        partyMemberSay(model.getParty().getRandomPartyMember(), "That smells lovely!");
        showRandomPortrait(model, Classes.BAKER, "Baker");
        portraitSay("Yes, fresh out of the oven. But alas, today two of my three apprentices are home with the flu. " +
                "How will I ever get everything baked and delivered on time with only Freddy?");
        print("Do you offer to help the poor baker? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("I don't think we have time for this today.");
            return;
        }
        leaderSay("Sure, what can we do?");
        portraitSay("Well, you can either help me here with the baking (A), or you can run around town and make the deliveries (B). Naturally " +
                "it's best if we can perform both tasks evenly.");
        SubView oldSub = model.getSubView();
        List<GameCharacter> groupA = new ArrayList<>(model.getParty().getPartyMembers());
        List<GameCharacter> groupB = new ArrayList<>();
        model.setSubView(new SplitPartySubView(oldSub, groupA, groupB));
        waitForReturnSilently();
        model.setSubView(oldSub);
        String verb = "make";
        int points = 0;
        if (groupA.isEmpty()) {
            portraitSay("Nobody's going to help me bake? That's fine, I'll just keep Freddy here with me.");
            println("You spend the day running around town delivering the goods from the baker. You work up quite the sweat!");
            points = groupB.size() - failedDeliveries(model);
            verb = "deliver";
        } else if (groupB.isEmpty()) {
            portraitSay("Nobody's going to make deliveries? That's fine, Freddy can do those.");
            println("You spend the day in the bakery with the baker, together you make many loafs, cakes and pastries.");
            points = groupA.size() - failedBakings(model);
        } else {
            portraitSay("You're splitting up? Good, we'll do some good work today!");
            model.getLog().waitForAnimationToFinish();
            model.getParty().benchPartyMembers(groupB);
            println("One group spends the day baking with the baker.");
            int failedBakings = failedBakings(model);
            model.getLog().waitForAnimationToFinish();
            model.getParty().unbenchAll();
            model.getParty().benchPartyMembers(groupA);
            println("The other group spends the day delivering all the baked goods.");
            int failedDeliveries = failedDeliveries(model);
            model.getLog().waitForAnimationToFinish();
            model.getParty().unbenchAll();
            int balance = Math.min(groupA.size(), groupB.size()) + 1;
            points = ((groupA.size() - failedBakings) + (groupB.size() - failedDeliveries)) * balance;
        }
        if (points == 0) {
            portraitSay("Oh come on! Big strong adventurers like you, and you can't even " + verb + " some bread?");
            leaderSay("We tried...");
            portraitSay("You must try harder. Freddy really did all the work. " +
                    "I'm afraid I can only pay you a very small salary, " +
                    "since we really didn't make much money today.");
            points = 1;
        } else if (points < 7) {
            portraitSay("Good work today, here's your salary.");
        } else {
            portraitSay("Excellent work today! I think my apprentices could learn a thing or two from you! Here's your salary.");
        }

        int money = points;
        println("The party receives " + money + " gold.");
        model.getParty().addToGold(money);
        println("You say goodbye to the baker and Freddy.");
    }

    private int failedBakings(Model model) {
        List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Labor, 4);
        return failers.size();
    }

    private int failedDeliveries(Model model) {
        List<GameCharacter> failers = model.getParty().doCollectiveSkillCheckWithFailers(model, this, Skill.Endurance, 4);
        return failers.size();
    }
}
