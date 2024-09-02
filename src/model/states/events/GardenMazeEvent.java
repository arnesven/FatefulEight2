package model.states.events;

import model.Model;
import model.states.DailyEventState;
import model.states.maze.GardenMaze;
import view.subviews.CollapsingTransition;
import view.subviews.GardenMazeSubView;

import java.awt.*;

public class GardenMazeEvent extends DailyEventState {

    private static final String[] MAZE_PLAN = new String[]{
            "   # # # # #   ",
            " +#+#+#+#+#+ + ",
            "       # #   # ",
            " +#+#+#+#+#+ + ",
            "       # #   # ",
            " +#+#+#+#+#+ + ",
            "       # #   # ",
            " +#+#+ +#+#+ + ",
            "   #     # # # ",
            " +#+#+ +#+#+ + ",
            "             # ",
    };

    private Point currentPoint = new Point(0, 0);

    public GardenMazeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        GardenMaze maze = GardenMaze.generate(10, 10);
        GardenMazeSubView subView = new GardenMazeSubView(maze, currentPoint);

        CollapsingTransition.transition(model, subView);
        do {
            waitUntil(subView, GardenMazeSubView::isDone);
            if (subView.statueFound()) {
                leaderSay("There's the statue.");
                println("Press enter to continue");
            }
            waitForReturnSilently();
            if (subView.getBorderIndex() == 1) {
                leaderSay("Ach, this is to tough. Let's give up.");
                break;
            } else if (subView.getBorderIndex() == 0) {
                subView.setShowMap(true);
            } else {
                subView.setShowMap(false);
            }
        } while (true);
        setCurrentTerrainSubview(model);
    }

}
