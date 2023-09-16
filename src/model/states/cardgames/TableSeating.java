package model.states.cardgames;

import view.subviews.SubView;

import java.awt.*;

public class TableSeating {
    public final int rotation;
    public final int offset;
    private final Point handPosition;
    private final Point cardPos;
    private final Point dxdy;

    public TableSeating(int rotation, int offset) {
        this.rotation = rotation;
        this.offset = offset;
        switch (rotation) {
            case 0:
                handPosition = new Point(SubView.X_OFFSET + offset, SubView.Y_MAX-8);
                cardPos = new Point(SubView.X_OFFSET + offset-2, SubView.Y_MAX-2);
                dxdy = new Point(0, -1);
                break;
            case 90:
                handPosition = new Point(SubView.X_OFFSET, SubView.Y_OFFSET + offset);
                cardPos = new Point(SubView.X_OFFSET-2, SubView.Y_OFFSET + offset-2);
                dxdy = new Point(1, 0);
                break;
            case 180:
                handPosition = new Point(SubView.X_OFFSET + offset, SubView.Y_OFFSET);
                cardPos = new Point(SubView.X_OFFSET + offset, SubView.Y_OFFSET-2);
                dxdy = new Point(0, 1);
                break;
            default:
                handPosition = new Point(SubView.X_MAX-8, SubView.Y_OFFSET + offset);
                cardPos = new Point(SubView.X_MAX-2, SubView.Y_OFFSET + offset);
                dxdy = new Point(-1, 0);
                break;
        }
    }

    public Point getHandPosition() {
        return handPosition;
    }

    public Point getCardPosition() {
        return cardPos;
    }

    public Point getHandDirection() {
        return dxdy;
    }
}
