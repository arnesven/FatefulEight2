package model.states.events;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.states.DailyEventState;
import model.states.maze.GardenMaze;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyRandom;
import util.MyStrings;
import view.subviews.CollapsingTransition;
import view.subviews.GardenMazeSubView;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

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
    private static final int BET_SIZE = 10;
    private static final List<String> STATUE_NAMES = List.of("Cassandra", "Belladonna", "Adromeda",
            "Evelina", "Francesca", "Galexia", "Hubertina", "Inderella", "Kassiopeia", "Leonidia",
            "Marcella", "Narcissa", "Ophelia", "Quentixia", "Priscilla", "Roberta", "Samantha",
            "Talandria", "Vitriola", "Wolverina");
    private static final String MAZE_WINS_KEY = "GARDENER_MAZE_WINS";
    private static final int MAX_WINS = 5;
    private Point startingPoint;

    public GardenMazeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        AdvancedAppearance gardener = PortraitSubView.makeRandomPortrait(Classes.GARDENER);
        println("You thought you were heading into town but it seems this road is taking you in the opposite direction. " +
                "You come to a large mansion with a vast garden. The garden has neatly trimmed bushes, hedges and trees. " +
                "A gardener is trimming the lawn near the house. " + heOrSheCap(gardener.getGender()) + " approaches you.");
        model.getLog().waitForAnimationToFinish();
        showExplicitPortrait(model, gardener, "Gardener");
        portraitSay("Howdy, you here to meet the master?");
        leaderSay("Actually, we've lost our way. Can you point us in the direction of town?");
        portraitSay("Hehe. Well don't go this way, my maze in the master's garden will get you even more lost.");
        leaderSay("A maze?");
        portraitSay("Yessir. You see those tall hedges over there? They're quite the labyrinth. I've been working on it for years. " +
                "The master even got lost in there once!");
        leaderSay("Can't be that tricky...");
        portraitSay("Oh it is! Let's have a proper look at it from the porch.");
        println("The gardener leads you up on a large porch next to the house. From there you can see the garden maze. " +
                "It's actually more impressive than you had anticipated.");
        leaderSay("Interesting. But still, can't be too hard to navigate.");
        int previousWins = Math.min(MAX_WINS, getNumberOfWins(model));
        int timeLimitMinutes = MAX_WINS + 1 - previousWins;
        portraitSay("I'll tell you what. There's a statue in the middle of the maze. I bet you can't find it in under " +
                MyStrings.numberWord(timeLimitMinutes) + " minute" + (timeLimitMinutes > 1 ? "s" : "")  + ".");
        leaderSay("Hmm maybe. How big a bet do you recon?");

        int betSize = Math.max(1, Math.min(model.getParty().getGold(), BET_SIZE * (previousWins + 1)));
        portraitSay("How about " + betSize + " gold?");
        if (model.getParty().getGold() > 0) {
            print("Take the bet? (Y/N) ");
        }
        if (model.getParty().getGold() == 0 || !yesNoInput()) {
            leaderSay("Actually, we don't have time for that. Just point us in the direction of town.");
            portraitSay("Just go back the way you came. There aren't too many roads around here.");
            leaderSay("Good luck with your maze.");
            portraitSay("So long friend.");
            return;
        }
        leaderSay("I accept.");
        portraitSay("Great! When you're ready, just step into the maze and I'll turn my hourglass. " +
                "If you find the statue, call out the name written on plaque on the base. " +
                "That way I'll know you've found it, not that I think that you will, he he he.");
        leaderSay("Got it. I'll just have a look at the maze first then.");
        portraitSay("Take all the time you want here on the porch. " +
                "You can even come back up here if you need to take another look.");
        leaderSay("I don't think I'll need to.");
        portraitSay("Sure you wont.");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);

        ClientSoundManager.playBackgroundMusic(BackgroundMusic.caveSong);
        int mazeSize = 12 + previousWins * 2;
        this.startingPoint = new Point(MyRandom.randInt(1, mazeSize-1), 0);
        GardenMaze maze = GardenMaze.generate(mazeSize, mazeSize, startingPoint.x);
        GardenMazeSubView subView = new GardenMazeSubView(maze, new Point(startingPoint.x, startingPoint.y), timeLimitMinutes * 60);
        CollapsingTransition.transition(model, subView);
        do {
            waitForReturnSilently();
            if (subView.getBorderIndex() == -1) {
                println("You enter the garden maze.");
                enterMaze(subView);
                printQuote("Gardener", "I'm turning the hourglass now. Good luck!");
                subView.startTimer();
                break;
            } else if (subView.getBorderIndex() == 1) {
                leaderSay("Now that I think of it, this is just a waste of time. Let's get out of here.");
                printQuote("Gardener", "Hmph! Well, gardening just isn't for everybody I suppose.");
                return;
            }
        } while (true);

        boolean betWin = false;
        do {
            waitUntil(subView, GardenMazeSubView::isDone);
            if (subView.statueFound()) {
                leaderSay("There's the statue. What's written on the bottom? " + "Hey gardener! " + MyRandom.sample(STATUE_NAMES) + "!");
                printQuote("Gardener", "Shucks!");
                println("Press enter to continue");
                waitForReturnSilently();
                betWin = true;
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

        if (betWin) {
            setCurrentTerrainSubview(model);
            showExplicitPortrait(model, gardener, "Gardener");
            leaderSay("Like I said gardener, it wasn't too tricky.");
            portraitSay("I commend your sense of direction! I must keep working and make " +
                    "my maze even more difficult to solve. Here's your money.");
            println("You got " + betSize + " gold from the gardener.");
            model.getParty().addToGold(betSize);
            leaderSay("Thank you. See you around gardener.");
            portraitSay("Bye for now.");
            incrementWins(model);
        } else {
            subView.setShowMap(true);
            printQuote("Gardener", "See. Not so easy after all, was it?");
            leaderSay("I guess not. Here's your money.");
            println("You hand over " + betSize + " gold to the gardener.");
            model.getParty().addToGold(-betSize);
            printQuote("Gardener", "Much obliged.");
            leaderSay("See you around gardener.");
            printQuote("Gardener", "Bye for now.");
            print("Press enter to continue.");
            waitForReturn();
        }
    }

    private void incrementWins(Model model) {
        if (!model.getSettings().getMiscCounters().containsKey(MAZE_WINS_KEY)) {
            model.getSettings().getMiscCounters().put(MAZE_WINS_KEY, 0);
        }
        int wins = getNumberOfWins(model) + 1;
        model.getSettings().getMiscCounters().put(MAZE_WINS_KEY, wins);
        if (wins == MAX_WINS) {
            println("Congratulations! You have mastered the gardener's maze!");
            println("You gain 1 reputation!");
            model.getParty().addToReputation(1);
        }
    }

    private int getNumberOfWins(Model model) {
        if (model.getSettings().getMiscCounters().containsKey(MAZE_WINS_KEY)) {
            return model.getSettings().getMiscCounters().get(MAZE_WINS_KEY);
        }
        return 0;
    }

    private boolean handleReturnPressed(GardenMazeSubView subView) {
        waitForReturnSilently();
        if (subView.getBorderIndex() == 1) {
            leaderSay("Ach, this is to tough. Let's give up.");
            subView.setTime(0);
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
