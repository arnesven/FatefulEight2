package model.mainstory.jungletribe;

import model.Model;
import model.SteppingMatrix;
import model.states.DailyEventState;
import util.MyRandom;
import view.MyColors;
import view.subviews.CollapsingTransition;

import java.util.ArrayList;
import java.util.List;

public class RubiqPuzzleEvent extends DailyEventState {

        /*
                   0
                   1
              4 5     6 7
                   2
                   3
     */

    private static final int[] OUTER = new int[]{0, 4, 3, 7};
    private static final int[] INNER = new int[]{1, 5, 2, 6};
    private static final int[] NW_QUAD = new int[]{0, 4, 5, 1};
    private static final int[] NE_QUAD = new int[]{0, 1, 6, 7};
    private static final int[] SW_QUAD = new int[]{4, 3, 2, 5};
    private static final int[] SE_QUAD = new int[]{6, 2, 3, 7};

    private static final MyColors[] COLORS = new MyColors[]{MyColors.RED, MyColors.BLUE, MyColors.YELLOW, MyColors.GREEN};

    public RubiqPuzzleEvent(Model model) {
        super(model);
    }

    public static int getGlowLevel(List<RubiqBall> balls, int i) {
        int set = i/2;
        int lower = 2 * set;
        int higher = 2 * set + 1;

        int score = 0;
        score += balls.get(lower).getColor() == COLORS[set] ? 1 : 0;
        score += balls.get(higher).getColor() == COLORS[set] ? 1 : 0;

        if (score > 0) {
            return score + 1;
        }
        if (balls.get(lower).getColor() != balls.get(higher).getColor()) {
            return 1;
        }
        return 0;
    }

    @Override
    protected void doEvent(Model model) {
        List<RubiqBall> list = new ArrayList<>(List.of(new RubiqBall(COLORS[0]), new RubiqBall(COLORS[0]),
                new RubiqBall(COLORS[1]), new RubiqBall(COLORS[1]), new RubiqBall(COLORS[2]),
                new RubiqBall(COLORS[2]), new RubiqBall(COLORS[3]), new RubiqBall(COLORS[3])));

        SteppingMatrix<RubiqButton> buttons = new SteppingMatrix<>(4, 3);
        buttons.addElement(0, 0, new CWRubiqButton(NW_QUAD, true, "Upper Left Quadrant"));
        buttons.addElement(1, 0, new CCWRubiqButton(NW_QUAD, false, "Upper Left Quadrant"));
        buttons.addElement(2, 0, new CWRubiqButton(NE_QUAD, true, "Upper Right Quadrant"));
        buttons.addElement(3, 0, new CCWRubiqButton(NE_QUAD, false, "Upper Right Quadrant"));
        buttons.addElement(0, 1, new CWRubiqButton(SW_QUAD, true, "Lower Left Quadrant"));
        buttons.addElement(1, 1, new CCWRubiqButton(SW_QUAD, false, "Lower Left Quadrant"));
        buttons.addElement(2, 1, new CWRubiqButton(SE_QUAD, true, "Lower Right Quadrant"));
        buttons.addElement(3, 1, new CCWRubiqButton(SE_QUAD, false, "Lower Right Quadrant"));
        buttons.addElement(0, 2, new CWRubiqButton(OUTER, true, "Outer Circle"));
        buttons.addElement(1, 2, new CCWRubiqButton(OUTER, false, "Outer Circle"));
        buttons.addElement(2, 2, new CWRubiqButton(INNER, true, "Inner Circle"));
        buttons.addElement(3, 2, new CCWRubiqButton(INNER, false, "Inner Circle"));

        // Randomize puzzle
        for (int i = 0; i < 1000; ++i) {
            MyRandom.sample(buttons.getElementList()).doAction(list);
        }

        CollapsingTransition.transition(model, new RubiqPuzzleSubView(buttons, list));
        do {
            waitForReturnSilently();
            buttons.getSelectedElement().doAction(list);
        } while (true);
    }
}
