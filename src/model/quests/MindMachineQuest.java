package model.quests;

import model.Model;
import model.achievements.Achievement;
import model.items.spells.TeleportSpell;
import model.map.WorldBuilder;
import model.states.GameState;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.CollapsingTransition;
import view.subviews.MapSubView;
import view.subviews.TeleportBetweenWorldsTransition;
import view.subviews.TeleportingTransition;
import view.widget.QuestBackground;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MindMachineQuest extends MainQuest {
    public static final String QUEST_NAME = "Mind Machine";
    private static final String INTRO_TEXT = "You find yourself back at the sewage pipe from you once escaped. " +
            "Once aside, a labyrinth of watery passageways lay between you and the castle dungeons. " +
            "If you can find the way through you may be able to get into the castle, but what will you find inside?";

    private static final String ENDING_TEXT = "TODO Ending";
    private List<QuestBackground> backgroundSprites = makeBackgroundSprites();

    public MindMachineQuest() {
        super(QUEST_NAME, "Yourself", QuestDifficulty.VERY_HARD,
                new Reward( 0, 0), 0, INTRO_TEXT, ENDING_TEXT);
    }

    @Override
    public Achievement.Data getAchievementData() {
        return makeAchievement(this, "TODO");
    }

    @Override
    protected void resetQuest() {
        super.resetQuest();
        getSuccessEndingNode().move(6, 0);
    }

    @Override
    public MainQuest copy() {
        return new MindMachineQuest();
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of();
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new MindMachineStartingPoint(new QuestEdge(getSuccessEndingNode(), QuestEdge.VERTICAL)));
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {

    }

    @Override
    public GameState endOfQuest(Model model, QuestState state, boolean questWasSuccess) {
        GameState toIgnore = super.endOfQuest(model, state, questWasSuccess);
        teleportToOtherWorld(model, state, new Point(5, 5));
        return toIgnore; // TODO Fix
    }

    public static void teleportToOtherWorld(Model model, GameState state, Point destinationPosition) {
        MapSubView mapSubView = new MapSubView(model);
        CollapsingTransition.transition(model, mapSubView);
        state.println("Preparing to teleport, press enter to continue.");
        state.waitForReturn();
        TeleportBetweenWorldsTransition.transition(model, mapSubView, destinationPosition);
        state.println("Press enter to continue.");
        state.waitForReturn();
        CollapsingTransition.transition(model, model.getCurrentHex().getImageSubView(model));
    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.BLACK;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.darkQuestSong;
    }

    private static List<QuestBackground> makeBackgroundSprites() {
        List<QuestBackground> result = new ArrayList<>();
        for (int row = 0; row < 9; ++row) {
            for (int col = 0; col < 8; ++col) {
                Sprite spr =
                        new FlippedBgSprite("mindmachinebg"+row+":"+col, "castle.png",
                                row * 0x10 + col);

                MyColors.transformImage(spr);
                result.add(new QuestBackground(new Point(7 - col, row), spr));
            }
        }
        return result;
    }

    @Override
    public List<QuestBackground> getBackgroundSprites() {
        return backgroundSprites;
    }

    private class MindMachineStartingPoint extends QuestStartPointWithoutDecision{
        public MindMachineStartingPoint(QuestEdge questEdge) {
            super(questEdge, "");
            setColumn(6);
            setRow(8);
        }
    }

    private static class FlippedBgSprite extends Sprite32x32 {
        public FlippedBgSprite(String name, String map, int num) {
            super(name, map, num, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE);
            setFlipHorizontal(true);
        }
    }
}
