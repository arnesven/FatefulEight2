package model.ruins.objects;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.ruins.DungeonRoom;
import model.ruins.themes.DungeonTheme;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.DungeonDrawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class DungeonSpikeTrap extends CenterDungeonObject {
    private static final Sprite32x32 SPRITE = new Sprite32x32("spikes1", "dungeon.png", 0x53,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.DARK_GRAY);
    private static final Sprite32x32 HORI_PATH = new Sprite32x32("spikes2", "dungeon.png", 0x54,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.DARK_GRAY);
    private static final Sprite32x32 VERTI_PATH = new Sprite32x32("spikes3", "dungeon.png", 0x55,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.DARK_GRAY);
    private static final Sprite32x32 BOTH_PATH = new Sprite32x32("spikes4", "dungeon.png", 0x56,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.DARK_GRAY);

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return SPRITE;
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        drawer.register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
        drawer.register(HORI_PATH.getName(), new Point(xPos, yPos-4), HORI_PATH);
        drawer.register(VERTI_PATH.getName(), new Point(xPos-4, yPos), VERTI_PATH);
        drawer.register(BOTH_PATH.getName(), new Point(xPos-4, yPos-4), BOTH_PATH);
    }

    @Override
    public String getDescription() {
        return "A trap";
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        for (GameCharacter gc : new ArrayList<>(model.getParty().getPartyMembers())) {
            SkillCheckResult result = gc.testSkillHidden(Skill.Acrobatics, 3, 0);
            if (!result.isSuccessful()) {
                exploreRuinsState.println(gc.getName() + " got injured from the trap! (Acrobatics " + result.asString() + ")");
                gc.addToHP(-1);
                if (gc.isDead()) {
                    DailyEventState.characterDies(model, exploreRuinsState, gc, " has died from the damage of the trap.",
                            true);
                } else {
                    model.getParty().partyMemberSay(model, gc, List.of("Ouch", "Darn spikes!", "I'm bleeding!", "Yeouch!"));
                }
            }
        }
    }

    public static void makeSpikeTrap(DungeonRoom room, Random random) {
        room.clearDecorations();
        room.addDecoration(new RoomDecoration(RoomDecoration.UPPER_LEFT, 4));
        room.addDecoration(new RoomDecoration(RoomDecoration.LOWER_LEFT, 4));
        room.addDecoration(new RoomDecoration(RoomDecoration.UPPER_RIGHT, 4));
        room.addDecoration(new RoomDecoration(RoomDecoration.LOWER_RIGHT, 4));
        room.addObject(new DungeonSpikeTrap());
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.println("Those spikes look dangerous.");
    }
}
