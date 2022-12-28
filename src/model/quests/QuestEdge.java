package model.quests;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;
import java.io.Serializable;

public class QuestEdge implements Serializable {
    public static final boolean HORIZONTAL = true;
    public static final boolean VERTICAL = false;
    private final Sprite horizontalSprite = new Sprite16x16("horipath", "quest.png", 0x41);
    private final Sprite verticalSprite = new Sprite16x16("vertipath", "quest.png", 0x40);
    private final Sprite[] specialSprites = new Sprite[] {
            new Sprite16x16("ulcorner", "quest.png", 0x42),
            new Sprite16x16("urcorner", "quest.png", 0x43),
            new Sprite16x16("llcorner", "quest.png", 0x44),
            new Sprite16x16("lrcorner", "quest.png", 0x45),
            new Sprite16x16("uparrow", "quest.png", 0x46),
            new Sprite16x16("rightarrow", "quest.png", 0x47),
            new Sprite16x16("leftarrow", "quest.png", 0x48),
            new Sprite16x16("downarrow", "quest.png", 0x49),
    };
    private QuestNode node;
    private boolean alignment;

    public QuestEdge(QuestNode node, boolean align, MyColors lineColor) {
        this.node = node;
        this.alignment = align;
        horizontalSprite.setColor2(lineColor);
        verticalSprite.setColor2(lineColor);
        for (Sprite sp : specialSprites) {
            sp.setColor2(lineColor);
        }
    }

    public QuestEdge(QuestNode node, boolean align) {
        this(node, align, MyColors.WHITE);
    }

    public QuestEdge(QuestNode node) {
        this(node, QuestEdge.HORIZONTAL, MyColors.WHITE);
    }

    public QuestNode getNode() {
        return node;
    }

    public void drawYourself(ScreenHandler screenHandler, QuestNode startPoint, int xOffset, int yOffset) {
        int dx = node.getColumn() - startPoint.getColumn();
        int dy = node.getRow() - startPoint.getRow();
        Point from = startPoint.getPosition();
        Point to = node.getPosition();
        if (dx == 0 && dy != 0) {
            goDownWithArrow(screenHandler, from, to, xOffset, yOffset, dy);
        } else if (dy == 0 && dx != 0) {
            goRightWithArrow(screenHandler, from, to, xOffset, yOffset, dx);
        } else {
            Point mid;
            int cornerNum;
            if (alignment) {
                mid = new Point(to.x, from.y);
                goRight(screenHandler, from, mid, xOffset, yOffset, dx);
                goDownWithArrow(screenHandler, mid, to, xOffset, yOffset, dy);
                cornerNum = (dx<0?0:1) + (dy<0?2:0);
            } else {
                mid = new Point(from.x, to.y);
                goDown(screenHandler, from, mid, xOffset, yOffset, dy);
                goRightWithArrow(screenHandler, mid, to, xOffset, yOffset, dx);
                cornerNum = (dx<0?1:0) + (dy<0?0:2);
            }

            screenHandler.register("corner", new Point(xOffset + 4*mid.x + 1, yOffset +4*mid.y + 1), specialSprites[cornerNum]);
        }
    }

    private void goRight(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, int dx) {
        if (dx < 0) {
            goStraight(screenHandler, to, from, xOffset, yOffset, true);
        } else {
            goStraight(screenHandler, from, to, xOffset, yOffset, true);
        }
    }

    private void goRightWithArrow(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, int dx) {
        if (dx < 0) {
            goStraight(screenHandler, to, from, xOffset, yOffset, true);
            screenHandler.clearForeground(xOffset + 4*to.x + 2, xOffset + 4*to.x + 3,
                    yOffset +4*to.y+1, yOffset +4*to.y+1);
            screenHandler.register("arrow", new Point(xOffset + 4*to.x + 3, yOffset +4*to.y+1), specialSprites[6]);
        } else {
            goStraight(screenHandler, from, to, xOffset, yOffset, true);
            screenHandler.clearForeground(xOffset + 4*to.x - 1, xOffset + 4*to.x,
                    yOffset +4*to.y+1, yOffset +4*to.y+1);
            screenHandler.register("arrow", new Point(xOffset + 4*to.x - 1, yOffset +4*to.y+1), specialSprites[5]);
        }
    }


    private void goDown(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, int dy) {
        if (dy < 0) {
            goStraight(screenHandler, to, from, xOffset, yOffset, false);
        } else {
            goStraight(screenHandler, from, to, xOffset, yOffset, false);
        }
    }

    private void goDownWithArrow(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, int dy) {
        if (dy < 0) {
            goStraight(screenHandler, to, from, xOffset, yOffset, false);
            screenHandler.clearForeground(xOffset + 4*to.x + 1,xOffset + 4*to.x + 1,
                    yOffset +4*to.y+2, yOffset +4*to.y+3);
            screenHandler.register("arrow", new Point(xOffset + 4*to.x + 1, yOffset +4*to.y+3), specialSprites[4]);
        } else {
            goStraight(screenHandler, from, to, xOffset, yOffset, false);
            screenHandler.clearForeground(xOffset + 4*to.x + 1,xOffset + 4*to.x + 1,
                    yOffset +4*to.y-1, yOffset +4*to.y);
            screenHandler.register("arrow", new Point(xOffset + 4*to.x + 1, yOffset +4*to.y-1), specialSprites[7]);
        }
    }

    private void goStraight(ScreenHandler screenHandler, Point from, Point to, int xOffset, int yOffset, boolean horizontal) {
        from = new Point(from);
        to = new Point(to);
        int times = 0;
        while ((from.x != to.x && horizontal) || (from.y != to.y && !horizontal)) {
            if (horizontal) {
                screenHandler.register("horipath1", new Point(xOffset + 4 * from.x + 2, yOffset + 4 * from.y + 1), horizontalSprite);
                screenHandler.register("horipath2", new Point(xOffset + 4 * from.x + 4, yOffset + 4 * from.y + 1), horizontalSprite);
                from.x++;
            } else {
                screenHandler.register("vertipath1", new Point(xOffset + 4 * from.x + 1, yOffset + 4 * from.y + 2), verticalSprite);
                screenHandler.register("vertipath2", new Point(xOffset + 4 * from.x + 1, yOffset + 4 * from.y + 4), verticalSprite);
                from.y++;
            }
            if (times++ > 100) {
                throw new IllegalStateException("Drew Edge 100 times, something is wrong!");
            }
        }
    }

    public boolean getAlignment() {
        return alignment;
    }
}
