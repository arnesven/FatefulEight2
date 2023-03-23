package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.items.weapons.RustyPickaxe;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class MineEvent extends DailyEventState {
    public MineEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model,
                List.of("That looks like an old mine over there in the hillside."));
        int bonus = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getCharClass().id() == Classes.MIN.id() || gc.getRace() == Race.DWARF) {
                model.getParty().partyMemberSay(model, gc, "Looks interesting! We should explore it!");
                bonus = 1;
            }
        }
        if (bonus == 0) {
            model.getParty().randomPartyMemberSay(model,
                    List.of("Entering could be perilous, but could also yield rewards..."));
        }
        print("Do you go inside the mine? (Y/N) ");
        if (!yesNoInput()) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Let's just continue on our journey.");
            return;
        }
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Bring out your torches, we're going down there.");

        int roll = MyRandom.rollD10() + bonus;
        int gold = 35;
        int difficulty = 10;
        switch (roll) {
            case 1 :
                // TODO: Goblins
            case 2:
                // TODO: Spiders
            case 3:
                println("The mine seems to be abandoned.");
                DeadBodyEvent dbe = new DeadBodyEvent(model);
                dbe.run(model);
                break;
            case 4:
                println("The mine seems to be abandoned.");
                model.getParty().randomPartyMemberSay(model, List.of("What's that terrible smell! GAS!"));
                println("Each party member suffers 2 damage.");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    gc.addToHP(-2);
                    if (gc.isDead()) {
                        model.getParty().remove(gc, true, false, 0);
                        println("The gas has killed " + gc.getName() + "!");
                    }
                }
                break;
            case 5:
            case 6:
                print("The mine is currently in use. There are some miners here but they seem to completely ignore you.");
                model.getParty().randomPartyMemberSay(model, List.of("Nobody will mind if we just help ourselves to some " +
                        "of these materials.", "We might as well pick up some stuff while we're here."));
                println("The party gains 5 materials.");
                model.getParty().getInventory().addToMaterials(5);
            case 7:
                print("The mine is currently in use. ");
                new MinerEvent(model).doEvent(model);
                break;
            case 8:
                gold = 20;
                difficulty = 8;
            case 9:
                println("The mine is currently in use. The party spends some time snooping around and even find a few pick axes laying about.");
                model.getParty().randomPartyMemberSay(model, List.of("We might as well do some mining while we are here."));
                int axes = MyRandom.randInt(2, 4);
                for (int i = axes; i > 0; --i) {
                    model.getParty().getInventory().add(new RustyPickaxe());
                }
                println("The party finds " + axes + " Rusty Pickaxes.");
                boolean success = model.getParty().doCollaborativeSkillCheck(model, this, Skill.Labor, difficulty);
                if (success) {
                    println("The party gains " + gold + " gold!");
                    model.getParty().addToGold(gold);
                }
                break;
            default: // 10
                new CaveEvent(model).doEvent(model);
        }
        model.setGameOver(model.getParty().isWipedOut());
        if (!model.isInCaveSystem()) {
            println("The party exits the mine.");
        }
    }
}
