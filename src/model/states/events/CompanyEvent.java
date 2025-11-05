package model.states.events;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.BanditEnemy;
import model.enemies.Enemy;
import model.enemies.SoldierEnemy;
import model.enemies.SoldierLeaderEnemy;
import model.items.accessories.ShieldItem;
import model.items.special.TentUpgradeItem;
import model.states.CombatEvent;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class CompanyEvent extends DailyEventState {
    public CompanyEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a company of soldiers";
    }

    @Override
    protected void doEvent(Model model) {
        leaderSay("Hmm... That looks like a company of soldiers up ahead.");
        print("Do you get get off the road? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().setOnRoad(false);
            println("You quickly scurry off before the company approaches.");
            setFledCombat(true);
            return;
        }
        showRandomPortrait(model, Classes.CAP, "Soldiers");
        println("It is indeed a large company of soldiers. They " +
                "seem friendly and so you decide to share your " +
                "evening with them. The company has an abundance of " +
                "beer, but lack food and hope that you can share some " +
                "of your rations. Later on, they get rowdy and want the " +
                "party members to join in their degrading games.");

        randomSayIfPersonality(PersonalityTrait.irritable, new ArrayList<>(),
                "These are the most irritating bunch I've met in a while.");

        if (model.getParty().size() > 1) {
            GameCharacter gc1 = MyRandom.sample(model.getParty().getPartyMembers());
            GameCharacter gc2 = null;
            do {
                gc2 = MyRandom.sample(model.getParty().getPartyMembers());
            } while (gc1 == gc2);
            model.getParty().partyMemberSay(model, gc1, "We shouldn't have set up camp next to this rowdy bunch.");
            model.getParty().partyMemberSay(model, gc2, "Needless to say, they aren't good company.");
        }
        int soldiers = model.getParty().partyStrength() / 12 + 2;
        int loss = Math.min(soldiers + 1, model.getParty().getFood());

        println("Do you accept losing " + loss +
                " rations and indulge in their tiresome games, or do you handle the situation in another way?");
        int selectedChoice = multipleOptionArrowMenu(model, 24, 24, List.of("Give in to soldiers",
                "Distract with entertainment", "Beat them up", "Run away"));

        if (selectedChoice == 0) {
            foodAndSpLoss(model, loss);
            possiblyGetTent(model);
        } else if (selectedChoice == 1) {
            tryEntertainment(model, loss);
            possiblyGetTent(model);
        } else if (selectedChoice == 2) {
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < soldiers - 2; i++){
                enemies.add(new SoldierEnemy('A'));
            }
            enemies.add(new SoldierLeaderEnemy('B'));
            enemies.add(new SoldierLeaderEnemy('B'));
            runCombat(enemies);
            possiblyGetHorsesAfterCombat("soldiers", soldiers);
        } else {
            println("You quickly scurry away from the company of soldiers.");
            setFledCombat(true);
        }
    }

    private void possiblyGetTent(Model model) {
        if (model.getParty().getInventory().getTentSize() < Party.MAXIMUM_PARTY_SIZE) {
            if (MyRandom.flipCoin()) {
                println("The sloppy soldiers leave some gear behind. The party finds a tent!");
                model.getParty().addToInventory(new TentUpgradeItem());
            }
        }
    }

    private void tryEntertainment(Model model, int loss) {
        if (model.getParty().doCollaborativeSkillCheck(model, this, Skill.Entertain, 8)) {
            println("You quickly improvise a little play and musical number, full of slap-stick humor and " +
                    "inappropriate innuendo. The company of soldiers appreciate it greatly and forget about " +
                    "their own stupid games.");
            if (model.getParty().size() > 1) {
                leaderSay("Good work team!");
            }
        } else {
            println("The company of soldiers is outraged by your poor performance and start throwing bottles and " +
                    "other trash at you.");
            portraitSay("Amateurs! What a shoddy performance!");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                if (MyRandom.randInt(3) > 0) {
                    String thing = MyRandom.sample(List.of("shoe", "rotten apple", "rock",
                            "bottle", "leather strap", "stick"));
                    print("Somebody throws a " + thing + " at " + gc.getName() + "! ");
                    if (MyRandom.rollD10() <= gc.getSpeed()) {
                        println("But " + heOrShe(gc.getGender()) + " quickly " +
                                MyRandom.sample(List.of("dodges it.", "evades it.",
                                        "moves out of the way.", "ducks.")));
                    } else if (gc.getEquipment().getAccessory() instanceof ShieldItem &&
                            MyRandom.rollD10() <= ((ShieldItem)gc.getEquipment().getAccessory()).getBlockChance()) {
                        println("Clonk! The " + thing + " bounces off of " + gc.getFirstName() + "'s shield.");
                    } else {
                        println("It strikes " + himOrHer(gc.getGender()) + " right in the face.");
                        println(gc.getFirstName() + " takes 1 damage.");
                        gc.addToHP(-1);
                        if (gc.isDead()) {
                            characterDies(model, this, gc,
                                    " has been killed by the flying " + thing + "!", true);
                        } else {
                            partyMemberSay(gc, MyRandom.sample(List.of("Ow! That hurt", "Hey!", "What the heck?",
                                    "Ouch! Cut it out!")));
                        }
                    }
                }
            }
            if (!model.getParty().isWipedOut()) {
                leaderSay("I guess we'd better think of something else.");
                foodAndSpLoss(model, loss);
            }
        }
    }

    private void foodAndSpLoss(Model model, int loss) {
        model.getParty().addToFood(-loss);
        MyLists.forEach(model.getParty().getPartyMembers(), (GameCharacter gc) -> gc.addToSP(-1));
        println("You grudgingly hand over your packs of food and take part in the shenanigans.");
        println("Each party member exhausts 1 SP.");
    }
}
