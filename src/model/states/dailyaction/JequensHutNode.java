package model.states.dailyaction;

import model.Model;
import model.journal.PartSixStoryPart;
import model.journal.StoryPart;
import model.mainstory.GainSupportOfJungleTribeTask;
import model.mainstory.GainSupportOfVikingsTask;
import model.map.WorldBuilder;
import model.states.DailyEventState;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public class JequensHutNode extends DailyActionNode {

    private static final Sprite SPRITE = new Sprite32x32("jequenshut", "world_foreground.png", 0x9C,
            MyColors.BLACK, MyColors.DARK_GRAY, MyColors.DARK_GREEN, MyColors.BROWN);


    public JequensHutNode() {
        super("Jequen's Hut");
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        if (isMainStoryTriggered(model)) {
            GainSupportOfJungleTribeTask task = GainSupportOfJungleTribeTask.getTask(model);
            return task.generateJequenEvent(model);
        }
        state.println("Jequen isn't home right now.");
        return state;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) {

    }

    public static boolean isMainStoryTriggered(Model model) {
        if (model.getMainStory().getExpandDirection() == WorldBuilder.EXPAND_SOUTH) {
            List<StoryPart> storyParts = model.getMainStory().getStoryParts();
            StoryPart lastStoryPart = storyParts.get(storyParts.size() - 1);
            if (lastStoryPart instanceof PartSixStoryPart) {
                PartSixStoryPart partSix = (PartSixStoryPart) lastStoryPart;
                if (partSix.witchTalkedTo()) {
                    return !partSix.getRemotePeopleTask().isCompleted();
                }
            }
        }
        return false;
    }

}
