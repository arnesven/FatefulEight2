package model.states.events;

import model.Model;
import model.enemies.BanditEnemy;
import model.items.Item;
import model.items.Prevalence;
import model.states.DailyEventState;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HideoutEvent extends DailyEventState {
    public HideoutEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You start up a tunnel when suddenly you hear voices. You creep closer to try to overhear the conversation.");
        println("From the way the inhabitants are talking it's clear that they are a bandit gang.");
        model.getParty().randomPartyMemberSay(model, List.of("What do we do now?"));
        int result = multipleOptionArrowMenu(model, 26, 20,
                List.of("Attack the bandits", "Approach the bandits", "Go back"));
        if (result == 0) {
            attackBandits(model);
        } else if (result == 1) {
            approachBandits(model);
        } else {
            println("You decide not to interact with the bandits. Instead, you go back the way you came.");
        }
    }

    private void approachBandits(Model model) {
        println("The bandits are alarmed that you've stumbled into their hideout. But they don't attack you outright. " +
                "Instead, they call for their leader. The bandit leader approaches you and takes a hard look at you.");
        int alignment = DailyEventState.calculatePartyAlignment(model, this);
        if (alignment >= 0) {
            println("Bandit Leader: \"Kill them all!\"");
            leaderSay("Bring it on!");
            attackBandits(model);
        } else {
            println("Bandit Leader: \"You look like the right kind of rascals, maybe we can help each other out.\"");
            leaderSay("What do you have in mind?");
            println("Bandit Leader: \"Well, you see, we've got this rare loot that we've boosted, but we're having trouble " +
                    "fencing it. You buy it from us, dirt cheap, then you can sell it!\"");
            if (model.getParty().getGold() < 10) {
                leaderSay("We're a bit short on cash...");
                reject();
            } else {
                int cost = (model.getParty().getGold() / 10) * 10;
                print("Buy the loot for " + cost + " gold? (Y/N) ");
                if (yesNoInput()) {
                    List<Item> items = generateLoot(model, cost);
                    model.getParty().addToGold(-cost);
                    println("The party pays " + cost + " gold.");
                    for (Item it : items) {
                        println("The party receives " + it.getName() + ".");
                        model.getParty().getInventory().addItem(it);
                    }
                    println("Bandit Leader: \"Oh, and if you want, you can use our secret passage to get to the nearest town.\"");
                    secretPassage(model);
                } else {
                    reject();
                }
            }
        }
    }

    private List<Item> generateLoot(Model model, int cost) {
        List<Item> result = new ArrayList<>();
        int sum = 0;
        while (sum < cost*3) {
            Item it = model.getItemDeck().draw(1, Prevalence.rare).get(0);
            result.add(it);
            sum += it.getCost();
        }
        return result;
    }

    private void reject() {
        println("Bandit Leader: \"Never mind then. Off you go you worthless sleazebags!\"");
    }

    private void attackBandits(Model model) {
        runCombat(List.of(new BanditEnemy('A'), new BanditEnemy('A'), new BanditEnemy('A'),
                new BanditEnemy('A'), new BanditEnemy('B', "Bandit Leader", 8)));
        println("After defeating the bandits, the party discovers a secret passage.");
        secretPassage(model);
    }

    private void secretPassage(Model model) {
        print("Do you take the secret passage? (Y/N) ");
        if (yesNoInput()) {
            model.getWorld().dijkstrasByLand(model.getParty().getPosition());
            List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle();
            forcedMovement(model, path);
            println("The secret passage took you to " + model.getCurrentHex().getPlaceName() + "!");
        }
    }
}
