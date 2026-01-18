package model.states.events;

import model.Model;
import model.SteppingMatrix;
import model.mainstory.jungletribe.QanoiDisc;
import model.mainstory.jungletribe.QanoiPin;
import model.mainstory.jungletribe.RubiqBall;
import model.mainstory.jungletribe.RubiqPuzzleSubView;
import model.states.DailyEventState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.subviews.CollapsingTransition;
import view.subviews.QanoiPuzzleSubView;
import view.subviews.SubView;

import java.util.ArrayList;
import java.util.List;

public class QanoiPuzzleEvent extends DailyEventState {


    private SteppingMatrix<QanoiPin> pins;
    private MyPair<List<Integer>, List<Integer>> facit;

    public QanoiPuzzleEvent(Model model, MyPair<List<Integer>, List<Integer>> oldSolution) {
        super(model);
        facit = oldSolution;
    }

    public QanoiPuzzleEvent(Model model) {
        this(model, null);
    }

    @Override
    protected void doEvent(Model model) {
        QanoiPin firstPin = new QanoiPin();
        firstPin.add(new QanoiDisc(4));
        firstPin.add(new QanoiDisc(3));
        firstPin.add(new QanoiDisc(2));
        firstPin.add(new QanoiDisc(1));

        QanoiDisc currentDisc = null;
        this.pins = new SteppingMatrix<>(3, 1);
        pins.addElements(List.of(new QanoiPin(), firstPin, new QanoiPin()));
        this.facit = makeRandomSolution();

        SubView oldSubView = model.getSubView();
        QanoiPuzzleSubView qanoiPuzzleSubView = new QanoiPuzzleSubView(pins, facit);
        CollapsingTransition.transition(model, qanoiPuzzleSubView);
        leaderSay("Three pins, four discs. I guess " + iOrWe() + " could move the discs to the other pins?");
        int strikes = 0;
        do {
            waitForReturnSilently();
            if (currentDisc == null) { // Pick up disc
                if (!pins.getSelectedElement().isEmpty()) {
                    currentDisc = pins.getSelectedElement().getTopDisc();
                    qanoiPuzzleSubView.setDiscCursor(currentDisc);
                }

            } else { // Put disc down.
                pins.getSelectedElement().add(currentDisc);
                qanoiPuzzleSubView.setDiscCursor(null);
                currentDisc = null;

                if (isBigOnLittle(pins.getSelectedElement())) {
                    qanoiPuzzleSubView.setShakeEnabled(true);
                    strikes++;
                    delay(250 + 250 * strikes);
                    if (strikes < 3) {
                        qanoiPuzzleSubView.setShakeEnabled(false);
                    }
                    qanoiPuzzleSubView.setCracks(strikes);
                    if (strikes == 1) {
                        println("The floor suddenly shakes violently.");
                        leaderSay("Uh-oh! I don't know what just happened, but it can't be good.");
                        model.getLog().waitForAnimationToFinish();

                    } else if (strikes == 2) {
                        println("Once again, the floor shakes.");
                        model.getLog().waitForAnimationToFinish();
                        leaderSay("I don't think that was a good move.");
                    } else {
                        leaderSay("Darn it! Did it again.");
                        model.getLog().waitForAnimationToFinish();
                        leaderSay("I think we aren't allowed to put a bigger disc on a smaller one.");
                    }
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

    private boolean puzzleSolved() {
        return MyLists.equal(facit.first, MyLists.transform(pins.getElementAt(0, 0), QanoiDisc::getSize)) &&
                MyLists.equal(facit.second, MyLists.transform(pins.getElementAt(2, 0), QanoiDisc::getSize)) &&
                pins.getElementAt(1, 0).isEmpty();
    }

    public boolean solvedPuzzle() {
        return puzzleSolved();
    }

    private boolean isBigOnLittle(QanoiPin pin) {
        if (pin.size() < 2) {
            return false;
        }
        return pin.get(pin.size() - 1).getSize() > pin.get(pin.size() - 2).getSize();
    }

    private MyPair<List<Integer>, List<Integer>> makeRandomSolution() {
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (int i = 1; i <= 4; ++i) {
            if (MyRandom.flipCoin()) {
                left.addFirst(i);
            } else {
                right.addFirst(i);
            }
        }
        return new MyPair<>(left, right);
    }

    public MyPair<List<Integer>, List<Integer>> getPuzzleSolution() {
        return facit;
    }
}
