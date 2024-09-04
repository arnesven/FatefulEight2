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
        GardenMazeSubView subView = new GardenMazeSubView(maze, new Point(startingPoint.x, startingPoint.y), 60);
        CollapsingTransition.transition(model, subView);
        do {
            waitForReturnSilently();
            if (subView.getBorderIndex() == -1) {
                println("You enter the garden maze.");
                enterMaze(subView);
                printQuote("Gardener", "I'm turning the hourglass now, good luck!");
                model.getLog().waitForAnimationToFinish();
                subView.startTimer();
                break;
            } else if (subView.getBorderIndex() == 1) {
                leaderSay("Now that I think of it, this is just a waste of time. Let's get out of here.");
                printQuote("Gardener", "Hmph! Well, gardening just isn't for everybody I suppose.");
                return;
            }
        } while (true);

        do {
            waitUntil(subView, GardenMazeSubView::isDone);
            if (subView.statueFound()) {
                leaderSay("There's the statue.");
                println("Press enter to continue");
                waitForReturnSilently();
                break;
            } else if (subView.isOutOfTime()) {
                printQuote("Gardener", "You're time is up friend! You lost the bet!");
                leaderSay("Shoot! Must've gotten lost somewhere...");
                println("Press enter to continue");
                waitForReturnSilently();
                break;
            }
            if (handleReturnPressed(subView)) {
                break;
            }
        } while (true);
    }

    private boolean handleReturnPressed(GardenMazeSubView subView) {
        waitForReturnSilently();
        if (subView.getBorderIndex() == 1) {
            leaderSay("Ach, this is to tough. Let's give up.");
            return true;
        }
        if (subView.getBorderIndex() == 0) {
            println("You retrace your steps and exit the maze. You return to the porch to get a good look at it.");
            subView.setShowMap(true);
        } else {
            println("You enter the maze again.");
            enterMaze(subView);
        }
        return false;
    }

    private void enterMaze(GardenMazeSubView subView) {
        subView.setPositionAndFacing(new Point(startingPoint.x, startingPoint.y), 2);
        subView.setShowMap(false);
    }

}
