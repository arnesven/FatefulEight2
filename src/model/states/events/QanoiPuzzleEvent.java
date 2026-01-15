package model.states.events;

import model.Model;
import model.SteppingMatrix;
import model.mainstory.jungletribe.QanoiDisc;
import model.mainstory.jungletribe.QanoiPin;
import model.mainstory.jungletribe.RubiqPuzzleSubView;
import model.states.DailyEventState;
import view.subviews.CollapsingTransition;
import view.subviews.QanoiPuzzleSubView;

import java.util.List;

public class QanoiPuzzleEvent extends DailyEventState {


    private SteppingMatrix<QanoiPin> pins;
    private QanoiDisc currentDisc;

    public QanoiPuzzleEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        QanoiPin firstPin = new QanoiPin();
        firstPin.add(new QanoiDisc(4));
        firstPin.add(new QanoiDisc(3));
        firstPin.add(new QanoiDisc(2));
        firstPin.add(new QanoiDisc(1));
        this.pins = new SteppingMatrix<>(3, 1);
        this.pins.addElements(List.of(new QanoiPin(), firstPin, new QanoiPin()));

        QanoiPuzzleSubView qanoiPuzzleSubView = new QanoiPuzzleSubView(pins);
        CollapsingTransition.transition(model, qanoiPuzzleSubView);
        do {
            waitForReturnSilently();
            if (currentDisc == null) {
                if (!pins.getSelectedElement().isEmpty()) {
                    currentDisc = pins.getSelectedElement().getTopDisc();
                    qanoiPuzzleSubView.setDiscCursor(currentDisc);
                }
            } else {
                pins.getSelectedElement().add(currentDisc);
                qanoiPuzzleSubView.setDiscCursor(null);
                currentDisc = null;
            }
        } while (true);
    }
}
