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
    private Point startingPoint;

    public GardenMazeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        this.startingPoint = new Point(4, 0);
        GardenMaze maze = GardenMaze.generate(10, 10, startingPoint.x);
        GardenMazeSubView subView = new GardenMazeSubView(maze, new Point(startingPoint.x, startingPoint.y));
        CollapsingTransition.transition(model, subView);
        handleReturnPressed(subView, true);

        do {
            waitUntil(subView, GardenMazeSubView::isDone);
            if (subView.statueFound()) {
                leaderSay("There's the statue.");
                println("Press enter to continue");
                waitForReturnSilently();
                break;
            }
            if (handleReturnPressed(subView, false)) {
                break;
            }
        } while (true);
        setCurrentTerrainSubview(model);
    }

    private boolean handleReturnPressed(GardenMazeSubView subView, boolean firstTime) {
        waitForReturnSilently();
        if (subView.getBorderIndex() == 1) {
            leaderSay("Ach, this is to tough. Let's give up.");
            return true;
        }
        if (subView.getBorderIndex() == 0) {
            if (!firstTime) {
                println("You retrace your steps and exit the maze. You return to the porch to get a good look at it.");
                subView.setShowMap(true);
            }
        } else {
            if (firstTime) {
                println("You enter the garden maze.");
            } else {
                println("You enter the maze again.");
            }
            subView.setPositionAndFacing(new Point(startingPoint.x, startingPoint.y), 2);
            subView.setShowMap(false);
        }
        return false;
    }

}
