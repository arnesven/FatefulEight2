package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.enemies.*;
import model.states.DailyEventState;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MountedPatrolEvent extends DailyEventState {
    public MountedPatrolEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You spot a mounted patrol up ahead. They approach you.");
        int dieRoll = MyRandom.rollD6();
        if (dieRoll < 3) {
            doMountedBanditsEvent();
        } else if (dieRoll < 5) {
            doMountedMarshalsEvent(model);
        } else {
            doMountedSoldiersEvent(model);
        }
    }

    private void doMountedSoldiersEvent(Model model) {
        MyPair<SkillCheckResult, GameCharacter> pair = doPassiveSkillCheck(Skill.Perception, 7);
        if (pair.first.isSuccessful()) {
            partyMemberSay(pair.second, "Those are some mounted soldiers coming at us.");
            if (getOffTheRoad()) {
                return;
            } else {
                leaderSay("We have nothing to fear from them.");
            }
        }
        println("When the riders come closer you can clearly see that these are knights, " +
                "in the service of the lord of this kingdom.");
        println("The knights ride up to you. One of them asks politely for a few rations.");
        List<String> options = new ArrayList<>(List.of("Attack knights", "Refuse"));
        if (model.getParty().getFood() > 2) {
            options.add("Give 3 rations");
        }
        int choice = multipleOptionArrowMenu(model, 24, 25, options);
        if (choice == 2) {
            model.getParty().addToFood(-3);
            println("You hand over the food. The knights salute you and are on their way.");
            return;
        }
        if (choice == 1) {
            if (MyRandom.rollD6() < 4 || calculateAverageLevel(model) < 2.0) {
                println("The knights just angrily shake their fists. Then they ride off.");
                leaderSay("You should never give in to bullying.");
                return;
            }
        }
        knightsAttack();
    }

    private void knightsAttack() {
        println("The knights attack you!");
        List<Enemy> enemies = new ArrayList<>(List.of(new MountedEnemy(new SoldierEnemy('A')),
                new MountedEnemy(new SoldierEnemy('A')), new MountedEnemy(new SoldierEnemy('A'))));
        runCombat(enemies);
        possiblyGetHorsesAfterCombat("knights", 3);
    }

    private void doMountedMarshalsEvent(Model model) {
        MyPair<SkillCheckResult, GameCharacter> pair = doPassiveSkillCheck(Skill.Perception, 8);
        if (pair.first.isSuccessful()) {
            partyMemberSay(pair.second, "Those are mounted marshals coming at us.");
            if (model.getParty().getNotoriety() > 0) {
                leaderSay("Uh oh... And we happen to be a wanted bunch.");
                if (getOffTheRoad()) {
                    return;
                } else {
                    leaderSay("Let's see if they're brave enough to tussle with us!");
                }
            } else {
                leaderSay("Good thing we are such law-abiding denizens. We've got nothing to fear.");
            }
        }
        println("When the riders come closer you can clearly see that these are marshals, " +
                "agents of the local authority.");
        println("The marshals ride up to you.");
        ConstableEvent constableEvent = new ConstableEvent(model);
        if (constableEvent.constableAssess(model, "marshal", true)) {
            return;
        }
        print("The marshal is attempting to arrest you. Do you resist arrest? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("You're not taking us down today marshal!");
            List<Enemy> enemies = new ArrayList<>(List.of(new MountedEnemy(new ConstableEnemy('C')),
                    new MountedEnemy(new ConstableEnemy('C')), new MountedEnemy(new ConstableEnemy('C'))));
            runCombat(enemies);
            possiblyGetHorsesAfterCombat("marshals", 3);
        } else {
            goToJailInNearestTown(model, constableEvent);
        }
    }

    private void goToJailInNearestTown(Model model, ConstableEvent constableEvent) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition());
        List<Point> path = model.getWorld().shortestPathToNearestTownOrCastle();
        forcedMovement(model, path);
        println("You've been arrested and taken to " + model.getCurrentHex().getPlaceName() + "!");
        setCurrentTerrainSubview(model);
        constableEvent.goToJail(model);
    }

    private boolean getOffTheRoad() {
        print("Do you run away? (Y/N) ");
        if (yesNoInput()) {
            println("You quickly get off the road and hide in the nearest hiding place you can find. " +
                    "Either the patrol did not spot you, or decided not to pursue you.");
            setFledCombat(true);
            return true;
        }
        return false;
    }

    private void doMountedBanditsEvent() {
        MyPair<SkillCheckResult, GameCharacter> pair = doPassiveSkillCheck(Skill.Perception, 8);
        if (pair.first.isSuccessful()) {
            partyMemberSay(pair.second, "Those types look like bandits to me. Perhaps we should get off the road?");
            if (!getOffTheRoad()) {
                leaderSay("What cowardice is that? If they want a piece of us, they shall have it!");
            } else {
                return;
            }
        }
        println("When the riders come closer you can clearly see that they are bandits. They charge at you!");
        List<Enemy> enemies = new ArrayList<>(List.of(generateMountedBandit(),
                generateMountedBandit(), generateMountedBandit()));
        runCombat(enemies);
        possiblyGetHorsesAfterCombat("bandits", 3);
    }

    private Enemy generateMountedBandit() {
        if (MyRandom.rollD6() < 3) {
            return new MountedEnemy(new BanditArcherEnemy('A'));
        }
        return new MountedEnemy(new BanditEnemy('B'));
    }
}
