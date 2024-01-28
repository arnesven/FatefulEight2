package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.Enemy;
import model.enemies.OrcChieftain;
import model.enemies.OrcWarrior;
import model.enemies.TrollEnemy;
import model.items.Equipment;
import model.items.Item;
import model.items.Prevalence;
import model.states.DailyEventState;
import model.states.RecruitState;
import model.states.ShopState;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;

import java.util.ArrayList;
import java.util.List;

public class OrcishStrongholdEvent extends DailyEventState {
    public OrcishStrongholdEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("Hold up a minute. That's some kind of fort or something up there."));
        model.getParty().randomPartyMemberSay(model, List.of("An orcish stronghold... Could be dangerous."));
        print("Investigate the orcish stronghold? (Y/N) ");
        if (yesNoInput()) {
            println("The party creeps up to a ridge overlooking the orcish stronghold.");
            leaderSay("Can we see how many warriors they have?");
            int warriors = MyRandom.randInt(2,12);
            boolean troll = MyRandom.randInt(2) == 0;
            assessStrongholdStrength(model, warriors, troll);
            model.getLog().waitForAnimationToFinish();
            randomSayIfPersonality(PersonalityTrait.diplomatic, List.of(model.getParty().getLeader()),
                    "Maybe we should try to approach them? Perhaps they would like to trade?");
            randomSayIfPersonality(PersonalityTrait.cowardly, List.of(model.getParty().getLeader()),
                    "Why take the risk in staying here any longer than we have to?");
            println("What do you do?");
            int choice = multipleOptionArrowMenu(model, 34, 18, List.of("Attack", "Approach", "Leave"));
            if (choice == 0) {
                attack(model, warriors, troll);
            } else if (choice == 1) {
                if (new OrcsEvent(model).interactWithOrcs(model)) {
                    tradeWithOrcs(model);
                } else {
                    attack(model, warriors, troll);
                }
            } else {
                leave(model);
            }
        } else {
            leave(model);
        }
    }

    private void tradeWithOrcs(Model model) {
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Which one of you do you call your leader?");
        println("The orcs gruff a little amongst themselves, but after a little while a large one steps forward.");
        printQuote("Orc Chieftain", "Me Chief. What you want?");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Are you perhaps interested in trade?");
        printQuote("Orc Chieftain", "Trade good! Us need gold and materials.");
        waitForReturn();
        List<Item> items = model.getItemDeck().draw(MyRandom.randInt(4, 8), Prevalence.rare);
        items.addAll(model.getItemDeck().draw(MyRandom.randInt(4, 8), Prevalence.uncommon));
        int[] prices = new int[items.size()];
        for (int i = 0; i < items.size(); ++i) {
            prices[i] = items.get(i).getCost() / 3 + 3;
        }
        ShopState shopState = new ShopState(model, "Orc Stronghold", items, prices);
        shopState.setSellingEnabled(false);
        shopState.run(model);

        if (model.getParty().getInventory().getMaterials() > 0) {
            println("The orcs are also interested in buying materials from you at a rate of 5 gold per material. Are you interested? (Y/N) ");
            while (yesNoInput()) {
                model.getParty().getInventory().addToMaterials(-1);
                model.getParty().addToGold(5);
                if (model.getParty().getInventory().getMaterials() == 0) {
                    break;
                }
                print("Do you want to sell more materials? (Y/N) ");
            }
        }

        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.BBN);
        print("The orcs would also inspire some of your party members to take on the class of barbarian, ");
        changeClassEvent.areYouInterested(model);
        println("You leave the orcish stronghold.");
    }

    private void attack(Model model, int warriors, boolean troll) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < warriors-1; ++i) {
            enemies.add(new OrcWarrior('A'));
        }
        if (troll) {
            enemies.add(new TrollEnemy('B'));
        }
        enemies.add(new OrcChieftain('C'));
        runCombat(enemies, false);


        print("You've sacked the orcish stronghold. ");
        int dieRoll = MyRandom.rollD10();
        if (dieRoll < 3) {
            WoundedAdventurerEvent wounded = new WoundedAdventurerEvent(model);
            wounded.doEvent(model);
        } else if (dieRoll < 6) {
            ChestEvent chest = new ChestEvent(model);
            chest.doEvent(model);
        } else {
            boolean gender = MyRandom.randInt(2) == 0;
            List<GameCharacter> list = new ArrayList<>();
            list.add(MyRandom.sample(model.getAvailableCharactersByGender(gender)));
            list.get(0).setLevel(4);
            list.get(0).setRandomStartingClass();
            list.get(0).setEquipment(new Equipment(model.getItemDeck().getRandomWeapon(), model.getItemDeck().getRandomApparel(), null));

            if (MyRandom.randInt(2) == 0) {
                list.add(MyRandom.sample(model.getAvailableCharactersByGender(!gender)));
                list.get(1).setLevel(3);
                list.get(1).setRandomStartingClass();
                list.get(1).setEquipment(new Equipment(model.getItemDeck().getRandomWeapon(), model.getItemDeck().getRandomApparel(), null));
                println("You have rescued two prisoners!");
                printQuote(list.get(1).getName(), "Thank you for rescuing us. We gladly offer our services!");
            } else {
                println("You have rescued a prisoner!");
                printQuote(list.get(0).getName(), "Thank you for rescuing me. I'll gladly offer my services!");
            }
            RecruitState recruitState = new RecruitState(model, list);
            recruitState.run(model);
        }
        println("You leave the orcish stronghold.");
    }

    private void leave(Model model) {
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Better not get too close. Let's take a detour and be on with our journey.");
    }

    private void assessStrongholdStrength(Model model, int warriors, boolean troll) {
        MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, this, Skill.Perception, 7);
        if (pair.first) {
            int spotted = Math.min(warriors, 5);
            model.getParty().partyMemberSay(model, pair.second, "I can distinguish " + MyStrings.numberWord(spotted) + " of them.");
            if (troll) {
                model.getParty().partyMemberSay(model, pair.second, "Oh, and they have a troll!");
            }
            if (spotted < warriors) {
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    SkillCheckResult result = gc.testSkill(Skill.Logic, 8);
                    if (result.isSuccessful()) {
                        println(gc.getFirstName() + " tests Logic " + result.asString() + ".");
                        model.getParty().partyMemberSay(model, gc, "Hmm. Based on the smoke coming from those chimneys " +
                                "I'm guessing there are at least a few more. I'd say " + MyStrings.numberWord(warriors) + " of them in total.");
                        break;
                    }
                }
            }
        } else {
            model.getParty().partyMemberSay(model, pair.second, "There are two or three of them maybe. I can't really distinguish anything else.");
        }
    }
}
