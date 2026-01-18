package model.mainstory.jungletribe;

import model.Model;
import model.SteppingMatrix;
import model.states.DailyEventState;
import util.Arithmetics;
import util.MyLists;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.SubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SudoqPuzzleEvent extends DailyEventState {

    private enum SudoqStatus {
        unsolved("UNUSED"),
        solved("UNUSED"),
        badColumn("column"),
        badRow("row"),
        badRegion("box");

        private final String decription;

        SudoqStatus(String text) {
            this.decription = text;
        }

        public String getDescription() {
            return decription;
        }
    }

    private SudoqStatus lastStatus = SudoqStatus.unsolved;

    public static final int PUZZLE_SIZE = 6;
    private SteppingMatrix<SudoqSymbol> matrix = null;
    private SudoqSymbol currentSymbol = null;

    public SudoqPuzzleEvent(Model model, SteppingMatrix<SudoqSymbol> oldState) {
        super(model);
        matrix = oldState;
    }

    public SudoqPuzzleEvent(Model model) {
        this(model, null);
    }

    @Override
    protected void doEvent(Model model) {
        SubView oldSubView = model.getSubView();
        if (matrix == null) {
            this.matrix = new SteppingMatrix<>(PUZZLE_SIZE, PUZZLE_SIZE + 1);
            fillWithSolution(matrix);
            randomizeSolution(matrix);
            removeSymbols(matrix);
            for (int i = 0; i < PUZZLE_SIZE; ++i) {
                matrix.addElement(i, PUZZLE_SIZE, new SudoqSymbol(i + 1, false));
            }
        }

        SudoqPuzzleSubView subView = new SudoqPuzzleSubView(matrix);
        CollapsingTransition.transition(model, subView);
        leaderSay("What a strange pattern on the wall, and sockets...");
        leaderSay("And here, on the ground, colorful rocks. How curious.");
        int strikes = 0;
        do {
            waitForReturnSilently();
            if (matrix.getSelectedPoint().y == PUZZLE_SIZE) { // Selecting symbol
                if (currentSymbol != null && currentSymbol.getValue() == matrix.getSelectedElement().getValue()) {
                    deselectSymbol(subView);
                } else {
                    currentSymbol = matrix.getSelectedElement();
                    subView.setSelectedSymbol(currentSymbol);
                }
            } else if (currentSymbol != null) { // Place selected
                strikes = placeSelectedSymbol(model, subView, strikes);
            } else { // Remove
                if (matrix.getSelectedElement().isPreset()) {
                    leaderSay("Hmm. That can't be removed.");
                } else {
                    replaceSymbol(matrix, matrix.getSelectedPoint(), SudoqSymbol.makeBlank());
                }
            }
        } while (strikes < 3 && !puzzleSolved());
        if (strikes == 3) {
            println("Suddenly a hidden door opens, and a huge hulking figure approaches you!");
        } else {
            println("The puzzle has been solved!");
        }
        model.getLog().waitForAnimationToFinish();
        CollapsingTransition.transition(model, oldSubView);
    }

    private int placeSelectedSymbol(Model model, SudoqPuzzleSubView subView, int strikes) {
        if (matrix.getSelectedElement().isPreset()) {
            leaderSay("Can't place a stone there...");
            return strikes;
        }
        Point placementPoint = new Point(matrix.getSelectedPoint());
        replaceSymbol(matrix, placementPoint, currentSymbol.copy());
        this.lastStatus = checkPlacement(matrix, placementPoint);
        if (isBad(lastStatus)) {
            subView.setShakeEnabled(true);
            strikes++;
            delay(250 + 250 * strikes);
            if (strikes < 3) {
                subView.setShakeEnabled(false);
            }
            subView.setCracks(strikes);
            if (strikes == 1) {
                println("The floor suddenly shakes violently.");
                leaderSay("Uh-oh! I don't know what just happened, but it can't be good.");
                model.getLog().waitForAnimationToFinish();
                leaderSay("Hey! The rock fell out. How odd!");
                replaceSymbol(matrix, placementPoint, SudoqSymbol.makeBlank());
                deselectSymbol(subView);
            } else if (strikes == 2) {
                println("Once again, the floor shakes.");
                model.getLog().waitForAnimationToFinish();
                leaderSay("I don't think that was a good move.");
                replaceSymbol(matrix, placementPoint, SudoqSymbol.makeBlank());
                deselectSymbol(subView);
            } else {
                leaderSay("Darn it! Did it again.");
                model.getLog().waitForAnimationToFinish();
                leaderSay("I don't think " + imOrWere() +
                        " allowed to place two of the same rocks in the same " + lastStatus.getDescription() + ".");
                replaceSymbol(matrix, placementPoint, SudoqSymbol.makeBlank());
                deselectSymbol(subView);
            }
        }
        System.out.println(lastStatus);
        return strikes;
    }

    private void deselectSymbol(SudoqPuzzleSubView subView) {
        currentSymbol = null; // deselect
        subView.setSelectedSymbol(null);
    }

    private boolean isBad(SudoqStatus lastStatus) {
        return lastStatus != SudoqStatus.unsolved && lastStatus != SudoqStatus.solved;
    }

    private SudoqStatus checkPlacement(SteppingMatrix<SudoqSymbol> matrix, Point p) {
        SudoqSymbol placed = matrix.getElementAt(p.x, p.y);

        for (int col = 0; col < PUZZLE_SIZE; ++col) {
            if (col != p.x && matrix.getElementAt(col, p.y).getValue() == placed.getValue()) {
                return SudoqStatus.badRow;
            }
        }

        for (int row = 0; row < PUZZLE_SIZE; ++row) {
            if (row != p.y && matrix.getElementAt(p.x, row).getValue() == placed.getValue()) {
                return SudoqStatus.badColumn;
            }
        }

        int regionWidth = PUZZLE_SIZE / 2;
        int regionX = p.x / regionWidth;
        int regionHeight = PUZZLE_SIZE / 3;
        int regionY = p.y / regionHeight;

        for (int row = regionY * regionHeight; row < (regionY+1) * regionHeight; ++row) {
            for (int col = regionX * regionWidth; col < (regionX+1) * regionWidth; ++col) {
                if (!(p.x == col && p.y == row) &&
                        matrix.getElementAt(col, row).getValue() == placed.getValue()) {
                    return SudoqStatus.badRegion;
                }
            }
        }

        for (int row = 0; row < matrix.getRows(); ++row) {
            for (int col = 0; col < matrix.getColumns(); ++col) {
                if (matrix.getElementAt(col, row).getValue() == 0) {
                    return SudoqStatus.unsolved;
                }
            }
        }
        return SudoqStatus.solved;
    }

    private void randomizeSolution(SteppingMatrix<SudoqSymbol> matrix) {
        for (int i = 0; i < 100; ++i) {
            if (MyRandom.flipCoin()) {
                // SWAP ROWS IN BANDS
                int band = MyRandom.randInt(PUZZLE_SIZE / 2);
                matrix.swapRows(2*band, 2*band+1);
            } else {
                // SWAP COLUMNS IN STACKS
                int stackSize = PUZZLE_SIZE / 2;
                int stack = MyRandom.randInt(PUZZLE_SIZE / 3);
                int col1 = MyRandom.randInt(stackSize);
                int col2 = Arithmetics.incrementWithWrap(col1, stackSize);
                matrix.swapColumns(stack * stackSize + col1, stack * stackSize + col2);
            }
        }
    }

    private void removeSymbols(SteppingMatrix<SudoqSymbol> matrix) {
        int clues = PUZZLE_SIZE * PUZZLE_SIZE;
        while (clues > 12) {
            Point p = new Point(MyRandom.randInt(PUZZLE_SIZE), MyRandom.randInt(PUZZLE_SIZE));
            if (matrix.getElementAt(p.x, p.y).getValue() > 0) {
                replaceSymbol(matrix, p, SudoqSymbol.makeBlank());
                clues--;
            }
        }
    }

    private static void fillWithSolution(SteppingMatrix<SudoqSymbol> matrix) {
        List<SudoqSymbol> template = new ArrayList<>();
        for (int i = 0; i < PUZZLE_SIZE; ++i) {
            template.add(new SudoqSymbol(i+1, true));
        }

        for (int row = 0; row < PUZZLE_SIZE; ++row) {
            for (SudoqSymbol s : template) {
                matrix.addElementLast(s.copy());
            }
            template.add(template.removeFirst());
            template.add(template.removeFirst());
            template.add(template.removeFirst());
            if (row % 2 == 1) {
                template.add(template.removeFirst());
            }
        }
    }

    private static void replaceSymbol(SteppingMatrix<SudoqSymbol> matrix, Point current, SudoqSymbol newSymbol) {
        matrix.remove(matrix.getElementAt(current.x, current.y));
        matrix.addElement(current.x, current.y, newSymbol);
        matrix.setSelectedPoint(current);
    }

    private boolean puzzleSolved() {
        return lastStatus == SudoqStatus.solved;
    }

    public SteppingMatrix<SudoqSymbol> getPuzzleState() {
        return matrix;
    }

    public boolean solvedPuzzle() {
        return puzzleSolved();
    }
}
