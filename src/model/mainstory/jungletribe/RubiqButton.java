package model.mainstory.jungletribe;

import view.ScreenHandler;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

public abstract class RubiqButton {
    private final int[] indices;
    private final boolean clockwise;
    private final String description;

    public abstract void drawYourself(ScreenHandler screenHandler, Point point);

    public RubiqButton(int[] indices, boolean clockwise, String description) {
        this.indices = indices;
        this.clockwise = clockwise;
        this.description = description;
    }

    public void doAction(List<RubiqBall> balls) {
        innerDoAction(balls, indices, clockwise);
    }

    private static void innerDoAction(List<RubiqBall> balls, int[] indices, boolean clockwise) {
        List<RubiqBall> copy = new ArrayList<>(balls);
        if (clockwise) {
            balls.set(indices[0], copy.get(indices[1]));
            balls.set(indices[1], copy.get(indices[2]));
            balls.set(indices[2], copy.get(indices[3]));
            balls.set(indices[3], copy.get(indices[0]));
        } else {
            balls.set(indices[1], copy.get(indices[0]));
            balls.set(indices[2], copy.get(indices[1]));
            balls.set(indices[3], copy.get(indices[2]));
            balls.set(indices[0], copy.get(indices[3]));
        }
    }

    public String getText() {
        return description + " " + (clockwise ? "clockwise" : "counter-clockwise");
    }

    public void undoAction(List<RubiqBall> list) {
        innerDoAction(list, indices, !clockwise);
    }
}
