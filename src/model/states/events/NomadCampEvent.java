package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.NomadEnemy;
import model.enemies.WarriorNomadEnemy;
import model.items.Item;
import model.states.DailyEventState;
import model.states.ShopState;
import model.states.dailyaction.BuyHorseState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class NomadCampEvent extends DailyEventState {

    private boolean didCombat = false;

    private enum Attitude {
        Hostile,
        Neutral,
        Friendly
    }

    public NomadCampEvent(Model model) {
        super(model);
    }

    @Override
    public String getDistantDescription() {
        return "a nomad camp";
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Visit nomad camp", "There's a camp nearby. It's the home of a nomadic tribe");
    }

    @Override
    protected boolean isFreeRations() {
        return !didCombat;
    }

    @Override
    protected void doEvent(Model model) {
        CharacterClass cls = MyRandom.sample(List.of(Classes.AMZ, Classes.BBN));
        showRandomPortrait(model, cls, "Nomads");
        println("The party comes to a little community of tents and wagons. " +
                "These nomads travel around the countryside, following game or herds.");
        randomSayIfPersonality(PersonalityTrait.friendly, new ArrayList<>(), "We should approach them peacefully. " +
                "If we befriend them, we may all benefit.");
        int bonus = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getCharClass().id() == Classes.BBN.id() ||
                    gc.getCharClass().id() == Classes.AMZ.id()) {
                if (bonus == 0) {
                    model.getParty().partyMemberSay(model, gc, List.of("Hey, I know these folks.",
                            "Just let me do the talking here.", "Ah, nomads. Good people!3",
                            "A tribal community, I've known many of them.", "I feel right at home here."));
                }
                bonus++;
            }
        }

        int roll = MyRandom.rollD10() + bonus;
        Attitude attitude = roll < 5 ? Attitude.Hostile : (roll < 8 ? Attitude.Neutral : Attitude.Friendly);
        if (roll < 3 || roll == 5 || roll == 8) {
            println("This tribe is a warrior clan and appears to be " + attitude.toString().toLowerCase() + ".");
        } else if (roll % 3 == 0) {
            println("This tribe is a trader clan and appears to be " + attitude.toString().toLowerCase() + ".");
        } else {
            println("This tribe is a spiritual clan and appears to be " + attitude.toString().toLowerCase() + ".");
        }

        boolean isWarriorClan = (roll < 3 || roll == 5 || roll == 8);
        if (attitude == Attitude.Hostile) {
            leaderSay("They seem pretty aggressive. Maybe we can find some common ground.");
            randomSayIfPersonality(PersonalityTrait.aggressive, List.of(model.getParty().getLeader()),
                    "Or we can crack some skulls?");
            randomSayIfPersonality(PersonalityTrait.diplomatic, List.of(model.getParty().getLeader()),
                    "That sounds like a good idea. There must be some way of reasoning with them.");
            boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Persuade, 7);
            if (!success) {
                didCombat = true;

                List<Enemy> enemies = new ArrayList<>();
                int numEnemies = Math.max(3, model.getParty().partyStrength() / (new NomadEnemy('A')).getThreat());
                for (int i = 0; i < numEnemies; ++i){
                    if (isWarriorClan) {
                        enemies.add(new WarriorNomadEnemy('A'));
                    } else {
                        enemies.add(new NomadEnemy('A'));
                    }
                }
                runCombat(enemies);
                possiblyGetHorsesAfterCombat("nomads", 7);
                return;
            } else {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                        List.of("Ok, I think we've overcome the first cultural hurdle.",
                                "I think we've broken the ice.",
                                "They seem calmer."));
            }
        }

        if (roll % 3 == 0) {
            println("The nomads offer to sell you a horse.");
            BuyHorseState buyHorseState = new BuyHorseState(model, "Nomad");
            buyHorseState.setPrice(model.getParty().getHorseHandler().getAvailableHorse(model).getCost() +
                    MyRandom.randInt(-20, 10));
            buyHorseState.run(model);

            print("The nomads agree to trade with you. Press enter to see their wares. ");
            waitForReturn();
            List<Item> items = new ArrayList<>();
            items.addAll(model.getItemDeck().draw(6));

            int[] prices = null;
            if (attitude == Attitude.Friendly) {
                prices = new int[]{items.get(0).getCost() / 2, items.get(1).getCost() / 2,
                        items.get(2).getCost() / 2, items.get(3).getCost() / 2,
                        items.get(4).getCost() / 2, items.get(5).getCost() / 2};
            } else {
                prices = new int[]{items.get(0).getCost() / 2, items.get(1).getCost() / 2,
                        items.get(2).getCost() - 2, items.get(3).getCost() - 2,
                        items.get(4).getCost() + 10, items.get(5).getCost() + 10};
            }

            ShopState merchantShop = new ShopState(model, "Nomad Trader", items, prices, new boolean[]{true});
            merchantShop.setSellingEnabled(false);
            merchantShop.run(model);

        } else if (!isWarriorClan) {
            ChangeClassEvent change = new ChangeClassEvent(model, Classes.DRU);
            print("The nomads offer to teach you in their ancestral worship");
            if (attitude == Attitude.Friendly) {
                print(". This is an opportunity to learn druidism, ");
                change.areYouInterested(model);
            } else {
                print(" for a small fee of 10 gold. ");
                if (model.getParty().getGold() < 10) {
                    println("Since you do not have the money, you respectfully decline.");
                } else if (change.noOfCandidates() == 0) {
                    println("But nobody in your party seems interested, so you respectfully decline.");
                } else {
                    print("Do you pay (Y/N) ");
                    if (yesNoInput()) {
                        change.doEvent(model);
                    }
                }
            }
            print("Furthermore, the nomads also offer some of their special elixirs. Press enter.");
            waitForReturn();
            List<Item> items = new ArrayList<>(List.of(model.getItemDeck().getRandomPotion()));
            int[] prices = null;
            if (attitude == Attitude.Friendly) {
                prices = new int[]{0};
            } else {
                prices = new int[]{5};
            }
            ShopState merchantShop = new ShopState(model, "Nomad Trader", items, prices, new boolean[]{true});
            merchantShop.run(model);
        }

        println("The nomads invite you into their tents for the evening and you share a scrumptious and exotic supper.");

    }
}
