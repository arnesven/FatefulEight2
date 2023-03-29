package model.ruins;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.Random;
import java.util.List;

public class DungeonTrap extends CenterDungeonObject {
    private static final Sprite32x32 SPRITE = new Sprite32x32("spikes1", "dungeon.png", 0x53,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.DARK_GRAY);
    private static final Sprite32x32 HORI_PATH = new Sprite32x32("spikes2", "dungeon.png", 0x54,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.DARK_GRAY);
    private static final Sprite32x32 VERTI_PATH = new Sprite32x32("spikes3", "dungeon.png", 0x55,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.DARK_GRAY);
    private static final Sprite32x32 BOTH_PATH = new Sprite32x32("spikes4", "dungeon.png", 0x56,
            MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.PINK, MyColors.DARK_GRAY);

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(getSprite().getName(), new Point(xPos, yPos), getSprite());
        model.getScreenHandler().register(HORI_PATH.getName(), new Point(xPos, yPos-4), HORI_PATH);
        model.getScreenHandler().register(VERTI_PATH.getName(), new Point(xPos-4, yPos), VERTI_PATH);
        model.getScreenHandler().register(BOTH_PATH.getName(), new Point(xPos-4, yPos-4), BOTH_PATH);
    }

    @Override
    public String getDescription() {
        return "A trap";
    }

    @Override
    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            SkillCheckResult result = gc.testSkill(Skill.Acrobatics, 3);
            if (!result.isSuccessful()) {
                exploreRuinsState.println(gc.getName() + " got injured from the trap! (Acrobatics " + result.asString() + ")");
                gc.addToHP(-1);
                model.getParty().partyMemberSay(model, gc, List.of("Ouch", "Darn spikes!", "I'm bleeding!", "Yeouch!"));
            }
        }
    }

    public static void makeTrap(DungeonRoom room, Random random) {
        room.clearDecorations();
        room.addDecoration(new RoomDecoration(RoomDecoration.UPPER_LEFT, 4));
        room.addDecoration(new RoomDecoration(RoomDecoration.LOWER_LEFT, 4));
        room.addDecoration(new RoomDecoration(RoomDecoration.UPPER_RIGHT, 4));
        room.addDecoration(new RoomDecoration(RoomDecoration.LOWER_RIGHT, 4));
        room.addObject(new DungeonTrap());
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.println("Those spikes look dangerous.");
    }
}
