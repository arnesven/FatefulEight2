package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import view.subviews.CaveTheme;
import model.enemies.Enemy;
import model.items.weapons.RustyPickaxe;
import model.races.Race;
import model.states.DailyEventState;
import util.MyRandom;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

import java.util.List;

public class MineEvent extends DailyEventState {

    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x12);

    public MineEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        boolean enteredFromSurface = true;
        if (model.isInCaveSystem()) {
            enteredFromSurface = false;
            model.getParty().randomPartyMemberSay(model, List.of("Looks like this cave is connected to a mine."));
        } else {
            model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Mine"));
            model.getParty().randomPartyMemberSay(model,
                    List.of("That looks like an old mine over there in the hillside."));
        }
        int bonus = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (gc.getCharClass().id() == Classes.MIN.id() || gc.getRace() == Race.DWARF) {
                model.getParty().partyMemberSay(model, gc, "Looks interesting! We should explore it!");
                bonus = 1;
            }
        }
        if (bonus == 0) {
            randomSayIfPersonality(PersonalityTrait.cowardly, List.of(model.getParty().getLeader()),
                    "Are we really going down there?");
            randomSayIfPersonality(PersonalityTrait.brave, List.of(model.getParty().getLeader()),
                    "Sometimes you have to be a little bold.");
            model.getParty().randomPartyMemberSay(model,
                    List.of("Entering could be perilous, but could also yield rewards..."));
        }
        print("Do you go inside the mine? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("Let's just continue on our journey.");
            return;
        }
        if (!model.isInCaveSystem()) {
            leaderSay("Bring out your torches, we're going down there.");
            randomSayIfPersonality(PersonalityTrait.anxious, List.of(model.getParty().getLeader()),
                    "Okay, but you first!");
        }
        int roll = MyRandom.rollD10() + bonus;
        int gold = 35;
        int difficulty = 10;
        int materials = 5;
        switch (roll) {
            case 1 :
                println("This mine is infested with goblins!");
                List<Enemy> enemies = GoblinsEvent.randomGoblins();
                runCombat(enemies, new CaveTheme(), true);
                break;
            case 2:
                println("This mine is infested with spiders!");
                runCombat(SpidersEvent.makeSpiders(), new CaveTheme(), true);
                break;
            case 3:
                println("The mine seems to be abandoned.");
                DeadBodyEvent dbe = new DeadBodyEvent(model);
                dbe.run(model);
                break;
            case 4:
                println("The mine seems to be abandoned.");
                model.getParty().randomPartyMemberSay(model, List.of("What's that terrible smell? GAS!"));
                println("Each party member suffers 2 damage.");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    gc.addToHP(-2);
                    if (gc.isDead()) {
                        model.getParty().remove(gc, true, false, 0);
                        printAlert("The gas has killed " + gc.getName() + "!");
                    }
                }
                break;
            case 5:
                materials += 5;
            case 6:
                print("The mine is currently in use. There are some miners here but they seem to completely ignore you.");
                model.getParty().randomPartyMemberSay(model, List.of("Nobody will mind if we just help ourselves to some " +
                        "of these materials.", "We might as well pick up some stuff while we're here."));
                println("The party gains " + materials + " materials.");
                model.getParty().getInventory().addToMaterials(materials);
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
                if (enteredFromSurface) {
                    new CaveEvent(model).doEvent(model);
                } else {
                    println("The mine is just abandoned and contains nothing of interest.");
                }
        }
        model.setGameOver(model.getParty().isWipedOut());
        if (!model.isInCaveSystem()) {
            println("The party exits the mine.");
        } else if (!enteredFromSurface) {
            print("Do you want to exit to the surface? (Y/N) ");
            if (yesNoInput()) {
                model.exitCaveSystem();
            } else {
                println("The party returns to the caves.");
            }
        }
    }
}
