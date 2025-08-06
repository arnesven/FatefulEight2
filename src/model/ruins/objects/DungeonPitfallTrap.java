package model.ruins.objects;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import model.states.GameState;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.BlankSubView;
import view.subviews.CollapsingTransition;
import view.subviews.DungeonDrawer;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DungeonPitfallTrap extends CenterDungeonObject {
    private static final Sprite32x32[] SPRITES = new Sprite32x32[]{
            new Sprite32x32("holeUL", "dungeon.png", 0x34,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.BLACK),
            new Sprite32x32("holeUR", "dungeon.png", 0x35,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.BLACK),
            new Sprite32x32("holeLL", "dungeon.png", 0x36,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.BLACK),
            new Sprite32x32("holeLR", "dungeon.png", 0x37,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.BLACK),

    };

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return SPRITES[0];
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        drawer.register(SPRITES[3].getName(), new Point(xPos, yPos), SPRITES[3]);
        drawer.register(SPRITES[1].getName(), new Point(xPos, yPos-4), SPRITES[1]);
        drawer.register(SPRITES[2].getName(), new Point(xPos-4, yPos), SPRITES[2]);
        drawer.register(SPRITES[0].getName(), new Point(xPos-4, yPos-4), SPRITES[0]);
    }

    @Override
    public String getDescription() {
        return "A hole in the floor";
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        for (GameCharacter gc : new ArrayList<>(model.getParty().getPartyMembers())) {
            if (!model.getParty().getBench().contains(gc)) {
                SkillCheckResult result = gc.testSkillHidden(Skill.Acrobatics, 2, 0);
                if (!result.isSuccessful()) {
                    exploreRuinsState.partyMemberSay(gc, MyRandom.sample(List.of("I'm slipping, aaaah!", "I'm falling!",
                            "Help!", "Waaaagh!", "Woaaah!")));
                    if (checkForCatch(model, exploreRuinsState, gc)) {
                        continue;
                    }
                    exploreRuinsState.println(gc.getFirstName() + " has fallen down to the floor below.");

                    int remainingPartyMembers = model.getParty().size() - model.getParty().getBench().size();
                    if (remainingPartyMembers > 1) {
                        exploreRuinsState.print("Do you want to go on without " + GameState.himOrHer(gc.getGender()) + "? (Y/N) ");
                        if (exploreRuinsState.yesNoInput()) {
                            model.getParty().benchPartyMembers(List.of(gc));
                            checkForLeaderChange(model, exploreRuinsState, gc);
                            exploreRuinsState.leaderSay(
                                    MyRandom.sample(List.of("We'll just have to meet up with " + gc.getFirstName() +
                                    " when this is over.", GameState.heOrSheCap(gc.getGender()) + "'ll be fine, I'm sure.",
                                    "See you later " + gc.getFirstName() + ".")));
                        } else {
                            exploreRuinsState.println("You climb down the hole after " + gc.getFirstName() + ".");
                            exploreRuinsState.partyMemberSay(gc, "Sorry! At least I'm not hurt.");
                            fallDown(model, exploreRuinsState);
                            return;
                        }
                    } else {
                        fallDown(model, exploreRuinsState);
                        return;
                    }
                }
            }
        }
    }

    private boolean checkForCatch(Model model, ExploreRuinsState exploreRuinsState, GameCharacter victim) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!model.getParty().getBench().contains(gc)) {
                if (victim != gc && gc.getRankForSkill(Skill.Acrobatics) > 3) {
                    int dieRoll = MyRandom.rollD6();
                    if (dieRoll > 3) {
                        exploreRuinsState.partyMemberSay(gc, MyRandom.sample(List.of(victim.getFirstName() + "!", "Watch it!", "Watch out!", "Stop!")));
                        exploreRuinsState.println(victim.getFirstName() + " was just about to fall down the " +
                                "hole, but " + gc.getFirstName() + " caught hold of " + GameState.hisOrHer(victim.getGender()) + " just in time!");
                        exploreRuinsState.partyMemberSay(victim, MyRandom.sample(List.of("Thank you!3", "I owe you one.",
                                "You saved me!3", "Wow, that was a close one!", "I nearly soiled myself.")));
                        victim.addToAttitude(gc, 5);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void checkForLeaderChange(Model model, ExploreRuinsState exploreRuinsState, GameCharacter gc) {
        if (gc == model.getParty().getLeader()) {
            for (GameCharacter gc2 : model.getParty().getPartyMembers()) {
                if (!model.getParty().getBench().contains(gc2)) {
                    model.getParty().setLeader(gc2);
                    exploreRuinsState.println(gc2.getName() + " is now the leader of the party.");
                    break;
                }
            }
        }
    }

    private void fallDown(Model model, ExploreRuinsState exploreRuinsState) {
        int levelNumber = exploreRuinsState.getCurrentLevel()+1;
        DungeonLevel downOne = exploreRuinsState.getDungeon().getLevel(levelNumber);

        Point current = exploreRuinsState.getPartyPosition();
        DungeonRoom room = null;
        if (current.x < downOne.getRooms().length && current.y < downOne.getRooms()[0].length) {
            room = downOne.getRooms()[current.x][current.y];
        }
        if (room == null) {
            current = downOne.getDescentPoint();
        }

        SubView subView = model.getSubView();
        CollapsingTransition.transition(model, new BlankSubView(subView));
        exploreRuinsState.movePartyToRoom(model, levelNumber, current);
        CollapsingTransition.transition(model, subView);
        exploreRuinsState.getCurrentRoom().entryTrigger(model, exploreRuinsState);
    }

    public static void makePitfallTrap(DungeonRoom room, Random random) {
        room.clearDecorations();
        room.addObject(new DungeonPitfallTrap());
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.println("Don't look down.");
    }
}
