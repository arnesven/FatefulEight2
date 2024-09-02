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
        while (true) {
            waitForReturnSilently();
            if (subView.getBorderIndex() == 1) {
                break;
            }
        }
    }

}
