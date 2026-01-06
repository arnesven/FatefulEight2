package model.quests;

import model.Model;
import model.mainstory.jungletribe.RubiqPuzzleEvent;
import model.quests.scenes.StoryJunctionWithEvent;
import model.states.DailyEventState;
import model.states.QuestState;
import sound.BackgroundMusic;
import view.MyColors;

import java.awt.*;
import java.util.List;

public abstract class SeekTheJadeCrownQuest extends RemotePeopleQuest {
    private static final String INTRO_TEXT = "TODO INTRO";
    private static final String ENDING_TEXT = "TODO ENDING";

    private final String pyramidName;
    private final Point pyramidLocation;

    public SeekTheJadeCrownQuest(String pyramidName, Point pyramidLocation) {
        super(getQuestName(pyramidName), "UNUSED", QuestDifficulty.VERY_HARD, new Reward(200), 3, INTRO_TEXT, ENDING_TEXT);
        this.pyramidName = pyramidName;
        this.pyramidLocation = pyramidLocation;
    }

    @Override
    public void setRemoteLocation(Model model) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), false);
        setRemotePath(model, model.getWorld().shortestPathToPoint(pyramidLocation));
    }

    public static String getQuestName(String pyramidName) {
        return "Search " + pyramidName + " Pyramid";
    }

    public String getPyramidName() {
        return pyramidName + " Pyramid";
    }

    @Override
    protected List<QuestScene> buildScenes() {
        return List.of();
    }

    @Override
    protected List<QuestJunction> buildJunctions(List<QuestScene> scenes) {
        return List.of(new StoryJunctionWithEvent(3, 3, new QuestEdge(getSuccessEndingNode())) {
            @Override
            public DailyEventState makeEvent(Model model, QuestState state) {
                return new RubiqPuzzleEvent(model);
            }
        });
    }

    @Override
    protected void connectScenesToJunctions(List<QuestScene> scenes, List<QuestJunction> junctions) {

    }

    @Override
    public MyColors getBackgroundColor() {
        return MyColors.GREEN;
    }

    @Override
    public BackgroundMusic getMusic() {
        return BackgroundMusic.mysticSong;
    }
}
